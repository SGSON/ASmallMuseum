package xyz.asmallmuseum.android.presentation.ArtUpload;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import xyz.asmallmuseum.android.domain.Artwork;
import xyz.asmallmuseum.android.domain.Messages.ArtAttachedError;
import xyz.asmallmuseum.android.domain.Messages.ArtDescError;
import xyz.asmallmuseum.android.domain.Messages.ArtTypeError;
import xyz.asmallmuseum.android.domain.Messages.ArtTitleError;
import xyz.asmallmuseum.android.domain.Messages.ArtCategoryError;
import xyz.asmallmuseum.android.domain.Messages.CustomException;
import xyz.asmallmuseum.android.domain.RequestCode;
import xyz.asmallmuseum.android.domain.User;
import xyz.asmallmuseum.android.domain.Values;
import xyz.asmallmuseum.android.R;
import xyz.asmallmuseum.android.logic.ArtworkManager;
import xyz.asmallmuseum.android.logic.UserManager;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.UploadCompleteListener;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.UserLoadListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ArtUploadPageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, UploadCompleteListener, UserLoadListener {
    private Spinner mCategory;
    private Spinner mType;
    private EditText mTitle;
    private EditText mDesc;
    private Button mPost;
    private Button mBack;
    private Button mAdd;
    private RecyclerView mAttached;
    private FirebaseAuth mAuth;
    private Map<String, String> map;
    private ArtworkManager manager;
    private List<Uri> mPathList;
    private List<String> mFileName;
    private ArtUploadAdapter adapter;
    private List<String> mExtensions;

    private FirebaseUser firebaseUser;
    private User mUser;
    private UserManager userManager;
    private ProgressDialog dialog;

    private ActivityResultLauncher<String> mContents = registerForActivityResult(new ActivityResultContracts.GetMultipleContents(), new ActivityResultCallback<List<Uri>>() {
        @Override
        public void onActivityResult(List<Uri> result) {
            Log.d("Result", result.toString());
            getFiles(result);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);

        mAuth = FirebaseAuth.getInstance();
        map = new HashMap<>();

        manager = new ArtworkManager();
        manager.setUpLoadCompleteListener(this);
        userManager = new UserManager();
        userManager.setUserLoadListener(this);

        mPathList = new ArrayList<>();
        mFileName = new ArrayList<>();
        mExtensions = new ArrayList<>();
        adapter = new ArtUploadAdapter(mPathList, mFileName);

        mCategory = (Spinner) findViewById(R.id.upload_category_spinner);
        mType = (Spinner) findViewById(R.id.upload_type_spinner);
        mTitle = (EditText) findViewById(R.id.upload_title);
        mDesc = (EditText) findViewById(R.id.upload_description);
        mPost = (Button) findViewById(R.id.upload_post);
        mBack = (Button) findViewById(R.id.upload_close_button);
        mAdd = (Button) findViewById(R.id.upload_image_add);
        mAttached = (RecyclerView) findViewById(R.id.upload_images);

        mPost.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mCategory.setOnItemSelectedListener(this);
        mType.setOnItemSelectedListener(this);

        mAttached.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAttached.setHorizontalScrollBarEnabled(true);
        mAttached.setAdapter(adapter);

        setCategorySpinner();
        setTypeSpinner("");

//        Intent intent = getIntent();
//        if (intent.hasExtra("Art") && intent.getSerializableExtra("Art") instanceof Artwork){
//            setArtData((Artwork)intent.getSerializableExtra("Art"));
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            firebaseUser = user;
            userManager.getUserInfo(firebaseUser.getEmail(), 0);
        }
    }

    private void setArtData(Artwork artwork){
        mTitle.setText(artwork.getaTitle());
        mCategory.setSelection(getCategoryIndex(artwork.getaCategory()));
        mType.setSelection(getTypeIndex(artwork.getaCategory(), artwork.getaType()));
        mDesc.setText(artwork.getaDesc());

        List<StorageReference> refs = manager.getArtImages(artwork.getaCategory(), artwork.getaFileLoc());
        refs.get(0).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

            }
        });
    }

    private int getCategoryIndex(String category){
        int result = 0;

        List<String> categories = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.arts_categories)));
        result = categories.indexOf(category);

        return result;
    }

    private int getTypeIndex(String category, String type){
        int id = getTypeId(category);
        List<String> types = new ArrayList<>(Arrays.asList(getResources().getStringArray(id)));
        return types.indexOf(type);
    }

    private int getTypeId(String category){
        int id;
        switch (category){
            case Values.ART_FINE:
                id = R.array.type_fine;
                break;
            case Values.ART_VISUAL:
                id =R.array.type_visual;
                break;
            case Values.ART_APPLIED:
                id = R.array.type_applied;
                break;
            case Values.ART_OTHERS:
                id = R.array.type_others;
                break;
            default:
                id = R.array.spinner_select;
        }
        return id;
    }

    @SuppressLint("IntentReset")
    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.upload_close_button){
            finish();
        }
        else if (id == R.id.upload_post){
            getTexts();
            uploadArt();
        }
        else if (id == R.id.upload_image_add){
            mContents.launch("image/*");
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
//            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), RequestCode.REQUEST_CODE);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id  = adapterView.getId();
        if (id == R.id.upload_category_spinner){
            String selected = adapterView.getItemAtPosition(i).toString();
            Log.d("Selected", selected);
            map.put(Values.ART_CATEGORY, selected);
            setTypeSpinner(selected);
        }
        else if (id == R.id.upload_type_spinner){
            String selected = adapterView.getItemAtPosition(i).toString();
            map.put(Values.ART_TYPE, selected);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //setGenreSpinner("");
    }

    private void setCategorySpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.arts_categories_spinner, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategory.setAdapter(adapter);
    }

    private void setTypeSpinner(String category){
        int id = getTypeId(category);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, id, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mType.setAdapter(adapter);
    }

    private void getTexts(){
        String title = mTitle.getText().toString();
        String desc = mDesc.getText().toString();

        map.put(Values.ART_TITLE, title);
        map.put(Values.ART_DESC, desc);
        //String genre = mGenre.getSelectedItem();
    }

    /***Upload a art***/
    private void uploadArt()  {
        try{
            manager.validateArt(mPathList, mExtensions, map.get(Values.ART_CATEGORY), map.get(Values.ART_TYPE), map.get(Values.ART_TITLE), mUser.getuEmail(), "2020-12-13", map.get(Values.ART_DESC));
            showAlertDialog().show();
        }
        catch (CustomException e){
            if (e instanceof ArtTitleError){
                mTitle.setError(e.getErrorMsg());
            }
            else if (e instanceof ArtCategoryError){
                TextView textView = (TextView) mCategory.getSelectedView();
                textView.setError(e.getErrorMsg());
                textView.setTextColor(Color.RED);
            }
            else if (e instanceof ArtTypeError){
                TextView textView = (TextView) mType.getSelectedView();
                textView.setError(e.getErrorMsg());
                textView.setTextColor(Color.RED);
            }
            else if (e instanceof ArtDescError){
                mDesc.setError(e.getErrorMsg());
            }
            else if (e instanceof ArtAttachedError){
                Toast.makeText(this, e.getErrorMsg(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onUploadComplete(boolean status, String path, int result_code) {
        //Toast.makeText(this, "Upload finished", Toast.LENGTH_SHORT).show();
        if(result_code == RequestCode.RESULT_UPLOAD_INFO_OK){
            userManager.updateUserPost(firebaseUser.getEmail(), Values.ART_POSTS, path);
        }
        else{
            dialog.dismiss();
            finish();
        }
    }

    /***End***/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RequestCode.REQUEST_CODE && resultCode == RESULT_OK && data != null){
            if (data.getClipData() != null){
                ClipData clip = data.getClipData();
                for (int i = 0 ; i < clip.getItemCount() ; i++){
                    Uri uri = clip.getItemAt(i).getUri();
                    updateList(uri);

                }
            }
            else{
                if (data.getData() != null){
                    Uri uri = data.getData();
                    updateList(uri);
                }
            }
            adapter.updateList(mPathList, mFileName);
        }
        else {
            Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateList(Uri uri){
        String[] paths = {MediaStore.Images.Media.DATA};

        String type = getContentResolver().getType(uri);
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, paths, null, null, null);
        cursor.moveToFirst();

        int cursorIndex = cursor.getColumnIndexOrThrow(paths[0]);
        String image = cursor.getString(cursorIndex);
        cursor.close();

        String[] extension = type.split("/");
        String[] file = image.split("/");

        mExtensions.add(extension[1]);
        mFileName.add(file[file.length-1]);
        mPathList.add(uri);
    }

    private Dialog showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.upload_message)
                .setPositiveButton(R.string.upload_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //before the uploading, please check the type and genre has been selected
                        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date());
                        manager.upLoadArt(mPathList, mExtensions, map.get(Values.ART_CATEGORY), map.get(Values.ART_TYPE), map.get(Values.ART_TITLE), mUser.getuNick(), currentDate, currentTime, map.get(Values.ART_DESC), mUser.getuEmail());
                        dialog = new ProgressDialog(ArtUploadPageActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        dialog.setMessage("UPLOADING..");
                        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                })
                .setNegativeButton(R.string.upload_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                 });
        return builder.create();
    }

    @Override
    public void userInfo(User user) {
        if (user != null){
            mUser = user;
        }
        else {
            Toast.makeText(this, "User Information Load Failed.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void getAllUser(List<String> list) {

    }

    private void getFiles(List<Uri> list) {
        List<File> files = new ArrayList<>();

        for (int i = 0 ; i < list.size() ; i++) {
            Uri uri = list.get(i);
            Log.d("URI "+i, uri.getPath());
            Log.d("URI "+i, Environment.getExternalStorageDirectory().toString());

            int startInd = uri.getPath().indexOf(Environment.getExternalStorageDirectory().toString());
            File file = new File(uri.getPath().substring(startInd));
            Log.d("file exists", file.exists()+" "+startInd);
            files.add(file);
        }
    }
}
