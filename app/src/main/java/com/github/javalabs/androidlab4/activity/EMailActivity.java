package com.github.javalabs.androidlab4.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.github.javalabs.androidlab4.R;

public class EMailActivity extends AppCompatActivity {
    public static final int REQUEST_SELECT_CONTACT = 0x0001;
    public static final int REQUEST_SELECT_IMAGE = 0x0002;
    public static final int REQUEST_SEND_MAIL = 0x0003;

    private String attachment;
    private Boolean isMediaChoose = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

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
            case R.id.buttonChooseImg: {
                //Toast.makeText(this, "Выбрать картинку", Toast.LENGTH_SHORT).show();
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (pickPhoto.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(pickPhoto, REQUEST_SELECT_IMAGE);
                }

                break;
            }
            case R.id.buttonSendEmail: {
                //Toast.makeText(this, "Отправка Email", Toast.LENGTH_SHORT).show();
                if(!isMediaChoose)
                    attachment = String.format("android.resource://%s/%d",
                        // "com.github.javalabs.androidlab4",
                        this.getPackageName(),
                        R.mipmap.my_img);

                EditText editAdress = (EditText)findViewById(R.id.editTextEmailAdress);
                EditText editSubject= (EditText)findViewById(R.id.editTextSubject);
                EditText editMessage= (EditText)findViewById(R.id.editTextMessage);

                String adress = editAdress.getText().toString();
                String message = editMessage.getText().toString();
                String subject = editSubject.getText().toString();

                Intent sendMail = new Intent(Intent.ACTION_SEND);
                sendMail.setType("*/*");
                //sendMail.setData(Uri.parse("mailto:" + adress));
                sendMail.putExtra(Intent.EXTRA_EMAIL, new String[] { adress });
                sendMail.putExtra(Intent.EXTRA_SUBJECT, subject);
                sendMail.putExtra(Intent.EXTRA_TEXT, message);
                sendMail.putExtra(Intent.EXTRA_STREAM, Uri.parse(attachment));
                Log.d("LAB", adress);
                Log.d("LAB", subject);
                Log.d("LAB", Uri.parse(attachment).toString());
                if (sendMail.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendMail);
                }
                break;
            }
            case R.id.buttonSendSms: {
                //Toast.makeText(this, "Отправить смс", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SmsActivity.class);
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
            case REQUEST_SELECT_CONTACT: {
                Uri contactUri = data.getData();
                EditText email = (EditText)findViewById(R.id.editTextEmailAdress);
                email.setText(retrieveContactEmail(contactUri));

            }
            case REQUEST_SELECT_IMAGE: {
                String d = data.getData().toString();
                //new AlertDialog.Builder(this).setMessage(d).show();
                isMediaChoose = true;
                attachment = d;
                break;
            }
            case REQUEST_SEND_MAIL: {
                new AlertDialog.Builder(this).setMessage(data.getStringExtra("REQUEST_SEND_MAIL")).show();
                break;
            }

        }
    }

    private String retrieveContactEmail(Uri uriContact) {
        String contactEmail = null;
        String contactID = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();


        // Using the contact ID now we will get contact email
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS},

                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",// AND " +
                        //ContactsContract.CommonDataKinds.Email.TYPE + " = " +
                        //ContactsContract.CommonDataKinds.Email.TYPE_HOME,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactEmail = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
        }

        cursorPhone.close();
        if(contactEmail == null)
            contactEmail = "";
        return contactEmail;
    }
}
