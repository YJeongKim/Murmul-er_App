package com.murmuler.organicstack;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainViewActivity extends AppCompatActivity {
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.botMain)
    ImageButton botMain;
    @BindView(R.id.botSearch)
    ImageButton botSearch;
    @BindView(R.id.botLike)
    ImageButton botLike;
    @BindView(R.id.botMore)
    ImageButton botMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Glide.with(this).load(R.drawable.bottom_main_on).into(botMain);
        Glide.with(this).load(R.drawable.bottom_search).into(botSearch);
        Glide.with(this).load(R.drawable.bottom_like).into(botLike);
        Glide.with(this).load(R.drawable.bottom_more).into(botMore);
        webView.setWebViewClient(new MyWebClient());
        webView.loadUrl("http://www.murmul-er.com");
    }

    class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @OnClick(R.id.botSearch)
    public void clickSearch(View v) {
        Toast.makeText(this, "click search", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.botLike)
    public void clickLike(View v) {
        Toast.makeText(this, "click like", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.botMore)
    public void clickMore(View v) {
        Toast.makeText(this, "click more", Toast.LENGTH_SHORT).show();
    }
}
