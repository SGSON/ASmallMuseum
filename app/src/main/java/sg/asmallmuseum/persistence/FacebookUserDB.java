package sg.asmallmuseum.persistence;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.logic.DBListener;
import sg.asmallmuseum.presentation.UserInformInterface;

public class FacebookUserDB implements UserDBInterface {
    private DBListener dbListener;
    private FirebaseFirestore db;

    public FacebookUserDB(){

        db = FirebaseFirestore.getInstance();

    }

    @Override
    public void setDBListener(DBListener dbListener) {
        this.dbListener = dbListener;
    }

    @Override
    public void addUser(User user) {
        db.collection("Users").document("FacebookUser").collection("Users").document(user.getuEmail()).set(user);

    }

    @Override
    public void getUser(String email) {
        DocumentReference docRef = db.collection("Users").document("FacebookUser").collection("Users").document(email);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("getUser", documentSnapshot.getId()+" "+documentSnapshot.getString("uFirstName"));
                List<String> list = new ArrayList<>();
                User user = documentSnapshot.toObject(User.class);

                list.add("facebook");
                list.add("sType");
                list.add("loop");
                if(user != null){
                    list.add(user.getuEmail());
                    list.add(user.getuNick());
                    list.add(user.getuLastName());
                    list.add(user.getuFirstName());
                    list.add(user.getuBirth());
                }else if(user == null){
                    list.add("null");
                }
                dbListener.setAllUserListener(list);
            }

        });
    }

    @Override
    public void getTempUser(String email) {
        ;
    }

    @Override
    public void getAllUser(List<String> list) {

        db.collection("Users").document("FacebookUser").collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            list.set(1,"facebookDB");

                            StringBuilder sb = new StringBuilder();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.getId());
                                sb.append(document.getId()+", ");
                            }

                            Log.d("getAllFacebookUser", sb.toString());
                            dbListener.setAllUserListener(list);

                        } else {
                            Log.d("getAllUser", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void updateUser() {

    }

    @Override
    public void deleteUser(String email) {
        db.collection("Users").document("FacebookUser").collection("Users").document(email)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("facebookDBUserDelete", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("facebookDBUserDelete", "Error deleting document", e);
                    }
                });
    }


}
