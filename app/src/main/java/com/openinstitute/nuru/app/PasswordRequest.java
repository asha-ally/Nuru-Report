package com.openinstitute.nuru.app;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import static com.openinstitute.nuru.app.Globals.CONF_API_SERVER;


public class PasswordRequest extends StringRequest {

    private static final String LOGIN_URL = CONF_API_SERVER + "aj_forget.php?tk=" + Math.random();
    private final Map<String, String> parameters;

    public PasswordRequest(String email, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, LOGIN_URL, listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("email", email);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
