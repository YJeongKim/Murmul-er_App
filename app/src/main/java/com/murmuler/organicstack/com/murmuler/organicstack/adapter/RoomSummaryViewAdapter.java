package com.murmuler.organicstack.com.murmuler.organicstack.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.murmuler.organicstack.MainViewActivity;
import com.murmuler.organicstack.R;
import com.murmuler.organicstack.com.murmuler.organicstack.vo.RoomSummaryViewVO;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


public class RoomSummaryViewAdapter extends ArrayAdapter<RoomSummaryViewVO> {
    private final Context context;
    private final List<RoomSummaryViewVO> roomList;

    Bitmap bitmap = null;

    public RoomSummaryViewAdapter(Context context, List<RoomSummaryViewVO> roomList) {
        super(context, -1, roomList);
        this.context = context;
        this.roomList = roomList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.room_detail_list_item, parent, false);

        RoomSummaryViewVO room = roomList.get(position);
        ImageView imageView = view.findViewById(R.id.imageView);

        Thread mThread = new Thread() {
            @Override
            public void run() {
                InputStream is = null;
                try {
                    URL url = new URL("http://www.murmul-er.com/manage/download?middlePath=/room/roomId_" + room.getRoomId() + "&imageFileName=" + room.getRoomImg());
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        mThread.start();

        try {
            mThread.join();
            imageView.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TextView title = view.findViewById(R.id.title);
        String titleText = room.getTitle();
        if(titleText.length() >= 14) titleText = titleText.substring(0,14) + "...";
        title.setText(titleText);

        ImageButton likeButton = view.findViewById(R.id.likeButton);
        likeButton.setBackgroundColor(Color.WHITE);
        likeButton.setImageResource(R.drawable.heart_custom);

        TextView address = view.findViewById(R.id.address);
        String addressText = room.getSido() + " " + room.getSigungu() + " " + room.getRoadname();
        address.setText(addressText);

        TextView detail = view.findViewById(R.id.detail);
        String cost = room.getMonthlyCost() == 0 ? "전세" : "월세 " + room.getMonthlyCost();
        String manageCost = room.getManageCost() == 0 ? "없음" : "" + room.getManageCost();
        detail.setText("[" + room.getRentType() + "] "
                     + "보증금 " + room.getDeposit() + " / " + cost + "\n"
                     + room.getRoomType() + " " + room.getArea() + " 관리비 " + manageCost
        );
        view.setMinimumHeight(450);

        return view;
    }
}
