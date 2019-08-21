package com.murmuler.organicstack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editSearch;
    ImageButton btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editSearch = findViewById(R.id.editText);
        btnSearch = findViewById(R.id.btnSearch);
    }

    public void onClick(View view) {
        String keyword = editSearch.getText().toString();
        Toast.makeText(view.getContext(), keyword, Toast.LENGTH_LONG).show();
    }
}
