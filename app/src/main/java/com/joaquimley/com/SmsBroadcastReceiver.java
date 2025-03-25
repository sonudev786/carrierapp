package com.joaquimley.com;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

/**
 * A broadcast receiver who listens for incoming SMS
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

	private static SmsListener smsListener;

	public static void setSmsListener(SmsListener listener) {
		smsListener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				if (pdus != null) {
					for (Object pdu : pdus) {
						SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
						String sender = smsMessage.getDisplayOriginatingAddress();
						String messageBody = smsMessage.getMessageBody();
						String date = String.valueOf(smsMessage.getTimestampMillis());

						if (smsListener != null) {
							smsListener.onNewSmsReceived(new SmsModel(sender, messageBody, date));
						}
					}
				}
			}
		}
	}

	public interface SmsListener {
		void onNewSmsReceived(SmsModel sms);
	}
}
