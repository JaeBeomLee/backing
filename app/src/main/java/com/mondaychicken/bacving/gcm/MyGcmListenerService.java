package com.mondaychicken.bacving.gcm;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import com.mondaychicken.bacving.R;
import com.mondaychicken.bacving.main.MainPage;
import com.mondaychicken.bacving.main.alertList.alertData;
import com.mondaychicken.bacving.pushManager;


public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    //false가
    boolean isAlertChecked;
    int count = 0;

    /**
     *
     * @param from SenderID 값을 받아온다.
     * @param data Set형태로 GCM으로 받은 데이터 payload이다.
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        final pushManager manager = new pushManager(this,"push", null, 1);
        String title = data.getString("title");
        String message = data.getString("message");
        String type = data.getString("type");
        String link = data.getString("link");
        manager.insert("insert into push values(null, '" + title + "', '" + message + "', 'null')");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Title: " + title);
        Log.d(TAG, "Message: " + message);

        // GCM으로 받은 메세지를 디바이스에 알려주는 sendNotification()을 호출한다.
        sendNotification(title, message);
    }


    /**
     * 실제 디바에스에 GCM으로부터 받은 메세지를 알려주는 함수이다. 디바이스 Notification Center에 나타난다.
     * @param title
     * @param message
     */

    private void sendNotification(String title, String message) {
        LoadPreference(this);
//        realm = Realm.getDefaultInstance();
//        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);
//                .setSound(defaultSoundUri)
                NotificationCompat.InboxStyle inboxStyle =
                        new NotificationCompat.InboxStyle();
        String[] events = new String[6];
        inboxStyle.setBigContentTitle("Event tracker details : ");
        for (int i = 0; i<events.length; i++){
            inboxStyle.addLine(events[i]);
        }
        notificationBuilder.setStyle(inboxStyle);

        Intent intent = new Intent(this, MainPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("alert", true);


//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//        stackBuilder.addParentStack(MainPage.class);
//        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (isAlertChecked){
            notificationManager.notify(count /* ID of notification */, notificationBuilder.build());
            count++;
        }
    }

    private void LoadPreference(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        isAlertChecked = sharedPreferences.getBoolean("app_setting_alert",true);
    }

    public void SavePreference(String key, String value, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
}