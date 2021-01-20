package com.openinstitute.nuru.app;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;


import androidx.annotation.RequiresApi;

import com.openinstitute.nuru.R;

import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by oi-dev-01 on 24/09/17.
 */

public class AppFunctions extends Application {

    Context context;
    private static String result;
    private static final JSONObject formData = new JSONObject();

    static boolean isNotEmpty = true;


    public static void func_showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void func_showAlerts(Context context, CharSequence msg, String alert_type) {

        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        View toastView = toast.getView();


        if(alert_type.equals("")) {
            toastView.setBackgroundResource(R.drawable.toast_drawable);
        }
        else if(alert_type.equals("warning")) {
            toastView.setBackgroundResource(R.drawable.toast_drawable_warning);
        }
        else if(alert_type.equals("success")) {
            toastView.setBackgroundResource(R.drawable.toast_drawable_success);
        }
        toastView.setTop(600);
        toast.show();
    }


//    public static void func_showAlertsXXX(Context context, CharSequence msg, String alert_type) {
//
//        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
//        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
//
//        View toastView = toast.getView();
//        if(alert_type.equals("")) {
//            toastView.setBackgroundResource(R.drawable.toast_drawable);
//        }
//        else if(alert_type.equals("warning")) {
//            toastView.setBackgroundResource(R.drawable.toast_drawable_warning);
//        }
//        else if(alert_type.equals("success")) {
//            toastView.setBackgroundResource(R.drawable.toast_drawable_success);
//        }
//        toastView.setTop(500);
//        toast.show();
//    }


/*
    public static String[] func_apiPush(final Context context, String form_action, String form_data, RequestQueue requestQueue){

        String posi_alert = "";
        String nega_alert = "";
        final String[] func_response = {"", ""};

        //Log.d("KEY_API_ACTION_REGISTER", KEY_API_ACTION_REGISTER);

        switch (form_action) {
            case KEY_API_ACTION_REGISTER:
                posi_alert = "Account registered";
                nega_alert = "Account not registered";
                break;

            case KEY_API_ACTION_PIN_MANUAL:
            case KEY_API_ACTION_PIN_SCAN:
                posi_alert = "Pin accepted";
                nega_alert = "Pin not accepted";
                break;

            default:
                posi_alert = "Success";
                nega_alert = "Failed";
                break;
        }


        String finalPosi_alert = posi_alert;
        String finalNega_alert = nega_alert;

        AppApiPull appApiPush = new AppApiPull(form_data, form_action, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("apiPushResponse", response);
                try {
                    JSONObject jsonObject_a = new JSONObject(response);
                    JSONObject jsonObject = jsonObject_a.getJSONObject(KEY_API_RESPONSE);
                    String resp_status = jsonObject.getString(KEY_API_STATUS);

                    if (jsonObject.getBoolean(KEY_API_SUCCESS)) {
                        //Toasty.success(context, finalPosi_alert, Toast.LENGTH_LONG).show();

                        if(form_action.equals(KEY_API_ACTION_PIN_MANUAL) || form_action.equals(KEY_API_ACTION_PIN_SCAN))
                        {
                            String resp_code = jsonObject.getString("response_code");
                            String resp_message = jsonObject.getString("message");
                            int resp_item_id = jsonObject.getInt("request_id");

                            //TODO - Refresh Pins List
                            if(form_action.equals(KEY_API_ACTION_PIN_MANUAL)){
                                MainActivity caller_mainactivity;
                                caller_mainactivity = (MainActivity) context;
                                caller_mainactivity.get_pinDialog_Main(resp_message, 1);
                            }

                            if(form_action.equals(KEY_API_ACTION_PIN_SCAN)){
                                PinScanActivity caller_pin_scan_activity;
                                caller_pin_scan_activity = (PinScanActivity) context;
                                caller_pin_scan_activity.get_pinDialog_Scan(resp_message, 1);
                            }

                        }


                        if(form_action.equals(KEY_API_ACTION_PINS_FETCH)) {

                            JSONArray arrayPinsList = jsonObject.getJSONArray("pinslist");
                            MainActivity caller_mainactivity;
                            caller_mainactivity = (MainActivity) context;
                            caller_mainactivity.populatePinsList(arrayPinsList);

                        }


                        if(form_action.equals(KEY_API_ACTION_REGISTER)){
                            UserSession session = new UserSession(context);
                            session.createLoginSession(jsonObject);
                            String resp_account_id = jsonObject.getString(KEY_USER_ID);
                            String resp_account_otp = jsonObject.getString(KEY_USER_AUTH);
                            func_response[0] = resp_account_id;
                            func_response[1] = resp_account_otp;

                            Intent otpintent = new Intent(context, OTPActivity.class);
                            otpintent.putExtra(KEY_USER_ID, resp_account_id);
                            otpintent.putExtra(KEY_USER_AUTH, resp_account_otp);
                            context.startActivity(otpintent);

                            ((Activity) context).finish();
                        }


                    } else {

                        Toasty.error(context, finalNega_alert, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, error -> {

            if (error instanceof ServerError){
                Toasty.error(context, "Server Error", Toast.LENGTH_SHORT).show(); }
            else if (error instanceof TimeoutError) {
                Toasty.warning(context, "Connection Timed Out", Toast.LENGTH_SHORT).show(); }
            else if (error instanceof NetworkError){
                Toasty.warning(context, "Bad Network Connection", Toast.LENGTH_SHORT).show(); }
        });

        requestQueue.add(appApiPush);
        requestQueue.getCache().clear();

        return func_response;
    }
*/


    public static boolean isInternetConnected(Context context) {
        boolean flag = false;
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
            flag = true;
        }
        return flag;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String func_shaOne(String pass){

        String value = pass + "R2d2";
        String sha1 = "";

        // With the java libraries
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            digest.update(value.getBytes(StandardCharsets.UTF_8));
            sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
        } catch (Exception e){
            e.printStackTrace();
        }

        return sha1;

    }


    public static String func_getUserCoded(String user_email) {

        String[] email_pieces = (String.valueOf(user_email)).split("@");
        String piece_name = email_pieces[0];
        String piece_dom = email_pieces[1];

        String[] dom_pieces = (piece_dom).split("\\.");

        String name_nira = piece_name + "_" + dom_pieces[0];
        //String out = (str.trim().length() > len) ? str.trim().substring(0, len) + "..." : str;

        return name_nira;
    }



    public static String func_stringpart(String str, int len) {

        String out = (str.trim().length() > len) ? str.trim().substring(0, len) + "..." : str;

        return out;
    }


    public static String func_filetype(String str) {

        String out = (str.trim().length() > 4) ? str.trim().substring(str.trim().length()-3) : "";

        return out;
    }

    public static String func_filearray_first(String str) {

        String pichas_clean = str.trim().substring(1, str.trim().length()-1);
        //Log.d("pichas2", pichas_clean);
        String[] fileValues = pichas_clean.split(",");

        String out = fileValues[0];

        return out;
    }


    public static String[] func_filearray_all(String str) {

        String pichas_clean = str.trim().substring(1, str.trim().length()-1);
        //Log.d("pichas2", pichas_clean);
        String[] fileValues = pichas_clean.split(",");

        return fileValues;
    }



    public int func_getDrawableImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        return drawableResourceId;
    }


    public static String func_formatDate(int year, int month, int day) {

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }


    public static String func_formatDateFromString(String value) {

        if(value.length() == 13){
            Long raw_time_long = Long.parseLong(value);
            Date new_time = new Date(raw_time_long);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm"); //, HH:mm
            return sdf.format(new_time);
        } else {
            return value;
        }

        //Date date = null;
        /*if(value.length() > 6){
            SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-mm-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

            try {
                String reformat = sdf.format(fromUser.parse(value));
                return reformat;
            }
            catch (Exception e) {
                Log.d("DateFromString", e.getLocalizedMessage());
            }
        }*/

    }


    public static String func_formatUnixDate(String raw_time) {

        Long raw_time_long = Long.parseLong(raw_time);
        if(raw_time.length() < 13){
            raw_time_long = raw_time_long * 1000;
        }
        Date new_time = new Date(raw_time_long);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm");

        return sdf.format(new_time);
    }


    public static String func_formatDecimal(double val) {

        DecimalFormat form = new DecimalFormat("0.00");
        String value_out = form.format(val);

        return value_out;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String base64Encode(String rawData) {
        result = null;

        byte[] data = rawData.getBytes(StandardCharsets.UTF_8);
        result = Base64.encodeToString(data, Base64.DEFAULT);
        return result;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String base64Decode(String rawData) {
        result = null;

        byte[] data = Base64.decode(rawData, Base64.DEFAULT);
        result = new String(data, StandardCharsets.UTF_8);
        return result;
    }


    public static String func_sanitizePhoneNumber(String phone) {

        if(phone.equals("")){
            return "";
        }

        else if (phone.length() < 11 & phone.startsWith("0")) {
            String p = phone.replaceFirst("^0", "254");
            return p;
        }

        else if(phone.length() == 13 && phone.startsWith("+")){
            //String p = phone.replaceFirst("^+", "");
            String p = phone.substring(1);
            return p;
        } else {
            return phone;
        }
    }

}
