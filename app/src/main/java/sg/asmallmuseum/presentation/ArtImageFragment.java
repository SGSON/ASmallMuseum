package sg.asmallmuseum.presentation;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import sg.asmallmuseum.R;


public class ArtImageFragment extends Fragment {
    private ImageView mImage;
    private Uri uri;

    public ArtImageFragment() {
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
        Glide.with(ArtImageFragment.this).load(uri).into(mImage);
        return view;
    }

    public void setImage(Uri uri){
        this.uri = uri;
    }
}