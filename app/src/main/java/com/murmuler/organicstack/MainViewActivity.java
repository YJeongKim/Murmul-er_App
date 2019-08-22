package com.murmuler.organicstack;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainViewActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
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

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    private String memberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);

        ButterKnife.bind(this);
        Glide.with(this).load(R.drawable.bottom_main_on).into(botMain);
        Glide.with(this).load(R.drawable.bottom_search).into(botSearch);
        Glide.with(this).load(R.drawable.bottom_like).into(botLike);
        Glide.with(this).load(R.drawable.bottom_more).into(botMore);

        webView.setWebViewClient(new MyWebClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("http://192.168.30.242:8089/mobile/main");

        Intent intent = getIntent();
        memberId = intent.getExtras().getString("memberId");
        String nickname = intent.getExtras().getString("nickname");

        View headerView = navigationView.getHeaderView(0);
        TextView userName = headerView.findViewById(R.id.loginId);
        userName.setText(nickname);

        navigationView.setNavigationItemSelectedListener(this);

    }

    class MyWebClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @OnClick(R.id.botMain)
    public void clickMain(View v) {
        Glide.with(this).load(R.drawable.bottom_main_on).into(botMain);
        Glide.with(this).load(R.drawable.bottom_search).into(botSearch);
        Glide.with(this).load(R.drawable.bottom_like).into(botLike);
        Glide.with(this).load(R.drawable.bottom_more).into(botMore);
        webView.loadUrl("http://192.168.30.242:8089/mobile/main");
    }
    @OnClick(R.id.botSearch)
    public void clickSearch(View v) {
        Glide.with(this).load(R.drawable.bottom_main).into(botMain);
        Glide.with(this).load(R.drawable.bottom_search_on).into(botSearch);
        Glide.with(this).load(R.drawable.bottom_like).into(botLike);
        Glide.with(this).load(R.drawable.bottom_more).into(botMore);
    }
    @OnClick(R.id.botLike)
    public void clickLike(View v) {
        Glide.with(this).load(R.drawable.bottom_main).into(botMain);
        Glide.with(this).load(R.drawable.bottom_search).into(botSearch);
        Glide.with(this).load(R.drawable.bottom_like_on).into(botLike);
        Glide.with(this).load(R.drawable.bottom_more).into(botMore);
        webView.loadUrl("http://192.168.30.242:8089/mobile/like/"+memberId);
    }
    @OnClick(R.id.botMore)
    public void clickMore(View v) {
        drawerLayout.openDrawer(navigationView);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        System.out.println("selected " + id);

        switch (id) {
            case R.id.nav_search :
                Glide.with(this).load(R.drawable.bottom_main).into(botMain);
                Glide.with(this).load(R.drawable.bottom_search_on).into(botSearch);
                Glide.with(this).load(R.drawable.bottom_like).into(botLike);
                Glide.with(this).load(R.drawable.bottom_more).into(botMore);
                break;
            case R.id.nav_like :
                Glide.with(this).load(R.drawable.bottom_main).into(botMain);
                Glide.with(this).load(R.drawable.bottom_search).into(botSearch);
                Glide.with(this).load(R.drawable.bottom_like_on).into(botLike);
                Glide.with(this).load(R.drawable.bottom_more).into(botMore);
                webView.loadUrl("http://192.168.30.242:8089/mobile/like/"+memberId);
                break;
        }

        drawerLayout.closeDrawer(navigationView);
        return true;
    }


}
