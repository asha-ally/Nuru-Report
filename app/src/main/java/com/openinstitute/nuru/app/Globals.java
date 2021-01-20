package com.openinstitute.nuru.app;

/**
 * Created by oi-dev-01 on 24/12/17.
 */

public interface Globals {

    String CONF_APP_NAME = "com.openinstitute.nreport";
    String msg_no_internet = "Internet not connected!";

   String CONF_DOMAIN1      = "https://nuru.live/dashboard/";
    String CONF_DOMAIN      = "https://nuru.sand-box.online/";    // @@ sandbox
    //String CONF_DOMAIN      = "http://10.0.2.2/oi_nuru_dashboard/";         // @@ local

    String CONF_API_UPDATE_CHECK = CONF_DOMAIN + "api/ajbk_update.php?tk=123";
    String CONF_API_SERVER      = CONF_DOMAIN + "api/";
    String CONF_POSTS_API_PULL  = CONF_DOMAIN + "api/nuru_user.php?tk=";
    String CONF_POSTS_API_PUSH  = CONF_DOMAIN + "api/register";
//    String CONF_POSTS_API_PUSH  = "https://5e841dfe6332.ngrok.io/nuru/public/";
    String CONF_FILE_UPLOAD     = CONF_DOMAIN + "api/ajbk_upload.php";

    String CONF_WEB_POLICY_CONTENT = "https://nuru.live/nuru-user-generated-content-policy/";
    String CONF_WEB_POLICY_PRIVACY = "https://nuru.live/privacy-policy/";
    String CONF_WEB_POLICY_COOKIES = "https://nuru.live/cookie-policy/";
    String CONF_WEB_POLICY_TERMS = "https://nuru.live/nuru-terms-of-use/";
    String CONF_WEB_DASHBOARD = CONF_DOMAIN1;



    int imageView_width = 300; /*250*/
    int imageView_height = 350; /*200*/

    /* TABLE VARIABLES */
    String KEY_USER_ID = "user_id";
    String KEY_USER_NAME = "user_name";
    String KEY_USER_PHONE = "user_number";
    String KEY_USER_EMAIL = "user_email";
    String KEY_USER_PASS = "user_pass";
    String KEY_USER_META = "user_meta";
    String KEY_USER_AUTH = "user_auth";
    String KEY_USER_DATE = "user_date";
    String KEY_USER_SESSION = "session_id";

    /*Rage Chips*/
    String[] CONF_POST_TAGS = new String[]{
            "Discrimination",
            "Disturbance",
            "Food Items Availability",
            "Food Items Cost",
            "Food Distribution",
            "Gender Based Violence",
            "Restriction of Movement",
            "COVID-19 Suspected Case",
            "COVID-19 Recovered Case",
            "COVID-19 Death(s)",
            "Social Distancing",
            "Self Isolation",
            "Hand Washing",
            "Sanitisers",
            "Mask and Gloves",
            "Ventilators",
            "Medical Doctors",
            "Medical Staff",
            "Number of Hospital Beds",
            "Number of ICU Beds",
            "PPE Equipment",
            "Drugs",
            "Sanitiser Supply",
            "Maternal Health",
            "Water Availability",
            "Water Cost",
            "Restriction of Movement",
            "Riots"
    };

}
