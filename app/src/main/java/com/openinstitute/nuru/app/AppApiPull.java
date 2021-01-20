package com.openinstitute.nuru.app;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.openinstitute.nuru.app.Globals.CONF_POSTS_API_PULL;


public class AppApiPull extends StringRequest {

    private final Map<String, String> parameters;

    public AppApiPull(String form_data, String form_action, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, CONF_POSTS_API_PULL + Math.random(), listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("action", form_action);
        parameters.put("post_data", form_data);

        /*Log.d("formApiPush", String.valueOf(parameters));*/
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}