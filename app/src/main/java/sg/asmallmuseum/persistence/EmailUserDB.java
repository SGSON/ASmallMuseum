package sg.asmallmuseum.persistence;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import sg.asmallmuseum.Domain.User;
import sg.asmallmuseum.logic.UserDBListener;

public class EmailUserDB implements UserDBInterface {

    private UserDBListener userDbListener;
    private FirebaseFirestore db;
    public EmailUserDB(){

        db = FirebaseFirestore.getInstance();

    }

    @Override
    public void setDBListener(UserDBListener userDbListener) {
        this.userDbListener = userDbListener;
    }

    @Override
    public void addUser(User user) {

        db.collection("Users").document("eMailUser").collection("Users").document(user.getuEmail()).set(user);

    }

    public void addTempUser(User user) {

        db.collection("Users").document("eMailUser").collection("TempUsers").document(user.getuEmail()).set(user);

    }

    @Override
    public void addUserPosting(String uEmail, String field, String id, Map<String, String> map){
        DocumentReference docRef = db.collection("Users").document("eMailUser").collection("Users")
                .document(uEmail).collection(field).document(id);
        docRef.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("ADD", "ASD");
            }
        });
    }

    @Override
    public void getTempUser(String email) {

        DocumentReference docRef = db.collection("Users").document("eMailUser").collection("TempUsers").document(email);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("getTempUser", documentSnapshot.getId()+" "+documentSnapshot.getString("uFirstName")+"  "+ documentSnapshot.getString("uLastName"));

                List<String> list = new ArrayList<>();
                User user = documentSnapshot.toObject(User.class);

                if(user != null){
                    list.add(user.getuEmail());
                    list.add(user.getuNick());
                    list.add(user.getuLastName());
                    list.add(user.getuFirstName());
                    list.add(user.getuBirth());
                }else if(user == null){
                    ;
                }
                userDbListener.onUserLoadComplete(user, 0);
            }

        });

    }

    @Override
    public void getUser(String email, int request_code) {
//        if (task is successful)
//            then shows exist alert
//        else
//            pass

        DocumentReference docRef = db.collection("Users").document("eMailUser").collection("Users").document(email);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = null;
                if (task.getResult() != null){
                    List<String> list = new ArrayList<>();
                    user = task.getResult().toObject(User.class);
                }
                userDbListener.onUserLoadComplete(user, request_code);
            }
        });
    }

     @Override
     public void getAllUser(List<String> list){
        List<String> userList = new ArrayList<>();
        db.collection("Users").document("eMailUser").collection("Users")
              .get()
              .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if (task.isSuccessful()) {
                          for (QueryDocumentSnapshot document : task.getResult()) {
                              User user = document.toObject(User.class);
                              userList.add(user.getuEmail());
                          }
                          userDbListener.onAllUserLoadComplete(list);
                      } else {
                          Log.d("getAllUser", "Error getting documents: ", task.getException());
                      }
                  }
              });
    }

    @Override
    public void updateUser(User user) {
        DocumentReference documentReference = db.collection("Users").document("eMailUser").collection("Users").document(user.getuEmail());
        documentReference.update("uFirstName", user.getuFirstName(), "uLastName", user.getuLastName(),
                                    "uBirth", user.getuBirth(), "uNick", user.getuNick());
    }

    @Override
    public void deleteUser(String email) {
        db.collection("Users").document("eMailUser").collection("Users").document(email)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("emailDBUserDelete", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("emailDBUserDelete", "Error deleting document", e);
                    }
                });
    }



}
