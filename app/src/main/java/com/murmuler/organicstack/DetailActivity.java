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

public class DetailActivity extends AppCompatActivity {
    private static final String ROOT_CONTEXT = Constants.ROOT_CONTEXT;
    private final Handler handler = new Handler();
//    private String memberId;
//    private String nickname;
    private String roomId;

    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
//        memberId = intent.getExtras().getString("memberId");
//        nickname = intent.getExtras().getString("nickname");
        roomId = intent.getExtras().getString("roomId");

        settingWeb();
        webView.loadUrl(ROOT_CONTEXT+"/mobile/searchRoom/"+roomId+"?flag=true");
    }

    private void settingWeb() {
        WebSettings webSettings = webView.getSettings();
        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new WebChromeClient());
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new AndroidBridge(), "detail");
    }

    private class AndroidBridge {
        @JavascriptInterface
        public void back() {
            handler.post(() -> {
                Log.d("상세정보", "상세에서 뒤로가기 요청");
                finish();
            });
        }
    }
}
