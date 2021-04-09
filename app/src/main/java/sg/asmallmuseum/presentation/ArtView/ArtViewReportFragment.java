package sg.asmallmuseum.presentation.ArtView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import sg.asmallmuseum.Domain.Artwork;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.presentation.CustomListenerInterfaces.UploadCompleteListener;

public class ArtViewReportFragment extends Fragment implements View.OnClickListener, UploadCompleteListener {

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

        map.put("Title", mTitle);
        map.put("Desc", mDesc);
        map.put("ArtPath", mArtwork.getaID().getPath());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        artworkManager.uploadReport(map, mArtwork, user.getIdToken(false).toString());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (status){
            builder.setMessage("Thank you for reporting.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //remove dialog.
                        }
                    });
        }
        else {
            builder.setMessage("Upload failed.");
        }
        builder.create();
    }
}