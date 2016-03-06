package com.mondaychicken.bacving.Login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ijaebeom on 15. 7. 27..
 */
public class sms_receiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            StringBuilder sms = new StringBuilder();
            Bundle bundle = intent.getExtras();

            if (bundle != null){
                Object[] pdusObj = (Object[]) bundle.get("pdus");

                SmsMessage[] messages = new SmsMessage[pdusObj.length];
                for (int i = 0; i<pdusObj.length; i++){
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                }

                for (SmsMessage smsMessage : messages){
                    sms.append(smsMessage.getMessageBody());
                }

                String smsbody = sms.toString();
                Pattern pattern = Pattern.compile("\\d{4}");

                Matcher matcher = pattern.matcher(smsbody);
                String authNumber = null;

                if (matcher.find()){
                    authNumber = matcher.group(0);
                }

                if (authNumber !=null){
                    Certificate.inputAuthNumber(authNumber);
                }
            }
        }
    }
}
