package fr.cherry.app.models;

import java.util.List;

public class ListModel {

    private Integer id;
    private String title;
    private int type;
    private String date;
    private UserModel owner;
    private List<UserModel> shared;
    private String text = "";

    public ListModel(Integer id, String title, int type, UserModel owner, List<UserModel> shared){
        this.title = title;
        this.type = type;
        this.owner = owner;
        this.shared = shared;
        this.id = id;
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

    public int getType() {
        return type;
    }

    public List<UserModel> getShared() {
        return shared;
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addSharedWith(UserModel user){
        this.shared.add(user);
    }

    public boolean isSharingWith(String email){
        if(this.owner.getEmail().equals(email)) return true;
        return this.shared.stream().filter(user -> user.getEmail().equals(email)).count() > 0;
    }
}
