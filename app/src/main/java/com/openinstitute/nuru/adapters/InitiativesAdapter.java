package com.openinstitute.nuru.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.bumptech.glide.Glide;


import com.openinstitute.nuru.R;
import com.openinstitute.nuru.models.InitiativesModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;



public class InitiativesAdapter extends RecyclerView.Adapter<InitiativesAdapter.MyViewHolder>  {

    private Context context;
    private String ImageUrl;

    SharedPreferences preferences;
    private String user_name, user_email, session_id;

    private List<InitiativesModel> InitiativesModelList;
    private List<InitiativesModel> InitiativesModelListFiltered;

    private AdapterView.OnItemClickListener clickListener;

    InitiativesAdapter.MyViewHolder viewHolder;


    public InitiativesAdapter(Context context, List<InitiativesModel> InitiativesModelList){

        this.context = context;
        this.InitiativesModelList = InitiativesModelList;
        this.InitiativesModelListFiltered = InitiativesModelList;

    }


    //@NonNull
    @Override
    public InitiativesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_initiative, parent,false);
        viewHolder = new InitiativesAdapter.MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final InitiativesAdapter.MyViewHolder holder,
                                 final int position) {
        JSONObject pinRecord = InitiativesModelList.get(position).get_post_entry();

        try {
            String title = pinRecord.getString("title");
            String narration = pinRecord.getString("narration");
            String start_date= pinRecord.getString("start_date");
            String end_date = pinRecord.getString("end_date");
            String summary = pinRecord.getString("summary");
            String tags= pinRecord.getString("tags");
            String initiative_private = pinRecord.getString("private");


            String rec_details = func_stringpart(summary, 150);

            holder.title.setText(title);
            holder.description.setText(summary);
            holder.date.setText(start_date);




            /*if (!post_files_last.equals("")) {
                String f_type = func_filetype(post_files_last);

                if(!f_type.equals("3gp")) {
                    Glide.with(holder.itemView.getContext()).load(post_files_last).into(holder.thumbnail);
                }
                else {
                    Glide.with(holder.itemView.getContext()).load(R.drawable.ic_mic_white_24dp).into(holder.thumbnail);
                }
                holder.thumbnail.setVisibility(View.VISIBLE);
            }
            else {
                holder.thumbnail.setVisibility(View.GONE);
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return InitiativesModelList == null ? 0 : InitiativesModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, description, date, rec_project, rec_tag, rec_post_id;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view){
            super(view);

            title = itemView.findViewById(R.id.init_title);
            date = itemView.findViewById(R.id.start_date);
            description = itemView.findViewById(R.id.summary);
//            thumbnail = itemView.findViewById(R.id.thumbnail);
//            overflow = itemView.findViewById(R.id.overflow);
//            rec_project = itemView.findViewById(R.id.rec_project);
//            rec_tag = itemView.findViewById(R.id.rec_tag);
//            rec_post_id = itemView.findViewById(R.id.rec_post_id);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {

                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view12) {

                    return true;
                }
            });
        }
    }


    public String func_stringpart(String str, int len) {

        String out = (str.trim().length() > len) ? str.trim().substring(0, len) + "..." : str;

        return out;
    }

}