package sg.asmallmuseum.presentation.ArtView;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import sg.asmallmuseum.R;


public class ArtViewImageFragment extends Fragment {
    private ImageView mImage;
    private Uri uri;

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
}