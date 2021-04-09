package sg.asmallmuseum.presentation.General;

import androidx.appcompat.app.AppCompatActivity;
import sg.asmallmuseum.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SupportActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        Button menu = (Button) findViewById(R.id.top_menu_button);
        menu.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back_button){
            finish();
        }
    }

}