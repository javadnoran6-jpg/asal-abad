# عسل آباد — ساخت APK با گیت‌هاب و Termux

## تغییرات نسخه ۱.۱ (versionCode 2)
- دیالوگ «ویرایش عکس»: پیش‌فرض خانم، پیش‌فرض آقا، گالری (۱۰ الماس کسر می‌شود؛ بدون الماس کافی انجام نمی‌شود)
- عکس واقعاً انتخاب، در حافظه برنامه ذخیره و در پروفایل/چت نمایش داده می‌شود
- دکمه‌های پروفایل واقعی شدند: بیوگرافی (ذخیره)، ذخیره حساب با Gmail، بازیابی حساب، خروج، حذف حساب

## ساخت با Termux + GitHub
```bash
pkg update && pkg install git -y
cd ~/asal-abad            # پوشه پروژه (همین زیپ باز شده)
git init && git add . && git commit -m "v1.1"
git branch -M main
git remote add origin https://github.com/USERNAME/REPO.git
git push -u origin main   # رمز = Personal Access Token
```
بعد از push: GitHub → تب **Actions** → Build Android APK → وقتی سبز شد پایین صفحه **AsalAbad-APK** را دانلود کنید.

## آپدیت روی نسخه قبلی
فایل `debug.keystore` داخل پروژه ثابت است؛ همیشه با همین کلید ساخته می‌شود، پس APK جدید روی قبلی نصب می‌شود
(برای نسخه بعدی `versionCode` را در `app/build.gradle.kts` یکی زیاد کنید).
نسخه ساخته‌شده با کلید دیگر را باید یک‌بار پاک کنید.
