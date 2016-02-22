package com.github.javalabs.androidlab4.activity;

import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.javalabs.androidlab4.R;

public class TemplateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);
    }

    public void onClickBtn(View v) {
        switch(v.getId()) {
            case R.id.buttonAddTemplate: {
                Toast.makeText(this, "Добавить шаблон", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        EditText e = (EditText)findViewById(R.id.editTextTextTemplate);

        Intent i = new Intent();
        i.putExtra("Test", e.getText().toString());
        setResult(RESULT_OK, i);
        Toast.makeText(this, "onBack", Toast.LENGTH_SHORT).show();
        finishActivity(SmsActivity.TEMPLATE_RESULT_CODE);
        super.onBackPressed();
    }
}
