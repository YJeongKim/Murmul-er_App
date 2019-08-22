package com.murmuler.organicstack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    EditText editSearch;
    ImageButton btnSearch;
    ImageButton btnSlide;
    ListView listView;
    LinearLayout popupLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editSearch = findViewById(R.id.editText);
        btnSearch = findViewById(R.id.btnSearch);
        btnSlide = findViewById(R.id.btnSlide);
        listView = findViewById(R.id.listView);

        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[] {"방1", "방2", "방3", "방4"}));
    }

    @OnClick(R.id.btnSearch)
    public void clickSearch(View view) {
        String keyword = editSearch.getText().toString();
        Toast.makeText(view.getContext(), keyword, Toast.LENGTH_LONG).show();
    }

    public void clickSlide(View view) {
        Toast.makeText(view.getContext(), "up", Toast.LENGTH_LONG).show();

    }

    public void clickBuildingType(View view) {
        Toast.makeText(view.getContext(), "건물유형", Toast.LENGTH_LONG).show();
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.popup_building, popupLayout, true);
    }

    public void clickPeriod(View view) {
        Toast.makeText(view.getContext(), "임대기간", Toast.LENGTH_LONG).show();
    }

    public void clickDeposit(View view) {
        Toast.makeText(view.getContext(), "보증금", Toast.LENGTH_LONG).show();
    }

    public void clickMonthlyCost(View view) {
        Toast.makeText(view.getContext(), "월세", Toast.LENGTH_LONG).show();
    }
    public void clickOption(View view) {
        Toast.makeText(view.getContext(), "옵션", Toast.LENGTH_LONG).show();
    }

}
