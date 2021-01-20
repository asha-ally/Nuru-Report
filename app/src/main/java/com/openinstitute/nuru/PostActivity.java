package com.openinstitute.nuru;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.Tracker;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.openinstitute.nuru.Database.DatabaseHelper;
import com.openinstitute.nuru.Database.TagList;
import com.openinstitute.nuru.app.Globals;
import com.openinstitute.nuru.app.WebAsyncPost;
import com.openinstitute.nuru.utils.GPS_Service;
import com.openinstitute.nuru.utils.VolleyMultipartRequest;


import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static android.Manifest.permission.ACCESS_BACKGROUND_LOCATION;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static com.openinstitute.nuru.AppFunctions.func_filetype;
import static com.openinstitute.nuru.AppFunctions.isInternetConnected;
import static com.openinstitute.nuru.app.AppFunctions.base64Encode;
import static com.openinstitute.nuru.app.AppFunctions.func_getUserCoded;
import static com.openinstitute.nuru.app.AppFunctions.func_showAlerts;
import static com.openinstitute.nuru.app.Globals.CONF_FILE_UPLOAD;
import static com.openinstitute.nuru.app.Globals.CONF_POSTS_API_PUSH;
import static com.openinstitute.nuru.app.Globals.imageView_height;
import static com.openinstitute.nuru.app.Globals.imageView_width;
import static com.openinstitute.nuru.app.Globals.msg_no_internet;


public class PostActivity extends AppCompatActivity {

    /* RAGE CAMERA */
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_GALLERY = 2;
    public static final int MEDIA_TYPE_VIDEO = 3;
    public static final int PICTURE_RESULT = 1;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    private static final String TAG = com.openinstitute.nuru.PostActivity.class.toString();
    private static final int CREATE_FILE = 1;
    private static final int CAMERA_REQUEST_CODE = 111;
    private static final int PICKFILE_RESULT_CODE = 1;
    public static String res_image_folder;
    public static File res_nuru_folder;
    private static LinearLayout linearLayout;
    static GridLayout gridLayout;
    static int cardHeight = 0;
    static int cardWidth = 0;
    static TextView txt_files_header;
    static Context _context;
    static String fileAuthority;
    static int cardBg;
    private static String user_email_code;
    private static Tracker mTracker;
    private static final ArrayList<String> filesList = new ArrayList<String>();
    EditText etDescription;
    Button btnSave;
    Button btnAddphoto;
    Button btnAddVideo;
    Button btnAddVoiceNote;
    Button btnInsertFile;
    int postId;
    Bundle bundle;
    String bundleData;
    String form_activity;
    String form_action;
    String form_data;
    String form_post_id;
    String form_post_position;
    String form_post_session;

    /* Rage Camera */
    /*private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    static final int PICTURE_RESULT = 1;
    private Uri file;
    String mCurrentPhotoPath;*/
    /* ----- */
    /*ImageView imageView;*/
    Bitmap help1;
    String picturePath;
    SharedPreferences prefs;
    Context context;
    ImageView imageView;
    Spinner projects;
    Spinner category;
    Spinner tag;
    DatabaseHelper databaseHelper;
    TextView tvlongitude;
    TextView tvlatitude;
    /* Rage Chips*/
    ChipGroup txt_tags_chipGroup;
    /* End:: RAGE CAMERA */
    TextInputEditText txt_tags;
    List<TagList> chip_tags;
    String[] chip_tags_default;
    String[] filepath;
    String[] chip_tags_selected;
    String chips_saved;
    View focusView;
    Boolean internetConnected = false;
    String gps_latitude;
    String gps_longitude;
    String trimmed = "";
    String autoCompleteText = "";
    private String description;
    private BroadcastReceiver broadcastReceiver;
    private String title;
    private String imageUrl;
    private String date;
    private String audioUrl;
    private String Url;
    private String user_email;
    private String post_projects;
    private String post_tag;
    private String post_session;
    private String post_category;
    private String post_longitude;
    private String post_latitude;
    private String filePath;
    private String filePathChoose;
    private Uri fileUri;
    private Uri file;
    private Bitmap bitmap;
    private WebAsyncPost asyncForm;

   /* static int imageView_width = 250;
    static int imageView_height = 200;*/

    //TODO -- getOrientation() akulaku
    private Button btnAddTag;
    private AutoCompleteTextView actv;

    public static void imageView_add(String url, String ext, int num) {

        if (linearLayout != null) {

            txt_files_header.setVisibility(View.VISIBLE);

            /*--- grid --------------------------*/
            CardView cardView = new CardView(_context);
            CardView.LayoutParams cardViewParam = new CardView.LayoutParams(
                    cardWidth,
                    cardHeight
            );
            cardView.setCardBackgroundColor(cardBg);
            cardView.setPadding(10, 10, 10, 10);
            cardView.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
            cardView.setLayoutParams(cardViewParam);
            /*--- grid --------------------------*/

            RelativeLayout relativeLayout = new RelativeLayout(_context);
            RelativeLayout.LayoutParams relativeViewParam = new RelativeLayout.LayoutParams(
                    imageView_width,
                    imageView_height
            );
            /*RelativeLayout.LayoutParams relativeViewParam = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );*/
            filesList.add(url);

            relativeLayout.setLayoutParams(relativeViewParam);

            //if (ext.equals("3gp") || ext.equals("jpg")) {

            ImageView imageView = new ImageView(_context);
            /*imageView.setLayoutParams(new LinearLayout.LayoutParams(imageView_width, imageView_height));*/
                /*imageView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));*/
            imageView.setLayoutParams(new LinearLayout.LayoutParams(cardWidth - 20, cardHeight - 20));

            if (ext.equals("3gp")) {
                imageView.setImageResource(R.drawable.ic_mic_white_24dp);
            } else {

                if (ext.equals("mp4")) {
                    imageView.setImageBitmap(ThumbnailUtils.createVideoThumbnail(url, MediaStore.Video.Thumbnails.MICRO_KIND));
                } else {
                    Bitmap thumbnail = (BitmapFactory.decodeFile(url));
                    imageView.setImageBitmap(thumbnail);
                }
            }

            //relativeLayout.addView(imageView);
            //linearLayout.addView(relativeLayout);
            cardView.addView(imageView);
            gridLayout.addView(cardView);

            imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    String mimeType = (ext.equals("3gp")) ? "audio/*" : "image/*";
                    if (ext.equals("mp4")) {
                        mimeType = "video/*";
                    }

                    File photoFile = new File(url);
                    Uri uri_one;
                    uri_one = FileProvider.getUriForFile(_context, fileAuthority, photoFile);
                    Intent viewIntent = new Intent();
                    viewIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    viewIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    viewIntent.setDataAndType(uri_one, mimeType);
                    viewIntent.setAction(Intent.ACTION_VIEW);
                    _context.startActivity(Intent.createChooser(viewIntent, null));

                }
            });


            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    imageView_remove(v, url);
                    return true;
                }
            });
            //}


            if (ext.equals("mp4X")) {

                VideoView videoView = new VideoView(_context);
                videoView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                /*videoView.setLayoutParams(new LinearLayout.LayoutParams(imageView_width, imageView_height));*/
                videoView.setVideoURI(Uri.parse(url));

                //relativeLayout.addView(videoView);
                //linearLayout.addView(relativeLayout);

                cardView.addView(videoView);
                gridLayout.addView(cardView);

                videoView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {


                        File photoFile = new File(url);
                        Uri uri_one = FileProvider.getUriForFile(_context, fileAuthority, photoFile); /*getString(R.string.file_provider_authority)*/
                        Intent vidIntent = new Intent();
                        vidIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        vidIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        vidIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        vidIntent.setDataAndType(uri_one, "video/*");
                        vidIntent.setAction(Intent.ACTION_VIEW);
                        _context.startActivity(Intent.createChooser(vidIntent, null));
                    }
                });


                videoView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //imageView_remove(v, url);
                        return true;
                    }
                });
            }


        }

    }

    private static void imageView_remove(View v, String photoPath) {
        final File photoFile = new File(photoPath);

        if (photoFile.exists()) {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(_context);
            alertDialogBuilder.setTitle(R.string.lbl_caution);
            alertDialogBuilder.setMessage(R.string.lbl_remove_file).setCancelable(false).setPositiveButton(R.string.lbl_yes, new DialogInterface.OnClickListener() {
                public void onClick(
                        DialogInterface dialog,
                        int id) {
                    v.setVisibility(View.GONE);
                    photoFile.delete();
                    filesList.remove(photoPath);
                    dialog.cancel();
                }
            }).setNegativeButton(R.string.lbl_no, new DialogInterface.OnClickListener() {
                public void onClick(
                        DialogInterface dialog,
                        int id) {
                    dialog.cancel();
                }
            });

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    }


    /**
     * Create a File for saving an image or video
     */
    /* @@Rage :: Added parameter to determine whether to add 'Extension' */
    private static File files_getResultMediaFile(int type, boolean ni_capture) {

        File mediaStorageDir;

        String de_path = files_getExternalSdCardPath();

        mediaStorageDir = new File(de_path + "/nuru/" + "/nuru_images/");

        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdir();

            if (!mediaStorageDir.mkdirs()) {
                //Log.d("NuruCamera", "failed to create directory");
                return null;
            }
        }

        String fileName;
        String dateString = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());


        fileName = user_email_code + "_" + dateString;

        File mediaFile;
        String mediaDir = mediaStorageDir.getPath();
        String fileExt;

        if (type == MEDIA_TYPE_IMAGE) {
            fileExt = (ni_capture) ? ".jpg" : "";
            mediaFile = new File(mediaDir + File.separator + fileName + fileExt);
        } else if (type == MEDIA_TYPE_VIDEO) {
            fileExt = (ni_capture) ? ".mp4" : "";
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + fileName + fileExt);
        } else {
            return null;
        }

        return mediaFile;
    }

    public static String files_getExternalSdCardPath() {
        File sdCardFile;
        String path;

        sdCardFile = files_getSecondaryStorage();
        path = sdCardFile.getAbsolutePath();
        return path;
    }

    /**
     * Gets the external sd card path from a system variable, if the result
     * was null, this function call the function files_getSdcardByPossiblePaths to
     * verify the path manually.
     *
     * @return externalSdCard the external sd card {@link File}
     */
    private static File files_getSecondaryStorage() {
        String value = System.getenv("SECONDARY_STORAGE");
        File externalSdCard = null;

        if (!TextUtils.isEmpty(value)) {
            assert value != null;
            String[] paths = value.split(":");

            for (String path : paths) {
                File file = new File(path);

                if (files_testIsWritable(file)) {
                    externalSdCard = file;
                }
            }
        }

        if (externalSdCard == null) {
            externalSdCard = files_getSdcardByPossiblePaths();

        }

        return externalSdCard;
    }

    /**
     * There are a lot of different kind of android versions and trademarks,
     * so, the implementation of each version are different.
     * <p>
     * This function verifies in a list of possibilities the path of the
     * external sd card.
     *
     * @return sdCardFile the external sd card {@link File}
     */
    private static File files_getSdcardByPossiblePaths() {
        File sdCardFile;
        String path = null;

        List<String> sdCardPossiblePath = Arrays.asList("external_sd", "ext_sd", "external", "extSdCard");

        for (String sdPath : sdCardPossiblePath) {
            File file = new File("/mnt/", sdPath);

            if (files_testIsWritable(file)) {
                path = file.getAbsolutePath();
            }
        }

        if (path != null) {
            sdCardFile = new File(path);
        } else {
            /*sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());*/
            sdCardFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        }

        return sdCardFile;
    }

    /**
     * Test whether a file is writable or not.
     *
     * @param {@link File}
     * @return {@link Boolean} isWritable
     */
    public static boolean files_testIsWritable(File file) {
        boolean isWritable = false;

        if (file.isDirectory() && file.canWrite()) {
            String path = file.getAbsolutePath();

            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
            File testWritable = new File(path, "test_" + timeStamp);

            if (testWritable.mkdirs()) {
                testWritable.delete();
                isWritable = true;
            }
        }

        return isWritable;
    }

    public static com.openinstitute.nuru.PostActivity audioResponse(String response) {
        //filesList.add(audio_url);
        imageView_add(response, "3gp", 0);
        //imageAdd_Relay(response);
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        context = this;
        _context = this;

        this.setTitle("Share What's Up!");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        UploadService.NAMESPACE = "com.openinstitute.nuru";

//        bundle = getIntent().getExtras();

//        form_activity = Objects.requireNonNull(bundle).getString("PostActivity");
//
//        form_action = bundle.getString("PostAction", null);
//        form_data = bundle.getString("PostData", null);
//        form_post_id = bundle.getString("PostId", null);
//        form_post_position = bundle.getString("PostPosition", null);
//        form_post_session = bundle.getString("PostSession", null);

        linearLayout = this.findViewById(R.id.container);
        gridLayout = findViewById(R.id.wrapResourceGrid);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        cardHeight = displayMetrics.heightPixels / 4;
        cardWidth = (displayMetrics.widthPixels / 2) - 40;

        fileAuthority = getString(R.string.file_provider_authority);
        cardBg = getResources().getColor(R.color.colorGrayLighter);

//        AnalyticsApplication application = (AnalyticsApplication) getApplication();
//        mTracker = application.getDefaultTracker();

        //Log.d("test_int", ""+ Integer.parseInt("success"));
        /*Log.d("form_data", form_data);*/
        /*Log.d("form_post_session", form_post_session);*/

        focusView = null;
        imageUrl = "";

        txt_files_header = findViewById(R.id.txt_files_header);
        etDescription = findViewById(R.id.etDescription);

        actv = findViewById(R.id.autoCompleteTextView1);

        /* Rage Chips*/
        chip_tags_default = Globals.CONF_POST_TAGS;
        chip_tags = new ArrayList<>();

        btnAddTag = findViewById(R.id.btnAddTag);

        txt_tags_chipGroup = findViewById(R.id.txt_tags_chipGroup);
        txt_tags = findViewById(R.id.txt_tags);

        txt_tags_chipGroup.setBackground(txt_tags.getBackground());
        txt_tags.setBackground(null);

        ArrayAdapter adapter = new
                ArrayAdapter(context, android.R.layout.simple_list_item_1, chip_tags_default);

        actv.setAdapter(adapter);
        actv.setThreshold(1);


//        imageView = findViewById(R.id.imgView);
        ImageView imgView = findViewById(R.id.thumbnail);
        databaseHelper = new DatabaseHelper(this);
        btnSave = findViewById(R.id.btnSave);

        prefs = getApplicationContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String user_id = prefs.getString("user_email", null);
        user_email = prefs.getString("user_email", null);

        /*Log.d(TAG, "user_email: " + user_email);*/
//        user_email_code = func_getUserCoded(user_email);

        btnAddphoto = findViewById(R.id.btnAddPhoto);
//        btnAddVideo = findViewById(R.id.btnAddVideo);
        btnAddVoiceNote = findViewById(R.id.btnAddVoiceNote);
        btnInsertFile = findViewById(R.id.btnInsertFile);
        /*projects = findViewById(R.id.project_array);*/
        /*tag = findViewById(R.id.tag_array);*/
        /*category = findViewById(R.id.category_array);*/
        tvlongitude = findViewById(R.id.tvlongitude);
        tvlatitude = findViewById(R.id.tvlatitude);

        callGPSBroadcast();

        Intent intent = new Intent(getApplicationContext(), GPS_Service.class);
        startService(intent);

        //getPostId();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            CheckPermissions();
        }

        callGPSBroadcast();

//        if (!form_action.equals("_edit")) {
//            //addDefaultChips();
//            /*jsonObject.getString("post_details")*/
//        } else {
//            displayPost();
//            addSavedChips(chips_saved);
//        }
        /*String tags = jsnobject.getString("post_tags").replace("|", "; ");*/


        actv.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int startIndex = 0;
                int endIndex = s.length();
                boolean startFound = false;


                if (s.length() != 0) {
                    btnAddTag.setEnabled(true);
                    btnAddTag.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

                    //autoCompleteText = s.toString();

                    String lastChar = s.subSequence(endIndex - 1, endIndex).toString();
                    if (lastChar.equals(",") || lastChar.equalsIgnoreCase("\n")) {
                        //Log.d(TAG, "rage_chip_changed: " + s.toString());
                        trimmed = s.subSequence(startIndex, endIndex - 1).toString();

                        addChip(trimmed, "y");

                    }
                }
            }
        });


        btnAddphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*func_showAlerts(context,"Long press to record Video","");*/
                selectImage();
            }
        });


//        btnAddVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*func_showAlerts(context,"Long press to record Video","");*/
//                permission_camera(MEDIA_TYPE_VIDEO);
//            }
//        });


        btnAddTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //autoCompleteText = s.toString();
                autoCompleteText = actv.getText().toString();
                if (autoCompleteText.length() > 2) {
                    addChip(autoCompleteText, "y");
                }

            }
        });


        /*btnAddphoto.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent,MEDIA_TYPE_VIDEO);

                return false;
            }
        });*/


        btnInsertFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent,PICKFILE_RESULT_CODE);*/
                createFile(file);
//                func_showAlerts(context, "Coming soon. We'll keep you posted.", "warning");
            }

        });


        btnAddVoiceNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(getBaseContext(), VoiceRecordActivity.class);
//                startActivity(intent);

                func_showAlerts(context, "Coming soon. We'll keep you posted", "warning");

            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override

            public void onClick(View v) {

                StringBuilder tags_selected = new StringBuilder();
                for (int i = 0; i < txt_tags_chipGroup.getChildCount(); i++) {
                    Chip chip_item = (Chip) txt_tags_chipGroup.getChildAt(i);
                    if (chip_item.isChecked()) {
                        /*chip_tags_selected = new String[]{ chip_item.getText().toString() };*/
                        tags_selected.append(chip_item.getText().toString()).append("|");
                        /*Log.d("chipGroup_"+i, tags_selected );*/
                    }
                }
                //Bundle b = getIntent().getExtras();
                //audio_url = b.getString("audio_url");

                Timestamp action_time = new Timestamp(System.currentTimeMillis());
                String action_time_id = String.valueOf(action_time.getTime());

                //Log.d("chip_tags_selected", tags_selected);
                //Log.d("audio_url", "" + audio_url);
                //Log.d("file_List", "" + filesList);

                /*if(filesList.size() > 0) {
                    for (int i = 0; i < filesList.size(); i++) {
                        Log.d("file_List "+ i, "" + filesList.get(i));
                        String f_name = func_filetype(filesList.get(i));
                        Log.d("file_Type "+ i, f_name);
                        //String up_file_name = user_email_code + "-" + post_session + "-" + Math.random();

                    }
                }*/


                description = etDescription.getText().toString();

                /*post_projects= projects.getSelectedItem().toString();*/
                post_projects = "COVID-19";
                /*post_tag = tag.getSelectedItem().toString();*/
                post_tag = tags_selected.toString();
                post_session = action_time_id;
                post_category = "post_activity"; // category.getSelectedItem().toString();
                post_longitude = tvlongitude.getText().toString();
                post_latitude = tvlatitude.getText().toString();

                int len_description = description.length();
                int len_tags_selected = tags_selected.length();


                boolean cancel = false;

                if (len_description == 0) {
                    func_showAlerts(context, "Comments / Details required", "warning");
                    focusView = etDescription;
                    cancel = true;
                } else if (len_tags_selected == 0) {
                    func_showAlerts(context, "Select or Enter at least one Tag", "warning");
                    focusView = txt_tags;
                    cancel = true;
                }

                if (cancel) {

                    focusView.requestFocus();

                } else {


                    String result = "0";
                    //
                    if (form_action.equals("_edit")) {
                        post_session = form_post_session;
                        //Log.d("form_post_id", form_post_session);
                    }
                    //PostActivity post =new PostActivity(title,description,date,imageUrl,audioUrl,user_id);
                    //databaseHelper.addPost(post);

                    JSONObject post_b = new JSONObject();
                    try {

                        if (form_action.equals("_edit")) {
                            post_b.put("post_id", form_post_id);
                        }
                        //post_b.put("imageUrl", Url);
                        post_b.put("user_id", user_email);
                        post_b.put("description", description);
                        post_b.put("post_project", post_projects);
                        post_b.put("post_tag", post_tag);
                        post_b.put("post_session", post_session);
                        post_b.put("post_category", post_category);
                        post_b.put("post_longitude", post_longitude);
                        post_b.put("post_latitude", post_latitude);
                        post_b.put("image_url", filesList.toString());
                        //post_b.put("image_url", imageUrl);
                        //post_b.put("audio_url",audio_url);
                        //Log.d("filesList", filesList.toString());


                        if (form_action.equals("_edit")) {
                            result = databaseHelper.post_Edit(post_b, form_post_id);
                        } else {
                            result = databaseHelper.post_Add(post_b);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /*Log.d("post_result", String.valueOf(result));*/


                    internetConnected = isInternetConnected(context);
                    if (internetConnected) {


                        if (filesList.size() > 0) {
                            for (int i = 0; i < filesList.size(); i++) {
                                String f_url = filesList.get(i);
                                /*Log.d("file_List " + i, "" + f_url);*/
                                if (f_url.length() > 4) {
                                    String f_type = func_filetype(f_url);
                                    String up_file_name;
                                    up_file_name = user_email_code + "-" + post_session + "-" + Math.random();

                                    /*rage_after_debug_return*/
                                    files_uploadMultipart(f_url, up_file_name, f_type, result);
                                }
                            }
                        }

                        /*if(filesList.toString().length() > 4) {
                            String up_file_name = user_email_code + "-" + post_session + "-" + Math.random();
                            files_uploadMultipart(up_file_name, result);
                        }*/

                        String booking_sess = "" + Math.random();
                        String form_category = "transfer";
                        String jsonString = String.valueOf(post_b);
                        String resultJson = base64Encode(jsonString);

                        /*rage_after_debug_return*/
                        asyncForm = new WebAsyncPost(context, user_email, form_category, resultJson, CONF_POSTS_API_PUSH, "POST");
                        asyncForm.execute();
                        filesList.clear();

                        //func_showAlerts(context, "Saved to server.", "");
                    } else {
                        func_showAlerts(context, msg_no_internet, "warning");
                    }

                    /*rage_after_debug_return*/

//                    refreshMainActivity();
                    finish();


                    //postNote("none",description,user_id);

                    stopService(intent);

                }
            }


        });


    }

    private void createFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //intent.setType("application/pdf");
        intent.setType("*/*");
        //intent.putExtra(Intent.EXTRA_TITLE, "invoice.pdf");

        // Optionally, specify a URI for the directory that should be opened in
        // the system file picker when your app creates the document.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
        }

        startActivityForResult(intent, CREATE_FILE);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.i("activityanalytics", "Setting screen name: " + "PostActivity");
//        mTracker.setScreenName("Image~" + "PostActivity");
//        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//        mTracker.send(new HitBuilders.EventBuilder()
//                .setCategory("Action")
//                .setAction("Share")
//                .build());
        if (broadcastReceiver == null) {
            callGPSBroadcast();
        }


    }

    public void callGPSBroadcast() {

        gps_latitude = prefs.getString("gps_latitude", null);
        gps_longitude = prefs.getString("gps_longitude", null);


       /* broadcastReceiver =new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                gps_latitude = String.valueOf(intent.getExtras().get("gps_latitude"));
                gps_longitude = String.valueOf(intent.getExtras().get("gps_longitude"));
            }
        };
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));

        Log.d("lat_long",gps_latitude + " - " + gps_longitude);
        */
        tvlatitude.setText(gps_latitude);
        tvlongitude.setText(gps_longitude);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds options to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private String selectImage() {

        files_test_getExternalStoragePath();

        CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"}; /*"Record Video", */

        if (Build.VERSION.SDK_INT >= 29) {
            //options = new CharSequence[]{"Choose from Gallery", "Cancel"};
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(com.openinstitute.nuru.PostActivity.this);
        builder.setTitle("Add Photo or Video:");
        CharSequence[] finalOptions = options;
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (finalOptions[item].equals("Take Photo")) {
                    permission_camera(MEDIA_TYPE_IMAGE);
                } else if (finalOptions[item].equals("Record Video")) {
                    permission_camera(MEDIA_TYPE_VIDEO);
                    /*Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, MEDIA_TYPE_VIDEO);*/

                } else if (finalOptions[item].equals("Choose from Gallery")) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    //startActivityForResult(intent, 2);
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        Uri photoURI = null;
                        File photoFile = files_getResultMediaFile(MEDIA_TYPE_IMAGE, false);


                        filePath = Objects.requireNonNull(photoFile).getAbsolutePath().trim();
                        filePathChoose = photoFile.getAbsolutePath();
                        fileUri = FileProvider.getUriForFile(context,
                                getString(R.string.file_provider_authority),
                                photoFile);
                        //fileUri = Uri.fromFile(photoFile);
                        //Log.d("file_choose_Uri", String.valueOf(fileUri));
                        //Log.d("file_choose_Path", path);

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                        startActivityForResult(intent, MEDIA_TYPE_GALLERY);
                    }

                } else if (finalOptions[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }

        });
        builder.show();

        //filesList.add(imageUrl);
        return imageUrl;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == MEDIA_TYPE_IMAGE) {

                imageUrl = filePath.trim();
                if (filePath != null) {
                    String ext = func_filetype(filePath);
                    imageView_add(filePath, ext, 0);

                    /*filesList.add(filePath);*/
                }

                /*picturePath = filePath;

                bitmap = (BitmapFactory.decodeFile(picturePath));

                ImageView imageView = new ImageView(getApplicationContext());
                //imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                imageView.setLayoutParams(new LinearLayout.LayoutParams(imageView_width, imageView_height));

                if (imageUrl != null){
                    Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                    //Log.w("image_path_a", imageUrl+"");
                    imageView.setImageBitmap(thumbnail);
                    //imageView.
                }
                else {
                    imageView.setVisibility(View.GONE);
                }
                if (linearLayout != null) {

                    txt_files_header.setVisibility(View.VISIBLE);

                    linearLayout.addView(imageView);

                    filesList.add(picturePath);
                    imageUrl = picturePath;
                }*/


            } else if (requestCode == MEDIA_TYPE_GALLERY) {
                /*Log.d("filePath2", String.valueOf(filePath));*/

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(Objects.requireNonNull(selectedImage), filePath, null, null, null);
                Objects.requireNonNull(c).moveToFirst();

                int columnIndex = c.getColumnIndex(filePath[0]);
                picturePath = c.getString(columnIndex);
                c.close();
                /*Log.d("picturePath", picturePath);*/
                String ext = (picturePath != null) ? func_filetype(picturePath) : "";

                /* COPY TO NURU FOLDER*/
                String sourcePath = picturePath;
                String destinationPath = filePathChoose + "." + ext;

                copyFileToAppFolder(sourcePath, destinationPath);
                /* END:: COPY TO NURU FOLDER*/

                imageUrl = destinationPath;
                if (picturePath != null) {
                    imageView_add(destinationPath, ext, 0);
                    //filesList.add(destinationPath);
                }
            } else if (requestCode == MEDIA_TYPE_VIDEO) {
                String videoPath = filePath.trim();

                if (filePath != null) {
                    String ext = func_filetype(filePath);
                    imageView_add(filePath, ext, 0);
                    //filesList.add(filePath);
                }
            }

            if (requestCode == CREATE_FILE && resultCode == Activity.RESULT_OK) {
                // The result data contains a URI for the document or directory that
                // the user selected.
                Uri uri = null;
                if (data != null) {
                    uri = data.getData();
                    // Perform operations on the document using its URI.
                }
            }
        }

        //Log.d("filesList", String.valueOf(filesList));
    }

    /* @@Rage :: Copy Selected File to Nuru Folder */
    public void copyFileToAppFolder(String sourcePath, String destinationPath) {

        InputStream f_src;
        OutputStream f_dest;

        try {
            f_src = new FileInputStream(sourcePath);
            f_dest = new FileOutputStream(destinationPath);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = f_src.read(buffer)) != -1) {
                f_dest.write(buffer, 0, read);
            }
            f_src.close();

            // write the output file
            f_dest.flush();
            f_dest.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addSavedChips(String de_chips) {
        //String tags = de_chips.replace("|", "; ");
        //Log.d("chips_saved_b", chips_saved);

        String[] pieces = chips_saved.split("\\|");

        for (String tag_item : pieces) {
            addChip(tag_item, "y");
        }

        /*addDefaultChips();*/
    }

    public void addDefaultChips() {

        for (String tag_item : chip_tags_default) {
            if (!form_action.equals("_edit") || !chips_saved.contains(tag_item)) {
                addChip(tag_item, "n");
            }
        }
    }

    /*private  void postNote(final String title, final String description,final String user_id){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, CONF_POSTS_API_PUSH, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //Log.d("post response",s);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    int user_id=jsonObject.getInt("user_id");
                    String title= jsonObject.getString("title");
                    String description=jsonObject.getString("description");


                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("json_error",volleyError.getMessage());

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>params=new HashMap<String, String>();
                params.put("user_id",String.valueOf(user_id));
                params.put("title",title);
                params.put("description",description);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getBaseContext());
        requestQueue.add(stringRequest);

    }*/

    public void addChip(String text, String checkd) {
        Chip chip = new Chip(this);

        int paddingDp = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics()
        );
        chip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
        chip.setText(text);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(true);

        chip.setChecked(checkd.equals("y"));

        chip.setCloseIconVisible(true);
        chip.setOnCloseIconClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_tags_chipGroup.removeView(chip);
            }
        });


        txt_tags_chipGroup.addView(chip);
        //txt_tags.setText("");
        actv.setText("");
        btnAddTag.setEnabled(false);
        btnAddTag.setBackgroundColor(getResources().getColor(R.color.colorGrayLighter));

    }





    /*
     * ==============================================================================
     * RAGE UPLOAD FUNCTION
     * ==============================================================================
     * */

    public void getPostId() {
        /*Bundle bundle = getIntent().getExtras();*/
        if (bundle != null) {
            postId = bundle.getInt("KEY_POST_ID", 0);
        }
    }

    public void displayPost() {

        String post = form_data;

        try {
            //JSONArray jsonArray = new JSONArray(post);
            JSONObject jsonObject = new JSONObject(post);
            //Log.d("displayPostObject", String.valueOf(jsonObject));
            //Log.d("displayPostObject", jsonObject.getString("post_Id"));

            chips_saved = jsonObject.getString("post_tags");
            //Log.d("chips_saved", chips_saved);

            etDescription.setText(jsonObject.getString("post_details"));
            //etTitle.setText(jsonObject.getString("record_date"));
            tvlongitude.setText(jsonObject.getString("post_longitude"));
            tvlatitude.setText(jsonObject.getString("post_latitude"));

            String picha = jsonObject.getString("post_files");

            if (picha.length() > 4 && picha.trim().startsWith("[")) {
                String pichas_clean = picha.trim().substring(1, picha.trim().length() - 1);
                String[] filesList = pichas_clean.split(",");

                int _num = 0;
                for (String file_item : filesList) {
                    /*Log.d("file_item", file_item);*/
                    if (file_item.length() > 4) {
                        String ext = func_filetype(file_item);
                        imageView_add(file_item.trim(), ext, _num);
                        _num++;
                    }
                }

            } else {
                if (picha.length() > 4) {
                    String ext = func_filetype(picha);
                    imageView_add(picha.trim(), ext, 0);
                }
            }


//            String imageUrl=jsonObject.getString("image_url");
            /*ImageView imageView = new ImageView(getApplicationContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setLayoutParams(new LinearLayout.LayoutParams(imageView_width, imageView_height));

            String imageUrl=jsonObject.getString("image_url");

            if (imageUrl!=null){
                Bitmap thumbnail = (BitmapFactory.decodeFile(imageUrl));
                Log.w("image_path_a", imageUrl+"");
                imageView.setImageBitmap(thumbnail);}
            else {
                imageView.setVisibility(View.GONE);
            }
            if (linearLayout != null) {
                linearLayout.addView(imageView);
            }*/
            //projects.setSelection();
        } catch (JSONException e) {
            //Log.e("displayPost_Post", e.getMessage());
        }

        /*Post post= new Post();
        JSONObject jsnobject = post.getPostAll();
        try {
            etDescription.setText( jsnobject.getString("post_details"));
            Log.d("edited", jsnobject.getString("post_details"));
            etTitle.setText(jsnobject.getString("record_date"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public boolean CheckPermissions() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED

        ) {

            requestPermissions(new String[]{ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION,
                    ACCESS_BACKGROUND_LOCATION,
                    CAMERA,
                    WRITE_EXTERNAL_STORAGE,
                    RECORD_AUDIO,
                    READ_EXTERNAL_STORAGE,
                    FOREGROUND_SERVICE}, 100);
            return true;
        }
        return false;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[3] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[4] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[5] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[6] == PackageManager.PERMISSION_GRANTED) {
                /*Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();*/

            }
        }

        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                files_getCamera();
            } else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
                ActivityCompat.requestPermissions(com.openinstitute.nuru.PostActivity.this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, REQUEST_IMAGE_CAPTURE);
                func_showAlerts(context, "Camera not available", "");
            }
        }
    }







    /*
     * ==============================================================================
     * RAGE CAMERA COLLECTION
     * ==============================================================================
     * */

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(com.openinstitute.nuru.PostActivity.this, new String[]{CAMERA, WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
    }

    public void asyncResponse(String response) {
        /*int num_response = Integer.parseInt(response);*/
        Log.d("postResponse", response);

        if (response.equals("Success")) { /*||  num_response > 0 */
            func_showAlerts(context, "Saved to server.", "");
        } else {
            func_showAlerts(context, "Save to server FAILED!", "warning");
        }
        //Log.d("asyncResponse_Transfer", response);
        //db.updateFormPostSync(session_id, form_category);
    }

    /*
     * This is the method responsible for image upload
     * We need the full image path and the name for the image in this method
     * */
    public void files_uploadMultipart(String f_path, String params, String f_type, String post_entry_id) {

        String name = params;
        //getting the actual path of the image
        String path = f_path; //imageUrl;

        String ff_type = (f_type.equals("3gpx")) ? "audio" : "image";

        //Uploading code
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            MultipartUploadRequest uploadRequest = new MultipartUploadRequest(this, uploadId, CONF_FILE_UPLOAD)
                    .addFileToUpload(path, ff_type) //Adding file
                    .addParameter("name", name) //Adding text parameter to the request
                    .addParameter("post_entry_id", post_entry_id)
                    .addParameter("user_id", user_email)
                    .setMaxRetries(3);

            // For Android > 8, we need to set an Channel to the UploadNotificationConfig.
            // So, here, we create the channel and set it to the MultipartUploadRequest
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
                NotificationChannel channel = new NotificationChannel("Upload", "Upload service", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);

                UploadNotificationConfig notificationConfig = new UploadNotificationConfig();
                notificationConfig.setNotificationChannelId("Upload");
                uploadRequest.setNotificationConfig(notificationConfig);

            } else {
                // If android < Oreo, just set a simple notification (or remove if you don't wanna any notification
                // Notification is mandatory for Android > 8
                uploadRequest.setNotificationConfig(new UploadNotificationConfig());
            }

            uploadRequest.startUpload(); //Starting the upload

            Toast.makeText(this, "Uploaded " + name, Toast.LENGTH_SHORT).show();

        } catch (Exception exc) {
            Toast.makeText(this, "NOT uploaded " + exc.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    public String files_uploadGetPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        Objects.requireNonNull(cursor).moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        Objects.requireNonNull(cursor).moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, CONF_FILE_UPLOAD,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        //Log.d("uploadBitmap_resp", String.valueOf(response.data));
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));

                            //Log.d("uploadBitmap", String.valueOf(obj));

                            Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            //Log.d("uploadBitmap_err", e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d("uploadBitmap_err", error.getMessage());
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError", "" + error.getMessage());
                    }
                }) {


            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("image", new VolleyMultipartRequest.DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
        Volley.newRequestQueue(this).getCache().clear();

    }

    public void permission_camera(int mediaType) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{CAMERA}, CAMERA_REQUEST_CODE);
            } else {
                if (mediaType == MEDIA_TYPE_IMAGE) {
                    files_getCamera();
                }
                if (mediaType == MEDIA_TYPE_VIDEO) {
                    files_getRecordVideo();
                }
            }
        } else {
            if (mediaType == MEDIA_TYPE_IMAGE) {
                files_getCamera();
            }
            if (mediaType == MEDIA_TYPE_VIDEO) {
                files_getRecordVideo();
            }
        }
    }

    public void files_getCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        takePictureIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = files_getResultMediaFile(MEDIA_TYPE_IMAGE, true);

            filePath = Objects.requireNonNull(photoFile).getAbsolutePath().trim();
            fileUri = FileProvider.getUriForFile(context,
                    getString(R.string.file_provider_authority),
                    photoFile);


            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(takePictureIntent, MEDIA_TYPE_IMAGE);
        }
    }

    public void files_getRecordVideo() {

        Intent recordVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        recordVideo.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        recordVideo.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        if (recordVideo.resolveActivity(getPackageManager()) != null) {

            File videoFile = files_getResultMediaFile(MEDIA_TYPE_VIDEO, true);

            filePath = Objects.requireNonNull(videoFile).getAbsolutePath();
            fileUri = FileProvider.getUriForFile(context,
                    getString(R.string.file_provider_authority),
                    videoFile);

            /*Log.d("videoURI", String.valueOf(fileUri));
            Log.d("videoPath", filePath);*/

            recordVideo.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 30);
            recordVideo.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            recordVideo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(recordVideo, MEDIA_TYPE_VIDEO);
        }
    }







    /*
     * ==============================================================================
     * END :: RAGE CAMERA COLLECTION
     * ==============================================================================
     * */

    public File files_test_getExternalStoragePath() {

        //File internalCardFile = null;
        String path = null;

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("nuru_images", Context.MODE_PRIVATE);

        if (files_testIsWritable(directory)) {
            path = directory.getAbsolutePath();
        }

        if (path != null) {
            res_nuru_folder = new File(path);
        }

        return res_nuru_folder;
    }

    /*@Override*/
    public void onGeolocationPermissionsShowPrompt(String origin, android.webkit.GeolocationPermissions.Callback callback) {
        /*Log.d("Location", "callback");*/
        callback.invoke(origin, true, false);
    }

    private void imageAdd_Relay(String response) {
        imageView_add(response, "3gp", 0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
