// عسل‌آباد - سرور آنلاین بدون وابستگی (فقط Node 18+)
const http = require('http'), fs = require('fs'), path = require('path'), crypto = require('crypto');
const PORT = process.env.PORT || 3000;
const DATA = process.env.DATA_DIR || path.join(__dirname, 'data');
const ADMIN_PASSWORD = process.env.ADMIN_PASSWORD || 'admin1234'; // حتماً در هاست عوض کنید
fs.mkdirSync(DATA, { recursive: true });
const DB_FILE = path.join(DATA, 'db.json');

let db = { settings: { gameName: 'عسل‌آباد', creator: 'پریا چشم‌رنگی', welcome: 'به عسل‌آباد خوش اومدی! 🍯', announcement: '', bannedWords: [], chatEnabled: true, rooms: ['باغ گل', 'کندو', 'بازار'] },
  users: {}, banned: {}, muted: {}, messages: [] };
try { db = Object.assign(db, JSON.parse(fs.readFileSync(DB_FILE, 'utf8'))); } catch {}
let dirty = false; const save = () => { dirty = true; };
setInterval(() => { if (dirty) { dirty = false; fs.writeFile(DB_FILE, JSON.stringify(db), () => {}); } }, 3000);

const clients = new Map(); // id -> {res, name, room, x, y, color, token}
const adminTokens = new Set();
const sign = s => crypto.createHmac('sha256', ADMIN_PASSWORD).update(s).digest('hex');
const rid = () => crypto.randomBytes(8).toString('hex');
const clean = (s, n) => String(s || '').replace(/[<>]/g, '').trim().slice(0, n);
const world = { flowers: [] };
function spawnFlowers() { while (world.flowers.length < 12) world.flowers.push({ id: rid(), x: 60 + Math.random() * 680, y: 80 + Math.random() * 380, room: db.settings.rooms[Math.floor(Math.random() * db.settings.rooms.length)] }); }
spawnFlowers();

function send(c, ev, data) { try { c.res.write(`event: ${ev}\ndata: ${JSON.stringify(data)}\n\n`); } catch {} }
function broadcast(ev, data, room) { for (const c of clients.values()) if (!room || c.room === room) send(c, ev, data); }
const pub = c => ({ id: c.id, name: c.name, room: c.room, x: c.x, y: c.y, color: c.color, level: (db.users[c.token] || {}).level || 1 });
const roster = () => [...clients.values()].map(pub);
const body = req => new Promise(r => { let b = ''; req.on('data', d => { b += d; if (b.length > 1e5) req.destroy(); }); req.on('end', () => { try { r(JSON.parse(b || '{}')); } catch { r({}); } }); });
const json = (res, code, o) => { res.writeHead(code, { 'Content-Type': 'application/json; charset=utf-8', 'Access-Control-Allow-Origin': '*' }); res.end(JSON.stringify(o)); };
const isAdmin = req => adminTokens.has((req.headers.authorization || '').replace('Bearer ', ''));
const levelOf = h => 1 + Math.floor(Math.sqrt(h / 10));

const MIME = { '.html': 'text/html; charset=utf-8', '.js': 'text/javascript', '.css': 'text/css', '.png': 'image/png', '.svg': 'image/svg+xml', '.json': 'application/json' };

http.createServer(async (req, res) => {
  const u = new URL(req.url, 'http://x'); const p = u.pathname;
  if (req.method === 'OPTIONS') { res.writeHead(204, { 'Access-Control-Allow-Origin': '*', 'Access-Control-Allow-Headers': '*', 'Access-Control-Allow-Methods': '*' }); return res.end(); }

  if (p === '/api/config') return json(res, 200, { gameName: db.settings.gameName, creator: db.settings.creator, welcome: db.settings.welcome, rooms: db.settings.rooms, announcement: db.settings.announcement, online: clients.size });

  if (p === '/api/join' && req.method === 'POST') {
    const b = await body(req); const name = clean(b.name, 16); if (name.length < 2) return json(res, 400, { error: 'اسم حداقل ۲ حرف باشد' });
    let token = clean(b.token, 40);
    if (!token || !db.users[token]) { token = rid() + rid(); db.users[token] = { name, honey: 0, level: 1, created: Date.now() }; }
    if (db.banned[token]) return json(res, 403, { error: 'حساب شما مسدود شده است: ' + db.banned[token] });
    const nameTaken = [...clients.values()].some(c => c.name === name && c.token !== token);
    if (nameTaken) return json(res, 409, { error: 'این اسم الان آنلاین است' });
    db.users[token].name = name; db.users[token].last = Date.now(); save();
    return json(res, 200, { token, honey: db.users[token].honey, level: db.users[token].level });
  }

  if (p === '/api/stream') {
    const token = u.searchParams.get('token'), user = db.users[token]; if (!user || db.banned[token]) { res.writeHead(403); return res.end(); }
    for (const [i, c] of clients) if (c.token === token) { try { c.res.end(); } catch {} clients.delete(i); }
    res.writeHead(200, { 'Content-Type': 'text/event-stream', 'Cache-Control': 'no-cache', 'Connection': 'keep-alive', 'Access-Control-Allow-Origin': '*', 'X-Accel-Buffering': 'no' });
    const id = rid(); const c = { id, res, token, name: user.name, room: db.settings.rooms[0], x: 400, y: 280, color: parseInt(token.slice(0, 6), 16) % 360 };
    clients.set(id, c);
    send(c, 'init', { id, me: pub(c), players: roster(), flowers: world.flowers.filter(f => f.room === c.room), history: db.messages.filter(m => m.room === c.room).slice(-40), honey: user.honey });
    broadcast('join', pub(c), c.room); broadcast('online', { n: clients.size });
    const ping = setInterval(() => { try { res.write(': ping\n\n'); } catch {} }, 20000);
    req.on('close', () => { clearInterval(ping); if (clients.get(id) === c) { clients.delete(id); broadcast('leave', { id }, c.room); broadcast('online', { n: clients.size }); } });
    return;
  }

  if (p === '/api/act' && req.method === 'POST') {
    const b = await body(req); const c = [...clients.values()].find(c => c.id === b.id && c.token === b.token); if (!c) return json(res, 401, { error: 'no session' });
    const user = db.users[c.token];
    if (b.type === 'move') { c.x = Math.max(20, Math.min(780, +b.x || 0)); c.y = Math.max(40, Math.min(500, +b.y || 0)); broadcast('move', { id: c.id, x: c.x, y: c.y }, c.room); return json(res, 200, {}); }
    if (b.type === 'room') {
      if (!db.settings.rooms.includes(b.room)) return json(res, 400, {});
      broadcast('leave', { id: c.id }, c.room); c.room = b.room; c.x = 400; c.y = 280;
      send(c, 'init', { id: c.id, me: pub(c), players: roster().filter(p => p.room === c.room), flowers: world.flowers.filter(f => f.room === c.room), history: db.messages.filter(m => m.room === c.room).slice(-40), honey: user.honey });
      broadcast('join', pub(c), c.room); return json(res, 200, {});
    }
    if (b.type === 'chat') {
      if (!db.settings.chatEnabled) return json(res, 403, { error: 'چت موقتاً بسته است' });
      const m = db.muted[c.token]; if (m && m > Date.now()) return json(res, 403, { error: 'شما سکوت شده‌اید' });
      let text = clean(b.text, 200); if (!text) return json(res, 400, {});
      c.lastChat = c.lastChat || 0; if (Date.now() - c.lastChat < 700) return json(res, 429, { error: 'آهسته‌تر!' }); c.lastChat = Date.now();
      for (const w of db.settings.bannedWords) if (w && text.includes(w)) text = text.split(w).join('***');
      const msg = { id: rid(), room: c.room, from: c.id, name: c.name, text, t: Date.now() };
      db.messages.push(msg); if (db.messages.length > 2000) db.messages.splice(0, 500); save();
      broadcast('chat', msg, c.room); return json(res, 200, {});
    }
    if (b.type === 'collect') {
      const i = world.flowers.findIndex(f => f.id === b.flower && f.room === c.room); if (i < 0) return json(res, 200, { ok: false });
      const f = world.flowers[i]; if (Math.hypot(f.x - c.x, f.y - c.y) > 90) return json(res, 200, { ok: false });
      world.flowers.splice(i, 1); user.honey += 1; user.level = levelOf(user.honey); save();
      broadcast('flowerGone', { id: f.id }, c.room); setTimeout(() => { spawnFlowers(); const nf = world.flowers[world.flowers.length - 1]; broadcast('flower', nf, nf.room); }, 4000);
      send(c, 'honey', { honey: user.honey, level: user.level }); return json(res, 200, { ok: true });
    }
    return json(res, 400, {});
  }

  if (p === '/api/top') return json(res, 200, Object.values(db.users).sort((a, b) => b.honey - a.honey).slice(0, 10).map(u => ({ name: u.name, honey: u.honey, level: u.level })));

  // ---- مدیریت ----
  if (p === '/api/admin/login' && req.method === 'POST') {
    const b = await body(req); if (sign(String(b.password)) !== sign(ADMIN_PASSWORD)) return json(res, 401, { error: 'رمز اشتباه است' });
    const t = rid() + rid(); adminTokens.add(t); return json(res, 200, { token: t });
  }
  if (p.startsWith('/api/admin/')) {
    if (!isAdmin(req)) return json(res, 401, { error: 'unauthorized' });
    const b = req.method === 'POST' ? await body(req) : {};
    if (p === '/api/admin/stats') return json(res, 200, { online: clients.size, users: Object.keys(db.users).length, messages: db.messages.length, banned: Object.keys(db.banned).length, settings: db.settings,
      players: [...clients.values()].map(c => ({ ...pub(c), token: c.token })), banList: Object.entries(db.banned).map(([t, r]) => ({ token: t, name: (db.users[t] || {}).name, reason: r })), recent: db.messages.slice(-100).reverse() });
    if (p === '/api/admin/settings') { const s = db.settings, n = b || {};
      if (n.gameName !== undefined) s.gameName = clean(n.gameName, 30); if (n.creator !== undefined) s.creator = clean(n.creator, 40);
      if (n.welcome !== undefined) s.welcome = clean(n.welcome, 200); if (n.chatEnabled !== undefined) s.chatEnabled = !!n.chatEnabled;
      if (Array.isArray(n.bannedWords)) s.bannedWords = n.bannedWords.map(w => clean(w, 30)).filter(Boolean);
      if (Array.isArray(n.rooms) && n.rooms.length) s.rooms = n.rooms.map(r => clean(r, 20)).filter(Boolean);
      if (n.announcement !== undefined) { s.announcement = clean(n.announcement, 300); broadcast('announce', { text: s.announcement }); }
      save(); return json(res, 200, { ok: true }); }
    const target = () => [...clients.values()].find(c => c.token === b.token);
    if (p === '/api/admin/kick') { const c = target(); if (c) { send(c, 'kicked', { reason: clean(b.reason, 100) || 'توسط مدیر خارج شدید' }); try { c.res.end(); } catch {} } return json(res, 200, { ok: true }); }
    if (p === '/api/admin/ban') { db.banned[b.token] = clean(b.reason, 100) || 'رعایت نکردن قوانین'; const c = target(); if (c) { send(c, 'kicked', { reason: 'مسدود شدید: ' + db.banned[b.token] }); try { c.res.end(); } catch {} } save(); return json(res, 200, { ok: true }); }
    if (p === '/api/admin/unban') { delete db.banned[b.token]; save(); return json(res, 200, { ok: true }); }
    if (p === '/api/admin/mute') { db.muted[b.token] = Date.now() + (+b.minutes || 10) * 60000; save(); return json(res, 200, { ok: true }); }
    if (p === '/api/admin/deleteMessage') { db.messages = db.messages.filter(m => m.id !== b.id); save(); broadcast('delMsg', { id: b.id }); return json(res, 200, { ok: true }); }
    if (p === '/api/admin/broadcast') { broadcast('announce', { text: clean(b.text, 300) }); return json(res, 200, { ok: true }); }
    return json(res, 404, {});
  }

  // فایل‌های ثابت
  let f = p === '/' ? '/index.html' : p === '/admin' ? '/admin.html' : p;
  const fp = path.join(__dirname, 'public', path.normalize(f).replace(/^(\.\.[\/\\])+/, ''));
  if (!fp.startsWith(path.join(__dirname, 'public'))) { res.writeHead(403); return res.end(); }
  fs.readFile(fp, (e, d) => { if (e) { res.writeHead(404); return res.end('404'); } res.writeHead(200, { 'Content-Type': MIME[path.extname(fp)] || 'application/octet-stream' }); res.end(d); });
}).listen(PORT, () => console.log('Asal Abad on :' + PORT));
