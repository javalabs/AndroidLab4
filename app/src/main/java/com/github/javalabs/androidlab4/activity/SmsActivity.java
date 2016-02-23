package com.github.javalabs.androidlab4.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.javalabs.androidlab4.R;

public class SmsActivity extends AppCompatActivity {
    public static final int REQUEST_TEMPLATE_ACTIVITY = 0x0001;
    public static final int REQUEST_SELECT_CONTACT = 0x0002;
    public static final int REQUEST_SAVE_CONTACT = 0x0003;
    public static final int REQUEST_TOP_ACTIVITY = 0x0004;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
    }

    public void onClickBtn(View v) {
        switch(v.getId()) {
            case R.id.buttonPhoneBook: {
                //Toast.makeText(this, "Тел книга", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_SELECT_CONTACT);
                }

                break;
            }
            case R.id.buttonSaveNum: {
                //Toast.makeText(this, "Сохранить номер", Toast.LENGTH_SHORT).show();
                EditText phone = (EditText)findViewById(R.id.editTextPhoneNum);
                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone.getText().toString());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_SAVE_CONTACT);
                }
                else {
                    Toast.makeText(this, "Error save phone number", Toast.LENGTH_SHORT).show();
                }

                break;
            }
            case R.id.buttonSendSms: {
                //Toast.makeText(this, "Отправка СМС", Toast.LENGTH_SHORT).show();
                EditText phoneNumber = (EditText)findViewById(R.id.editTextPhoneNum);
                EditText messageSms = (EditText)findViewById(R.id.editTextSmsText);
                String message = messageSms.getText().toString();
                String phone = phoneNumber.getText().toString();
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(phone, null, message, null, null);

                break;
            }
            case R.id.buttonTop10: {
                Toast.makeText(this, "Последние 10 смс", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.buttonTemplate: {
                Toast.makeText(this, "Шаблоны смс", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, TemplateActivity.class);
                startActivityForResult(intent, REQUEST_TEMPLATE_ACTIVITY);
                break;
            }
            case R.id.buttonEmail: {
                //Toast.makeText(this, "Отправка Email", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, EMailActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK)
            return;
        Toast.makeText(this, "onActivityResult", Toast.LENGTH_SHORT).show();
        switch (requestCode) {
            case REQUEST_TEMPLATE_ACTIVITY: {
                new AlertDialog.Builder(this).setMessage(data.getStringExtra("Test")).show();
                break;
            }
            case REQUEST_SELECT_CONTACT: {
                Uri contactUri = data.getData();
                //new AlertDialog.Builder(this).setMessage(contactUri.toString()).show();
                EditText phone = (EditText)findViewById(R.id.editTextPhoneNum);
                phone.setText(retrieveContactNumber(contactUri));

            }

        }
    }

    private String retrieveContactNumber(Uri uriContact) {
        // https://gist.github.com/evandrix/7058235
        String contactNumber = null;
        String contactID = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();


        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",// AND " +
                        //ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        //ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        return contactNumber;
    }
}
