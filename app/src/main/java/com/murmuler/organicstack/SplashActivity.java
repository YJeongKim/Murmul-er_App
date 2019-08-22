package com.murmuler.organicstack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;


import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.imageView)
    ImageView imageView;
    private final Handler handler = new Handler();
//    RequestManager glideRequestManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

//        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
//        Glide.with(this).load(R.raw.house).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageViewTarget);

        Glide.with(this).asGif().load(R.raw.house).into(imageView);
//        startLoading();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);
    }

    private void startLoading() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 3000);
    }
}