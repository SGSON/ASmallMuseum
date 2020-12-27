package sg.asmallmuseum.presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.R;
import sg.asmallmuseum.logic.ArtworkManager;
import sg.asmallmuseum.logic.MenuAdapter;
import sg.asmallmuseum.logic.UserManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserManager userManager = new UserManager();
        ArtworkManager artworkManager = new ArtworkManager();

        Button button = (Button) findViewById(R.id.textas);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userManager.addNewUser("asd", "asd", "s", "s", "asd@ads","1313");
                artworkManager.addArtwork("Pictures", "asdqweqes", "qwe", "1-1--", "Wow", "WOWOWO");
                makeText("Success!!");
            }
        });
    }
    private void makeText(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public void onMainButtonPressed(View view) {
        Toast.makeText(this, "Pressed Main Button", Toast.LENGTH_LONG).show();
    }

    public void onMenuButtonPressed(View view) {
        Toast.makeText(this, "Pressed Menu Button", Toast.LENGTH_LONG).show();
        //showPopup(view);
        setMenuAdapter(view);
    }

    public void onBackButtonPressed(View view) {
        Toast.makeText(this, "Pressed Back Button", Toast.LENGTH_LONG).show();
    }

    private void setMenuAdapter(View view){
        ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.main_menu);
        layout.setVisibility(View.VISIBLE);

        MenuAdapter menuAdapter = new MenuAdapter();
        ListView list_menu = (ListView) findViewById(R.id.list_menu);
        list_menu.setAdapter(menuAdapter);

        list_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                makeText("Item NUmber: "+i);
            }
        });
    }

    public void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, popupMenu.getMenu());
        popupMenu.show();
    }

}