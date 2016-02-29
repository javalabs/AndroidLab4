package com.github.javalabs.androidlab4.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.javalabs.androidlab4.R;
import com.github.javalabs.androidlab4.db.DatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;

public class TemplateActivity extends AppCompatActivity {

    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template);

        mDatabaseHelper = new DatabaseHelper(this, "lab4.db", null, 1);

        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        final ListView lv = (ListView) findViewById(R.id.listViewSmsTemplate);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TEMPLATE", "click on list item");
                String m = (String)lv.getItemAtPosition(position);
                Toast.makeText(TemplateActivity.this, m, Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                i.putExtra(SmsActivity.TEMPLATE_MESSAGE, m);
                setResult(RESULT_OK, i);
                finish();
                /*
                finishActivity(SmsActivity.REQUEST_TEMPLATE_ACTIVITY);
                onBackPressed();*/
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("TEMPLATE", "click long on list item");
                final String m = (String)lv.getItemAtPosition(position);
                Toast.makeText(TemplateActivity.this, m, Toast.LENGTH_SHORT).show();
                // DELETE FROM DATABASE !!!
                adapter.remove(m);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mSqLiteDatabase.delete(DatabaseHelper.DATABASE_TABLE_TEMPLATE, DatabaseHelper.MESSAGE_COLUMN + " = ?", new String[]{m});

                    }
                });
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, new ArrayList<String>(20));
        lv.setAdapter(adapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<String> strings = getMessagesFromDB();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //for(String s : strings)
                         //   adapter.add(s);
                        adapter.addAll(strings);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    public ArrayList<String> getMessagesFromDB() {
        ArrayList<String> messages = new ArrayList<>(20);
        Cursor cursor = mSqLiteDatabase.query(mDatabaseHelper.DATABASE_TABLE_TEMPLATE, new String[] {
                        DatabaseHelper.MESSAGE_COLUMN},
                null, null,
                null, null, null);

        if (cursor.moveToFirst()){
            String message = null;
            while(!cursor.isAfterLast()){
                message = cursor.getString(cursor.getColumnIndex(mDatabaseHelper.MESSAGE_COLUMN));
                if(message != null) {
                    messages.add(message);
                }
                cursor.moveToNext();
            }
        }

        cursor.close();
        //return messages.toArray(new String[0]);
        return messages;
    }
    public void onClickBtn(View v) {
        switch(v.getId()) {
            case R.id.buttonAddTemplate: {
                //Toast.makeText(this, "Добавить шаблон", Toast.LENGTH_SHORT).show();
                EditText et = (EditText)findViewById(R.id.editTextTextTemplate);
                String message = et.getText().toString();
                final ContentValues newValues = new ContentValues();
                newValues.put(DatabaseHelper.MESSAGE_COLUMN, message);
                // Вставляем данные в таблицу и в адаптер
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mSqLiteDatabase.insert(DatabaseHelper.DATABASE_TABLE_TEMPLATE, null, newValues);
                    }
                }).start();

                adapter.add(message);

                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        /*
        EditText e = (EditText)findViewById(R.id.editTextTextTemplate);

        Intent i = new Intent();
        i.putExtra("Test", e.getText().toString());
        setResult(RESULT_OK, i);
        Toast.makeText(this, "onBack", Toast.LENGTH_SHORT).show();
        finishActivity(SmsActivity.REQUEST_TEMPLATE_ACTIVITY);
        */
        super.onBackPressed();
    }
}
