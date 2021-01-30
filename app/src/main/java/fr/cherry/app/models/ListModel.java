package fr.cherry.app.models;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import fr.cherry.app.Cherry;
import fr.cherry.app.utils.Callback;

public class ListModel {

    private String id;
    private String title;
    private int type;
    private String date;
    private UserModel owner;
    private List<UserModel> shared;
    private String text = "";
    private String ownerId;

    public ListModel(String id, String title, int type, UserModel owner, List<UserModel> shared){
        this.title = title;
        this.type = type;
        this.owner = owner;
        this.shared = shared;
        this.id = id;
        this.ownerId = owner.getDocumentId();
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle(){
        return this.title;
    }

    public UserModel getOwner() {
        return owner;
    }

    public String getOwnerId(){
        return this.owner.getDocumentId();
    }

    public int getType() {
        return type;
    }

    public List<UserModel> getShared() {
        return shared;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addSharedWith(UserModel user){
        this.shared.add(user);
    }

    public boolean isSharingWith(String email){
        if(this.owner.getEmail().equals(email)) return true;
        return this.shared.stream().filter(user -> user.getEmail().equals(email)).count() > 0;
    }

    public void save(){
        Log.d("ListModel", "save");
        //Cherry.getInstance().getDb().collection("notes").document(id).update("id", id, "ownerId", this.getOwnerId());
    }

    public static void fromDocument(DocumentSnapshot doc, Callback<ListModel> cb){
        Log.d("Note", doc.toString());
        Log.d("sdsfsdfdsf", doc.getString("ownerId"));
        Cherry.getInstance().getDb().collection("users").document(doc.getString("ownerId")).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    UserModel user = UserModel.fromDocument(task.getResult());
                    List<UserModel> owner = new ArrayList<>();
                    Log.d("from", user.getEmail());
                    cb.call(new ListModel(doc.getId(), doc.getString("title"), doc.getLong("type").intValue(), user, owner));
                }
            }
        });

    }
}
