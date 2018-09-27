package com.example.harsh.mysmsmanager;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private EditText etMessage;
    private EditText etTelNo;
    private String SENT = "SMS_SENT";
    private String DELIVERED = "SMS_DELIVERED";
    PendingIntent pendingIntentSent, pendingIntentDelivered;
    BroadcastReceiver broadcastReceiverSend, broadcastReceiverDelivered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etMessage = findViewById(R.id.et_message);
        etTelNo = findViewById(R.id.et_tel_no);


        pendingIntentSent = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        pendingIntentDelivered = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);


    }


    @Override
    protected void onResume() {
        super.onResume();

        broadcastReceiverSend = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "Sms sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic Failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "NO_SERVICE", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "NULL_PDU", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "RADIO_OFF", Toast.LENGTH_SHORT).show();
                        break;

                }


            }
        };

        broadcastReceiverDelivered = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "Sms delivered", Toast.LENGTH_SHORT).show();

                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "Sms not delivered", Toast.LENGTH_SHORT).show();

                        break;
                }

            }
        };

        registerReceiver(broadcastReceiverSend, new IntentFilter(SENT));
        registerReceiver(broadcastReceiverDelivered, new IntentFilter(DELIVERED));


    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiverSend);
        unregisterReceiver(broadcastReceiverDelivered);
    }

    public void onClickSend(View view) {
        String message = etMessage.getText().toString();
        String telNo = etTelNo.getText().toString();


        if (!message.isEmpty() && !telNo.isEmpty()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            } else {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(telNo, null, message, pendingIntentSent, pendingIntentDelivered);
            }
        } else {
            Toast.makeText(this, "Input Field Required", Toast.LENGTH_SHORT).show();
        }


    }
}
