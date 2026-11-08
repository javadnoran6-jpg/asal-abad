package ir.asalabad.game;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView web;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        web = new WebView(this);
        setContentView(web);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView v, int code, String desc, String url) {
                v.loadData("<html dir='rtl'><body style='font-family:sans-serif;text-align:center;padding-top:30vh'>"
                        + "<h2>اتصال به سرور برقرار نشد</h2><p>اینترنت را بررسی کنید و دوباره وارد شوید.</p></body></html>",
                        "text/html; charset=utf-8", "UTF-8");
            }
        });
        web.setWebChromeClient(new WebChromeClient());
        web.loadUrl(getString(R.string.server_url));
    }

    @Override
    public void onBackPressed() {
        if (web.canGoBack()) web.goBack(); else super.onBackPressed();
    }
}
