package xyz.asmallmuseum.android.presentation.General;

import androidx.appcompat.app.AppCompatActivity;
import xyz.asmallmuseum.android.R;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SupportActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        Button menu = (Button) findViewById(R.id.top_menu_button);
        Button back = (Button) findViewById(R.id.back_button);

        menu.setVisibility(View.INVISIBLE);
        back.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.back_button){
            finish();
        }
    }

}