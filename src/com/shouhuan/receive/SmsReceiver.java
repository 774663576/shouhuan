package com.shouhuan.receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.ble.ble_fastcode.bluetooth.WristBandDevice;

/**
 * ��Ϣ / ���ŵļ����㲥 �����ֻ����ڼ������⣬ϵͳ���Ź㲥������
 * 
 * @author
 * @created
 */
public class SmsReceiver extends BroadcastReceiver {
	public static final boolean D = true;
	// Tag
	private String TAG = this.getClass().getSimpleName();

	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String SMS_RECEIVED_NEW = "android.provider.Telephony.SMS_DELIVER";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (D)
			Log.e(TAG, "+++ ON RECEIVE +++");
		// ����
		if (intent.getAction().equals(SMS_RECEIVED)
				|| intent.getAction().equals(SMS_RECEIVED_NEW)) {
			Sms sms = getSms(context, intent);
			if (sms == null) {
				return;
			}

			String number = sms.getContact().getDisplayName();
			if (number.length() != 0 && number.startsWith("+86")) {
				number = number.substring(3, number.length());
			}
			boolean alleng = true;
			for (int j = 0; j < number.length(); j++) {
				if (number.charAt(j) < 0x80) {
					continue;
				} else {
					alleng = false;
				}
			}

			if (WristBandDevice.getInstance(context).isConnected()) {
				if (alleng) {
					if (number.length() >= 11) {
						/**
						 * ��������Ϣ���͵��ֻ�
						 */
						WristBandDevice.getInstance(context)
								.writeWristBandSmsAlertNew(context,
										number.substring(0, 11));
					} else {
						WristBandDevice.getInstance(context)
								.writeWristBandSmsAlertNew(context,
										number.substring(0, number.length()));
					}
				} else {
					if (number.length() >= 6) {
						WristBandDevice.getInstance(context)
								.writeWristBandSmsAlertNew(context,
										number.substring(0, 6));
					} else {
						WristBandDevice.getInstance(context)
								.writeWristBandSmsAlertNew(context,
										number.substring(0, number.length()));
					}
				}
			}
			return;
		}
	}

	public static Sms getSms(Context context, Intent intent) {
		StringBuilder number = new StringBuilder("");// ���ŷ�����
		StringBuilder body = new StringBuilder(""); // ��������

		Bundle bundle = intent.getExtras();
		// Bundle����Ϊ���жϲ���
		if (bundle == null)
			return null;
		// ��ȡ���ŵ����ݺͷ�����
		Object[] _pdus = (Object[]) bundle.get("pdus");
		SmsMessage[] message = new SmsMessage[_pdus.length];

		for (int i = 0; i < _pdus.length; i++) {
			message[i] = SmsMessage.createFromPdu((byte[]) _pdus[i]);
		}
		// ��ȡ��Ϣ
		for (SmsMessage currentMessage : message) {
			// ��ȡ����
			if (!number.toString().equals(
					currentMessage.getDisplayOriginatingAddress())) {
				number.append(currentMessage.getDisplayOriginatingAddress());
			}
			body.append(currentMessage.getDisplayMessageBody());
		}

		return new Sms(number.toString(), body.toString(),
				SmsReceiver.getContact(context, number.toString()));
	}

	public static class Sms {
		public Sms() {
		}

		public Sms(String number, String body, Contact contact) {
			this.number = number;
			this.body = body;
			this.contact = contact;
		}

		private String number;
		private String body;
		private Contact contact;

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getBody() {
			return body;
		}

		public void setBody(String body) {
			this.body = body;
		}

		public Contact getContact() {
			return contact;
		}

		public void setContact(Contact contact) {
			this.contact = contact;
		}

	}

	public static Contact getContact(Context context, String phoneNumber) {
		Contact contact = new Contact(phoneNumber);
		if (TextUtils.isEmpty(phoneNumber)) {
			contact.setDisplayName("Unknown Number");
		}
		Cursor cursor = null;
		try {
			// ���Һ��룬������
			// �鵽��ʾ������û�в鵽��ʾ����
			Uri uri = Uri.withAppendedPath(
					ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
					Uri.encode(phoneNumber));
			cursor = context.getContentResolver().query(
					uri,
					new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME,
							ContactsContract.PhoneLookup.TYPE,
							ContactsContract.PhoneLookup.LABEL }, null, null,
					ContactsContract.PhoneLookup.DISPLAY_NAME + " LIMIT 1");
			while (cursor.moveToNext()) {
				contact.setDisplayName(cursor.getString(cursor
						.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)));
				break;
			}
		} catch (Exception e) {
			contact.setDisplayName(phoneNumber);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return contact;
	}

	public static class Contact {
		private String number;
		private String displayName;

		public Contact(String phoneNumber) {
			this.number = phoneNumber;
			this.displayName = phoneNumber;
		}

		public String getNumber() {
			return number;
		}

		public void setNumber(String number) {
			this.number = number;
		}

		public String getDisplayName() {
			return displayName;
		}

		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
	}
}