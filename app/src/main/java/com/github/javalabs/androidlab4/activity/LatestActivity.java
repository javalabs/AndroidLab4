package com.github.javalabs.androidlab4.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.github.javalabs.androidlab4.R;
import com.github.javalabs.androidlab4.db.DatabaseHelper;
import com.github.javalabs.androidlab4.db.MessageTop;

import java.util.ArrayList;
import java.util.List;

public class LatestActivity extends AppCompatActivity {

    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    private SmsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest);

        mDatabaseHelper = new DatabaseHelper(this, "lab4.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        ListView lv = (ListView)findViewById(R.id.latestListView);

        adapter = new SmsAdapter(this, null);

        lv.setAdapter(adapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<MessageTop> messages = getLatestFromDB();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setList(messages);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    public ArrayList<MessageTop> getLatestFromDB() {
        ArrayList<MessageTop> messages = new ArrayList<>(20);

        Cursor cursor = mSqLiteDatabase.query(mDatabaseHelper.DATABASE_TABLE_TOP, new String[] {
                        BaseColumns._ID, DatabaseHelper.PHONE_COLUMN, DatabaseHelper.TEXT_COLUMN},
                null, null,
                null, null, null);
        long delId = -1;
        if (cursor.moveToLast()){
            MessageTop message = null;
            int count = 0;
            while(!cursor.isBeforeFirst()){

                if(count < 10) {
                    message = new MessageTop("", "");
                    message.setNumber(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.PHONE_COLUMN)));
                    message.setText(cursor.getString(cursor.getColumnIndex(mDatabaseHelper.TEXT_COLUMN)));
                    messages.add(message);
                }
                else {
                    delId = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
                }

                cursor.moveToPrevious();
                count++;

            }
        }

        cursor.close();

        if(delId != -1) {
            mSqLiteDatabase.delete(mDatabaseHelper.DATABASE_TABLE_TOP, BaseColumns._ID + " = ?", new String[]{String.valueOf(delId)});
        }



        return messages;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static class SmsAdapter extends ArrayAdapter<MessageTop> {

        private AppCompatActivity context;
        private List<MessageTop> messages;

        public List<MessageTop> getList() {
            return messages;
        }

        public void setList(List<MessageTop> messages) {
            this.messages = messages;
            addAll(messages);
        }

        public SmsAdapter(AppCompatActivity context, List<MessageTop> messages) {
            //super(context, android.R.layout.simple_list_item_2, messages);
            super(context, android.R.layout.simple_list_item_2);
            this.context = context;
            this.messages = messages;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View rowView = convertView;

            if(rowView == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                rowView = inflater.inflate(android.R.layout.simple_list_item_2, null);
            }

            ((TextView)rowView.findViewById(android.R.id.text1)).setText(messages.get(position).getNumber());
            ((TextView)rowView.findViewById(android.R.id.text2)).setText(messages.get(position).getText());

            return rowView;
        }
    }
}
