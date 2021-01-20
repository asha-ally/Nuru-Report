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

import com.bumptech.glide.Glide;
import com.openinstitute.nuru.R;
import com.openinstitute.nuru.models.PostsListModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.openinstitute.nuru.app.AppFunctions.func_filetype;
import static com.openinstitute.nuru.app.AppFunctions.func_stringpart;
import static com.openinstitute.nuru.app.Globals.KEY_USER_EMAIL;
import static com.openinstitute.nuru.app.Globals.KEY_USER_NAME;


public class HomePostsListAdapter extends RecyclerView.Adapter<HomePostsListAdapter.MyViewHolder>  {

    private final Context context;
    private String ImageUrl;

    SharedPreferences preferences;
    private final String user_name;
    private final String user_email;
    private String session_id;

    /*private UserSession session;
    private HashMap<String, String> user;
    private String name, email, photo, mobile;*/

    private final List<PostsListModel> PostsListModelList;
    private final List<PostsListModel> PostsListModelListFiltered;

    private AdapterView.OnItemClickListener clickListener;

    MyViewHolder viewHolder;

    public HomePostsListAdapter(Context context, List<PostsListModel> PostsListModelList){

        this.context = context;
        this.PostsListModelList = PostsListModelList;
        this.PostsListModelListFiltered = PostsListModelList;

        this.preferences =  context.getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        this.user_email = preferences.getString(KEY_USER_EMAIL, null);
        this.user_name = preferences.getString(KEY_USER_NAME, null);

    }


    //@NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.card_row_new, parent,false);
//        viewHolder = new MyViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,
                                 final int position) {
        JSONObject pinRecord = PostsListModelList.get(position).get_post_entry();

        try {
            String post_description = pinRecord.getString("post_description");
            String post_date = pinRecord.getString("post_date_web");
            String post_initiative = pinRecord.getString("initiative_name");
            String organization_name = pinRecord.getString("organization_name");
            String post_longitude = pinRecord.getString("post_longitude");
            String post_latitude = pinRecord.getString("post_latitude");
            String post_user = pinRecord.getString("post_user");
            String post_tag = pinRecord.getString("post_tag");
            String post_entry_id = pinRecord.getString("post_entry_id");

            String post_files_num = pinRecord.getString("post_files_num");
            String post_files_last = pinRecord.getString("post_files_last");
            String post_files = pinRecord.getString("post_files");

            String rec_details = func_stringpart(post_description, 150);

            holder.title.setText(post_date);
            holder.description.setText(rec_details);
            holder.date.setText(post_date);
            holder.rec_project.setText(post_initiative + " by " + organization_name);
            holder.rec_tag.setText(post_tag);
            holder.rec_post_id.setText( post_entry_id );

            if (!post_files_last.equals("")) {
                String f_type = func_filetype(post_files_last);

                if(!f_type.equals("3gp")) {
                    Glide.with(holder.itemView.getContext()).load(post_files_last).into(holder.thumbnail);
                }
                else {
//                    Glide.with(holder.itemView.getContext()).load(R.drawable.ic_mic_white_24dp).into(holder.thumbnail);
                }
                holder.thumbnail.setVisibility(View.VISIBLE);
            }
            else {
                holder.thumbnail.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public int getItemCount() {
        return PostsListModelList == null ? 0 : PostsListModelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, description, date, rec_project, rec_tag, rec_post_id;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view){
            super(view);

//            title = itemView.findViewById(R.id.title);
//            date = itemView.findViewById(R.id.date);
//            description = itemView.findViewById(R.id.description);
//            thumbnail = itemView.findViewById(R.id.thumbnail);
//            overflow = itemView.findViewById(R.id.overflow);
//            rec_project = itemView.findViewById(R.id.rec_project);
//            rec_tag = itemView.findViewById(R.id.rec_tag);
//            rec_post_id = itemView.findViewById(R.id.rec_post_id);


            view.setOnClickListener(view1 -> {

            });

            view.setOnLongClickListener(view12 -> {

                return true;
            });
        }
    }



}
