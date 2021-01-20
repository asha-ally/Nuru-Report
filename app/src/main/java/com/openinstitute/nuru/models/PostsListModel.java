package com.openinstitute.nuru.models;

import org.json.JSONException;
import org.json.JSONObject;

public class PostsListModel {

    private String post_entry_id = "";
    private String user_id = "";
    private String initiative_id = "";
    private String post_session = "";
    private String post_description = "";
    private String post_tag = "";
    private String post_longitude = "";
    private String post_latitude = "";
    private String post_country = "";
    private String post_country_code = "";
    private String post_date_web = "";
    private String organization_code = "";
    private String organization_name = "";
    private String post_user = "";
    private String post_files_num = "";
    private String post_files_last = "";
    private String post_files = "";

    private JSONObject post_entry = null;


    public PostsListModel(JSONObject record) {

        try {

            this.post_entry = record;

            this.post_entry_id = record.getString("post_entry_id");
            this.user_id = record.getString("user_id");
            this.initiative_id = record.getString("initiative_id");
            this.post_session = record.getString("post_session");
            this.post_description = record.getString("post_description");
            this.post_tag = record.getString("post_tag");
            this.post_longitude = record.getString("post_longitude");
            this.post_latitude = record.getString("post_latitude");
            this.post_country = record.getString("post_country");
            this.post_country_code = record.getString("post_country_code");
            this.post_date_web = record.getString("post_date_web");
            this.organization_code = record.getString("organization_code");
            this.organization_name = record.getString("organization_name");
            this.post_files_num = record.getString("post_files_num");
            this.post_files_last = record.getString("post_files_last");
            this.post_files = record.getString("post_files");
            this.post_user = record.getString("post_user");


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public JSONObject get_post_entry() {
        return post_entry;
    }

    public void set_post_entry(JSONObject post_entry) {
        this.post_entry = post_entry;
    }



}
