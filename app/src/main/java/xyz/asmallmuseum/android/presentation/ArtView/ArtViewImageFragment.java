package xyz.asmallmuseum.android.presentation.ArtView;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import androidx.lifecycle.ViewModelProvider;
import xyz.asmallmuseum.android.R;


public class ArtViewImageFragment extends Fragment {//implements View.OnTouchListener {
    private ImageView mImage;
    private Uri uri;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private View view;
    private PhotoView photoView;
    private boolean zoomable;
    private int position;
    private ArtViewViewModel viewModel;

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
        position = getArguments().getInt("Position");

        photoView = (PhotoView) view.findViewById(R.id.fragment_image);
//        photoView.bringToFront();
//        photoView.setClickable(true);
//        photoView.setFocusableInTouchMode(true);
        photoView.setZoomable(zoomable);
//
//        if (!zoomable){
//            photoView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //Toast.makeText(getActivity(), "IN", Toast.LENGTH_SHORT).show();
//                    if (getActivity() instanceof ArtViewActivity && viewModel != null){
//                        ((ArtViewActivity) getActivity()).replaceFragment(ArtViewImageFragment.this);
//                        viewModel.setmCurrentPage(position);
//                    }
//                }
//            });
//        }

        Glide.with(this).load(uri).into(photoView);
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(ArtViewViewModel.class);
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