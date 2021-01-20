package com.openinstitute.nuru.models;

import org.json.JSONException;
import org.json.JSONObject;

public class InitiativesModel {

    private JSONObject post_entry = null;

    public InitiativesModel(JSONObject record) {
        this.post_entry = record;
    }

    public JSONObject get_post_entry() {
        return post_entry;
    }

    public void set_post_entry(JSONObject post_entry) {
        this.post_entry = post_entry;
    }

}