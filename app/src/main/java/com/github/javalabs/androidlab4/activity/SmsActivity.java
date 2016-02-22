package com.github.javalabs.androidlab4.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.javalabs.androidlab4.R;

public class SmsActivity extends AppCompatActivity {
    public static final int TEMPLATE_RESULT_CODE = 0x0001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
    }

    public void onClickBtn(View v) {
        switch(v.getId()) {
            case R.id.buttonPhoneBook: {
                Toast.makeText(this, "Тел книга", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.buttonSaveNum: {
                Toast.makeText(this, "Сохранить номер", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.buttonSendSms: {
                Toast.makeText(this, "Отправка СМС", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.buttonTop10: {
                Toast.makeText(this, "Последние 10 смс", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.buttonTemplate: {
                Toast.makeText(this, "Шаблоны смс", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, TemplateActivity.class);
                startActivityForResult(intent, TEMPLATE_RESULT_CODE);
                break;
            }
            case R.id.buttonEmail: {
                Toast.makeText(this, "Отправка Email", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, EMailActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "onActivityResult", Toast.LENGTH_SHORT).show();
        switch (requestCode) {
            case TEMPLATE_RESULT_CODE: {
                if(resultCode == RESULT_OK) {
                    new AlertDialog.Builder(this).setMessage(data.getStringExtra("Test")).show();
                }

                break;
            }

        }
    }
}
