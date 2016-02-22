package com.github.javalabs.androidlab4.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.javalabs.androidlab4.R;

public class EMailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
    }

    public void onClickBtn(View v) {
        switch(v.getId()) {
            case R.id.buttonPhoneBook: {
                Toast.makeText(this, "Тел книга", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.buttonChooseImg: {
                Toast.makeText(this, "Выбрать картинку", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.buttonSendEmail: {
                Toast.makeText(this, "Отправка Email", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.buttonSendSms: {
                Toast.makeText(this, "Отправить смс", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SmsActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}
