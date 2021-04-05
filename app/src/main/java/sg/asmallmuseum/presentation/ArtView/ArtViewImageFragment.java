package sg.asmallmuseum.presentation.ArtView;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import sg.asmallmuseum.R;


public class ArtViewImageFragment extends Fragment {//implements View.OnTouchListener {
    private ImageView mImage;
    private Uri uri;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private View view;
    private PhotoView photoView;
    private boolean zoomable;

    public ArtViewImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_art_image, container, false);

        photoView = (PhotoView) view.findViewById(R.id.fragment_image);
        photoView.setZoomable(zoomable);

        Glide.with(this).load(uri).into(photoView);
        
        return view;
    }

    public void setImage(Uri uri){
        this.uri = uri;
    }

    public void setImage(String uri){
        this.uri = Uri.parse(uri);
    }

    public String getUriAsString(){
        return uri.toString();
    }

    public Uri getUri(){
        return uri;
    }

    public void setZoomable(boolean zoom){
        zoomable = zoom;
    }

}