package com.maxym.weatherforecast.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.maxym.weatherforecast.R;

public class CityActivity extends AppCompatActivity{

    EditText etCity;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        initViews();
    }

    private void initViews() {
        etCity = findViewById(R.id.editCity);
        btnConfirm = findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("city", etCity.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}