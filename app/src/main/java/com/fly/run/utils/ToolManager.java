package com.fly.run.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Camera;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

/**
 * Created by xinzhendi-031 on 2016/11/21.
 */
public class ToolManager {

    public static final int REQUEST_CONTACT = 1;
    public static final int REQUEST_CAMERA_OK = 2;
    public static final int REQUEST_ALBUM = 3;
    public static final int REQUEST_RECORD_VOICE = 4;
    public static final int REQUEST_RECORD_VIDEO = 5;

    public static final String localTempImgFileName = "car.jpg";

    public static void getContactNum(Context context, Intent data) {
        if (data == null) {
            return;
        }
        String phoneNumber = null;
        Uri contactData = data.getData();
        if (contactData == null) {
            return;
        }
        Cursor cursor = ((Activity) context).managedQuery(contactData, null, null, null, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            if (hasPhone.equalsIgnoreCase("1")) {
                hasPhone = "true";
            } else {
                hasPhone = "false";
            }
            if (Boolean.parseBoolean(hasPhone)) {
                Cursor phones = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = " + id, null, null);
                while (phones.moveToNext()) {
                    phoneNumber = phones
                            .getString(phones
                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                phones.close();
                if (!TextUtils.isEmpty(phoneNumber))
                    callPhone(context, phoneNumber);
            }
        }
//        cursor.close();
    }

    @SuppressWarnings("MissingPermission")
    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNum));
        context.startActivity(intent);
    }

    public static Camera camera = null;

    public static void flashLightOffOn() {
        if (camera == null)
            camera = Camera.open();
        Camera.Parameters param = camera.getParameters();
        if (!checkFlashLightOpen(param)) {
            param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        } else {
            param.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }
        camera.setParameters(param);
    }

    public static boolean checkFlashLightOpen(Camera.Parameters param) {
        if (camera == null)
            camera = Camera.open();
        if (param == null)
            param = camera.getParameters();
        if (param.getFlashMode().equalsIgnoreCase(Camera.Parameters.FLASH_MODE_TORCH))
            return true;
        return false;
    }

    public static void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
