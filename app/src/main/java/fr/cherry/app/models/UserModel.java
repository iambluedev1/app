package fr.cherry.app.models;

public class UserModel {

    private String id;
    private String email;
    private String lastname;
    private String surname;

    public UserModel(){}

    public UserModel(String id, String email, String lastname, String surname){
        this.id = id;
        this.email = email;
        this.lastname = lastname;
        this.surname = surname;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLastname() {
        return lastname;
    }

    public String getSurname() {
        return surname;
    }
}
