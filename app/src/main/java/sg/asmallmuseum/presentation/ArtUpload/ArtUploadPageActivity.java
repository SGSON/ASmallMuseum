package sg.asmallmuseum.presentation.ArtUpload;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.Domain.Messages.ArtAttachedError;
import sg.asmallmuseum.Domain.Messages.ArtDescError;
import sg.asmallmuseum.Domain.Messages.ArtGenreError;
import sg.asmallmuseum.Domain.Messages.ArtTitleError;
import sg.asmallmuseum.Domain.Messages.ArtTypeError;
import sg.asmallmuseum.Domain.Messages.CustomException;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.presentation.General.ManagerListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtUploadPageActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ManagerListener {
    private Spinner mType;
    private Spinner mGenre;
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

    private final int REQUEST_CODE = 3020;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);

        mAuth = FirebaseAuth.getInstance();
        map = new HashMap<>();

        manager = new ArtworkManager();
        manager.setListener(this);

        mPathList = new ArrayList<>();
        mFileName = new ArrayList<>();
        mExtensions = new ArrayList<>();
        adapter = new ArtUploadAdapter(mPathList, mFileName);

        mType = (Spinner) findViewById(R.id.upload_type_spinner);
        mGenre = (Spinner) findViewById(R.id.upload_genre_spinner);
        mTitle = (EditText) findViewById(R.id.upload_title);
        mDesc = (EditText) findViewById(R.id.upload_description);
        mPost = (Button) findViewById(R.id.upload_post);
        mBack = (Button) findViewById(R.id.upload_close_button);
        mAdd = (Button) findViewById(R.id.upload_image_add);
        mAttached = (RecyclerView) findViewById(R.id.upload_images);

        mPost.setOnClickListener(this);
        mBack.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mType.setOnItemSelectedListener(this);
        mGenre.setOnItemSelectedListener(this);

        mAttached.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mAttached.setHorizontalScrollBarEnabled(true);
        mAttached.setAdapter(adapter);

        setTypeSpinner();
        setGenreSpinner("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            //show warning message
        }
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
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, REQUEST_CODE);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id  = adapterView.getId();
        if (id == R.id.upload_type_spinner){
            String selected = adapterView.getItemAtPosition(i).toString();
            Log.d("Selected", selected);
            map.put("type", selected);
            setGenreSpinner(selected);
        }
        else if (id == R.id.upload_genre_spinner){
            String selected = adapterView.getItemAtPosition(i).toString();
            map.put("genre", selected);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //setGenreSpinner("");
    }

    private void setTypeSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mType.setAdapter(adapter);
    }

    private void setGenreSpinner(String type){
        int id;
        switch (type){
            case "Books":
                id = R.array.genre_book;
                break;
            case "Music":
                id =R.array.genre_music;
                break;
            case "Pictures":
                id = R.array.genre_picture;
                break;
            default:
                id = R.array.spinner_select;
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, id, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGenre.setAdapter(adapter);
    }

    private void getTexts(){
        String title = mTitle.getText().toString();
        String desc = mDesc.getText().toString();

        map.put("title", title);
        map.put("desc", desc);
        //String genre = mGenre.getSelectedItem();
    }

    /***Upload a art***/
    private void uploadArt()  {
        try{
            manager.validateArt(mPathList, mExtensions, map.get("type"), map.get("genre"), map.get("title"), "tempuser", "2020-12-13", map.get("desc"));
            showAlertDialog().show();
        }
        catch (CustomException e){
            if (e instanceof ArtTitleError){
                mTitle.setError(e.getErrorMsg());
            }
            else if (e instanceof ArtDescError){
                mDesc.setError(e.getErrorMsg());
            }
            else if (e instanceof ArtGenreError){
                TextView textView = (TextView) mGenre.getSelectedView();
                textView.setError(e.getErrorMsg());
                textView.setTextColor(Color.RED);
            }
            else if (e instanceof ArtTypeError){
                TextView textView = (TextView) mGenre.getSelectedView();
                textView.setError(e.getErrorMsg());
                textView.setTextColor(Color.RED);
            }
            else if (e instanceof ArtAttachedError){
                Toast.makeText(this, e.getErrorMsg(), Toast.LENGTH_SHORT);
            }
        }

    }

    @Override
    public void onDownloadCompleteListener(List<Artwork> artworks) {

    }

    @Override
    public void onUploadCompleteListener(boolean status) {
        Toast.makeText(this, "Upload finished", Toast.LENGTH_SHORT).show();
        finish();
    }
    /***End***/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String[] paths = {MediaStore.Images.Media.DATA};
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null){
            if (data.getClipData() != null){
                ClipData clip = data.getClipData();
                for (int i = 0 ; i < clip.getItemCount() ; i++){
                    Uri uri = clip.getItemAt(i).getUri();
                    updateList(uri, paths);

                }
            }
            /*else{
                if (data.getData() != null){
                    Uri uri = data.getData();
                    updateList(uri, paths);
                }
            }*/
            adapter.updateList();
        }
        else {
            Toast.makeText(this, "Connection Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateList(Uri uri, String[] paths){
        String type = getContentResolver().getType(uri);
        Cursor cursor = getContentResolver().query(uri, paths, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER+"DESC");

        cursor.moveToFirst();
        String image = cursor.getString(cursor.getColumnIndex(paths[0]));
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
                        manager.upLoadArt(mPathList, mExtensions, map.get("type"), map.get("genre"), map.get("title"), "tempuser", "2020-12-13", map.get("desc"));
                    }
                })
                .setNegativeButton(R.string.upload_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                 });
        return builder.create();
    }
}
