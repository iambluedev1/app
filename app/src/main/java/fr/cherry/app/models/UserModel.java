package fr.cherry.app.models;

import com.google.firebase.firestore.DocumentSnapshot;
import fr.cherry.app.Cherry;

public class UserModel {

    private String id;
    private String documentId;
    private String email;
    private String lastname;
    private String surname;
    private String picture;

    public UserModel(){}

    public UserModel(String documentId, String id, String email, String lastname, String surname, String picture){
        this.documentId = documentId;
        this.id = id;
        this.email = email;
        this.lastname = lastname;
        this.surname = surname;
        this.picture = picture;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastname;
    }

    public String getSurname() {
        return surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void save(){
        Cherry.getInstance().getDb().collection("users").document(documentId)
                .update("email", email, "lastName", lastname, "surname", surname, "picture", picture);
    }

    public String getPicture() {
        return picture;
    }

    public static UserModel fromDocument(DocumentSnapshot d){
        return new UserModel(d.getId(), d.getString("id"), d.getString("email"), d.getString("lastName"), d.getString("surname"), d.getString("picture"));
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
