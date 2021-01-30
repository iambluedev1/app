package fr.cherry.app;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.cherry.app.models.ListModel;
import fr.cherry.app.models.UserModel;
import fr.cherry.app.utils.Callback;

public class Cherry {

    private List<ListModel> list;
    private UserModel authenticated;

    private static Cherry instance;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    static {
        instance = new Cherry();
    }

    public Cherry(){
        this.list = new ArrayList<>();
        /*this.list = new ArrayList<>(Arrays.asList(
                new ListModel(1,"Titre 1", 1, this.users.get(0), new ArrayList<UserModel>()),
                new ListModel(2, "Titrde 2", 1, this.users.get(1), new ArrayList<UserModel>()),
                new ListModel(3, "Titrdde 3", 0, this.users.get(2), new ArrayList<UserModel>()),
                new ListModel(4, "Titddde 3", 0, this.users.get(3), new ArrayList<UserModel>()),
                new ListModel(5, "Titre 3", 0, this.users.get(1), new ArrayList<UserModel>()),
                new ListModel(6, "Titsddre 3", 0, this.users.get(1), new ArrayList<UserModel>()),
                new ListModel(7, "Titdrsdfe 3", 0, this.users.get(1), new ArrayList<UserModel>()),
                new ListModel(8, "Titredf 3", 0, this.users.get(1), new ArrayList<UserModel>()),
                new ListModel(9, "Titdredf 3", 0, this.users.get(2), new ArrayList<UserModel>()),
                new ListModel(10, "Titde 3", 0, this.users.get(2), new ArrayList<UserModel>()),
                new ListModel(11, "Titddresdf 3", 0, this.users.get(3), new ArrayList<UserModel>()),
                new ListModel(12, "Titdre 3", 0, this.users.get(0), new ArrayList<UserModel>()),
                new ListModel(13, "Titddre 3sdf", 0, this.users.get(3), new ArrayList<UserModel>()),
                new ListModel(14, "Titre 3", 0, this.users.get(0), new ArrayList<UserModel>()),
                new ListModel(15, "Titsddre 3", 0, this.users.get(3), new ArrayList<UserModel>()),
                new ListModel(16, "Titdrsdfe 3", 0, this.users.get(1), new ArrayList<UserModel>()),
                new ListModel(17, "Titredf 3", 0, this.users.get(0), new ArrayList<UserModel>()),
                new ListModel(18, "Titdredf 3", 0, this.users.get(0), new ArrayList<UserModel>()),
                new ListModel(19, "Titde 3", 0, this.users.get(0), new ArrayList<UserModel>()),
                new ListModel(20, "Titddresdf 3", 0, this.users.get(1), new ArrayList<UserModel>()),
                new ListModel(21, "Titdre 3", 0, this.users.get(2), new ArrayList<UserModel>()),
                new ListModel(22, "Titddre 3sdf", 0, this.users.get(2), new ArrayList<UserModel>()),
                new ListModel(23, "Titdre 3", 0, this.users.get(3), new ArrayList<UserModel>(Arrays.asList(this.users.get(0))))
        ));
*/
        this.db = FirebaseFirestore.getInstance();
        this.auth = FirebaseAuth.getInstance();
    }

    public static Cherry getInstance(){
        return instance;
    }

    public void removeNote(int position){
        this.list.remove(position);
    }

    public List<ListModel> getNotes(String email){
        Log.d("sddqdqsdqd", "qsdqdqsdqsd");
        this.list.stream().forEach(u -> Log.d("--", u.getTitle()));
        return this.list.stream().filter(list -> list.isSharingWith(email)).collect(Collectors.toList());
    }

    public boolean userExist(String email){
        //return this.users.stream().filter(user -> user.getEmail().equals(email)).count() > 0;
        return false;
    }

    public void shareNoteWith(String email, String noteId){
        /*ListModel m = this.list.stream().filter(list -> list.getId() == noteId).findFirst().orElse(null);
        if(m != null){
            UserModel u = this.users.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
            if(u != null){
                m.addSharedWith(u);
            }
        }*/
    }

    public UserModel getAuthenticated() {
        return authenticated;
    }

    public ListModel createNote(String html){
        ListModel l = new ListModel("", "Untitled", 0, this.authenticated, new ArrayList<>());
        l.setText(html);

        Log.d("noooote", this.authenticated.getDocumentId());

        this.db.collection("notes").add(l).addOnCompleteListener(task -> {
            Log.d("add note", task.getResult().getId());
            l.setId(task.getResult().getId());
            list.add(l);
        });

        return l;
    }

    public void setAuthenticated(Callback<UserModel> cb) {
        db.collection("users").whereEqualTo("id", auth.getCurrentUser().getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot document = task.getResult();
                Log.d("aaaaa", document.getDocuments().get(0).toString() + " ");
                authenticated = UserModel.fromDocument(document.getDocuments().get(0));
                Log.d("user", authenticated.getEmail());
                Log.d("user", authenticated.getId());
                Log.d("user", authenticated.getLastName() + "");
                Log.d("user", authenticated.getSurname() + "");
                cb.call(authenticated);
            }
        });
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseFirestore getDb() {
        return db;
    }

    public void addNote(ListModel note){
        this.list.add(note);
        Log.d("========", this.list.size() + "");
    }

    public void setAuthenticated(UserModel model){
        this.authenticated = model;
    }


}
