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
import sg.asmallmuseum.Domain.Values;
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

        db.collection(Values.USER).document(Values.USER_EMAIL).collection(Values.USER).document(user.getuEmail()).set(user);

    }

    public void addTempUser(User user) {

        db.collection(Values.USER).document(Values.USER_EMAIL).collection("TempUsers").document(user.getuEmail()).set(user);

    }

    @Override
    public void addUserPosting(String uEmail, String field, String id, Map<String, String> map){
        DocumentReference docRef = db.collection(Values.USER).document(Values.USER_EMAIL).collection(Values.USER)
                .document(uEmail).collection(field).document(id);
        docRef.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("ADD", "ASD");
            }
        });
    }

    @Override
    public void getUserPosting(String uEmail, String field) {
        CollectionReference colRef = db.collection(Values.USER).document(Values.USER_EMAIL).collection(Values.USER).document(uEmail).collection(field);
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<String> results = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Map<String, Object> map = document.getData();
                        if (map.get(Values.PATH) instanceof String){
                            results.add((String)map.get(Values.PATH));
                        }
                    }
                    userDbListener.onUserPostLoadComplete(results, 0);
                }
            }
        });
    }

    @Override
    public void deletePath(String uEmail, String field, String id) {
        DocumentReference docRef = db.collection(Values.USER).document(Values.USER_EMAIL).collection(Values.USER).document(uEmail).collection(field).document(id);
        docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                userDbListener.onPathDeleteComplete(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userDbListener.onPathDeleteComplete(false);
            }
        });
    }

    @Override
    public void exists(String email, String field, String artId) {
        DocumentReference docRef = db.collection(Values.USER).document(Values.USER_EMAIL).collection(Values.USER)
                .document(email).collection(field).document(artId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getData() != null){
                    userDbListener.onPostExists(true);
                }
                else {
                    userDbListener.onPostExists(false);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                userDbListener.onPostExists(false);
            }
        });
    }

    @Override
    public void getTempUser(String email) {

        DocumentReference docRef = db.collection(Values.USER).document(Values.USER_EMAIL).collection("TempUsers").document(email);

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

        DocumentReference docRef = db.collection(Values.USER).document(Values.USER_EMAIL).collection(Values.USER).document(email);

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
         db.collection(Values.USER).document(Values.USER_EMAIL).collection(Values.USER)
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
        DocumentReference documentReference = db.collection(Values.USER).document(Values.USER_EMAIL).collection(Values.USER).document(user.getuEmail());
        documentReference.update(Values.USER_VAL_FIRST, user.getuFirstName(), Values.USER_VAL_LAST, user.getuLastName(),
                                    Values.USER_VAL_BIRTH, user.getuBirth(), Values.USER_VAL_NICK, user.getuNick());
    }

    @Override
    public void deleteUser(String email) {
        db.collection(Values.USER).document(Values.USER_EMAIL).collection(Values.USER).document(email)
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
