package xyz.asmallmuseum.android.presentation.ArtView;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import xyz.asmallmuseum.android.domain.Artwork;
import xyz.asmallmuseum.android.R;
import xyz.asmallmuseum.android.logic.ArtworkManager;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.UploadCompleteListener;

public class ArtViewReportFragment extends DialogFragment implements View.OnClickListener, UploadCompleteListener {

    private View view;
    private Artwork mArtwork;
    private ArtworkManager artworkManager;

    public ArtViewReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_art_view_report, container, false);

        Button mSubmit = (Button) view.findViewById(R.id.fragment_report_submit);
        mSubmit.setOnClickListener(this);

        artworkManager = new ArtworkManager();
        artworkManager.setUpLoadCompleteListener(this);

//        Bundle bundle = getArguments();
//        mArtwork = (Artwork) bundle.getSerializable("Artwork");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArtViewViewModel viewModel = new ViewModelProvider(requireActivity()).get(ArtViewViewModel.class);
        viewModel.getArtwork().observe(getViewLifecycleOwner(), new Observer<Artwork>() {
            @Override
            public void onChanged(Artwork artwork) {
                mArtwork = artwork;
            }
        });
    }

    private void report(){
        EditText mTitleView = (EditText) view.findViewById(R.id.fragment_report_title);
        EditText mDescView = (EditText) view.findViewById(R.id.fragment_report_desc);

        String mTitle = mTitleView.getText().toString();
        String mDesc = mDescView.getText().toString();

        Map<String, String> map = new HashMap<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        map.put("Title", mTitle);
        map.put("Desc", mDesc);
        map.put("ArtPath", mArtwork.getaID().getPath());
        map.put("User", user.getEmail());

        String currentTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date());
        artworkManager.uploadReport(map, mArtwork, currentTime);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.fragment_report_submit){
            report();
        }
    }

    @Override
    public void onUploadComplete(boolean status, String path, int result_code) {
        //show alert dialog
        if (status){
            Toast.makeText(getActivity(), "Thank you for reporting.", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        else {
            Toast.makeText(getActivity(), "Reporting failed.", Toast.LENGTH_SHORT).show();
        }
    }
}