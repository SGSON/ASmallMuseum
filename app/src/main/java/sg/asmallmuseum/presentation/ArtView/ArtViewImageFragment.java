package sg.asmallmuseum.presentation.ArtView;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import sg.asmallmuseum.R;


public class ArtViewImageFragment extends Fragment implements View.OnTouchListener {
    private ImageView mImage;
    private Uri uri;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

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
        View view = inflater.inflate(R.layout.fragment_art_image, container, false);
        mImage = (ImageView) view.findViewById(R.id.fragment_image);
        Glide.with(ArtViewImageFragment.this).load(uri).into(mImage);

        mScaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        mImage.setOnTouchListener(this);

        
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

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mScaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            mScaleFactor = Math.max(0.f, Math.min(mScaleFactor, 10.0f));

            mImage.setScaleX(mScaleFactor);
            mImage.setScaleY(mScaleFactor);
            return true;
        }
    }
}