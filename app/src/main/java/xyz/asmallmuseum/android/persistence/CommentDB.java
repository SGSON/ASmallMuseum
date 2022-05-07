package xyz.asmallmuseum.android.persistence;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.asmallmuseum.android.Domain.Artwork;
import xyz.asmallmuseum.android.Domain.Comment;
import xyz.asmallmuseum.android.Domain.CommentPath;
import xyz.asmallmuseum.android.Domain.Values;
import xyz.asmallmuseum.android.presentation.CustomListenerInterfaces.CommentDBListener;

public class CommentDB implements CommentDBInterface {
    private FirebaseFirestore db;
    private CommentDBListener commentDBListener;

    public CommentDB(){

        db = FirebaseFirestore.getInstance();

    }

    @Override
    public void setDBListener(CommentDBListener commentDBListener) {
        this.commentDBListener = commentDBListener;
    }

    @Override
    public void addComment(Comment comment, String category, String type, String ref) {
        DocumentReference docRef = db.collection(Values.ART).document(category).collection(type).document(ref).collection(Values.USER_COMMENTS).document();
        String refs[] = docRef.getPath().split("/");
        comment.setCommIdx(refs[5]);
        comment.setPath(docRef.getPath());
//        CommentPath commentPath = new CommentPath();
//        commentPath.setPath(docRef.getPath());
        Map<String, String> map = new HashMap<>();
        map.put(Values.PATH, docRef.getPath());
        db.collection(Values.ART).document(category).collection(type).document(ref).collection(Values.USER_COMMENTS).document(refs[5]).set(comment);
        db.collection(Values.USER).document(Values.USER_EMAIL).collection(Values.USER).document(comment.getuEmail()).collection(Values.USER_COMMENTS).document(refs[5]).set(map);
    }

    @Override
    public void getComment(String category, String type, String path) {
        List<Comment> list = new ArrayList<>();

        db.collection(Values.ART).document(category).collection(type).document(path).collection(Values.USER_COMMENTS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("CommentDB", document.getId() + " => " + document.getData());
                                Comment comment = document.toObject(Comment.class);
                                list.add(comment);
                            }

                            if(list.isEmpty()) {
                                Comment comment1 = new Comment("null","null","null","null","null","null");
                                list.add(comment1);
                            }
                            commentDBListener.onCommentLoadComplete(list);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    @Override
    public void getRefreshComment(String category, String type, String path) {
        List<Comment> list = new ArrayList<>();

        db.collection(Values.ART).document(category).collection(type).document(path).collection(Values.USER_COMMENTS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("CommentDB", document.getId() + " => " + document.getData());
                                Comment comment = document.toObject(Comment.class);
                                list.add(comment);
                            }

                            if(list.isEmpty()) {
                                Comment comment1 = new Comment("null","null","null","null","null","null");
                                list.add(comment1);
                            }


                            Log.d("commentDB", "onCommentLoadComplete: " + list.get(0));
                            commentDBListener.onCommentLoadComplete(list);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });

    }


    @Override
    public void getCommentByUser(String email) {
        List<Comment> list = new ArrayList<>();

        db.collection(Values.USER_COMMENTS)
                .whereEqualTo(Values.USER_VAL_EMAIL, email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("CommentDB", document.getId() + " => " + document.getData());
                                Comment comment = document.toObject(Comment.class);
                                list.add(comment);
                            }
                            commentDBListener.onCommentByUserLoadComplete(list);
                        } else {
                            Log.d("CommentDB", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void updateComment(Comment comment) {
        String path[] = comment.getPath().split("/");
        String category = path[1];
        String type = path[2];
        String ref = path[3];
        String commIdx = comment.getCommIdx();
        DocumentReference doc = db.collection(Values.ART).document(category).collection(type).document(ref).collection(Values.USER_COMMENTS).document(commIdx);

        doc.update(Values.COMMENT_CONTENT, comment.getContent())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("CommUpdate", "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("CommUpdate", "Error updating document", e);
                    }
                });
    }

    @Override
    public void deleteComment(String category, String type, String ref, String commIdx, String uEmail) {

        db.collection(Values.ART).document(category).collection(type).document(ref).collection(Values.USER_COMMENTS).document(commIdx)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("CommentDB", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("CommentDB", "Error deleting document", e);
                    }
                });

        db.collection(Values.USER).document(Values.USER_EMAIL).collection(Values.USER).document(uEmail).collection(Values.USER_COMMENTS).document(commIdx)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("CommentDB", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("CommentDB", "Error deleting document", e);
                    }
                });
    }


}
