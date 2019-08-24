package com.murmuler.organicstack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.murmuler.organicstack.util.Constants;
import com.murmuler.organicstack.util.MyWebClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.loginView)
    WebView loginView;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startActivity(new Intent(LoginActivity.this, SplashActivity.class));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setContentView(R.layout.activity_login);
                ButterKnife.bind(LoginActivity.this);
                settingWeb();
            }
        }, 1);

    }

    private void settingWeb() {
        WebSettings webSettings = loginView.getSettings();
        loginView.setWebViewClient(new MyWebClient());
        loginView.setWebChromeClient(new WebChromeClient());
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        loginView.addJavascriptInterface(new AndroidBridge(), "login");
        loginView.loadUrl(Constants.ROOT_CONTEXT +"/mobile");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setLogin(final String nickname, String memberId) {
            handler.post(new Runnable(){
                public void run(){
                    Log.d("로그인 성공", "login success : "+nickname);
                    Intent intent = new Intent(LoginActivity.this, MainViewActivity.class);
                    intent.putExtra("nickname", nickname);
                    intent.putExtra("memberId", memberId);
                    startActivity(intent);
                }
            });
        }
    }
}
