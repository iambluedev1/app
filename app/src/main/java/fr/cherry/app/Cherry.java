package fr.cherry.app;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fr.cherry.app.models.ListModel;
import fr.cherry.app.models.UserModel;

public class Cherry {

    private List<ListModel> list;
    private List<UserModel> users;
    private UserModel authenticated;

    private static Cherry instance;

    static {
        instance = new Cherry();
    }

    public Cherry(){
        /*this.users = new ArrayList<>(Arrays.asList(
                new UserModel(1, "guillaumechalons@gmail.com", "monmdp", "Chalons", "Guillaume"),
                new UserModel(2, "monemail@gmail.com", "monmdp", "qsdqs", "qsdqsd"),
                new UserModel(3, "sdfsdf@gmail.com", "monmdp", "sdfsdf", "sdfsdfsdf"),
                new UserModel(4, "monesdfsdfsdfmail@gmail.com", "monmdp", "sdfsdf", "qsdsdfsdfqsd")
        ));*/

        this.list = new ArrayList<>(Arrays.asList(
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
    }

    public static Cherry getInstance(){
        return instance;
    }

    public void removeNote(int position){
        Log.d("sqd", "before " + this.list.size());
        Log.d("sqd", "removing " + position);
        this.list.remove(position);
        Log.d("sqd", "after " + this.list.size());
    }

    public List<ListModel> getNotes(String email){
        this.list.stream().forEach(u -> Log.d("--", u.getTitle()));
        return this.list.stream().filter(list -> list.isSharingWith(email)).collect(Collectors.toList());
    }

    public List<ListModel> getNotes() {
        return this.getNotes(authenticated.getEmail());
    }

    public boolean userExist(String email){
        return this.users.stream().filter(user -> user.getEmail().equals(email)).count() > 0;
    }

    public void shareNoteWith(String email, Integer noteId){
        ListModel m = this.list.stream().filter(list -> list.getId() == noteId).findFirst().orElse(null);
        if(m != null){
            UserModel u = this.users.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
            if(u != null){
                m.addSharedWith(u);
            }
        }
    }

    public UserModel getAuthenticated() {
        return authenticated;
    }

    public ListModel createNote(String html){
        ListModel l = new ListModel(-1, "Untitled", 0, this.authenticated, new ArrayList<>());
        l.setText(html);
        this.list.add(l);

        return l;
    }
}
