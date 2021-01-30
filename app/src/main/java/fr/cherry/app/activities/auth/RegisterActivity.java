package fr.cherry.app.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.logging.Logger;

import fr.cherry.app.Cherry;
import fr.cherry.app.R;
import fr.cherry.app.activities.HomeActivity;
import fr.cherry.app.models.UserModel;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button backbtn = findViewById(R.id.back_btn);
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        final Button registerbtn = findViewById(R.id.loginBtnA);
        final EditText  email = findViewById(R.id.email);
        email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        final EditText mdp1 = findViewById(R.id.mdp1);
        mdp1.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final EditText mdp2 = findViewById(R.id.mdp2);
        mdp2.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        registerbtn.setEnabled(false);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(RegisterActivity.isValidEmail(email.getText().toString())){
                    if(mdp1.getText().toString().equals(mdp2.getText().toString())){
                        registerbtn.setEnabled(true);
                    }
                }
            }
        });

        mdp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(RegisterActivity.isValidEmail(email.getText().toString())){
                    if(mdp1.getText().toString().equals(mdp2.getText().toString())){
                        registerbtn.setEnabled(true);
                    }
                }
            }
        });

        mdp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(RegisterActivity.isValidEmail(email.getText().toString())){
                    if(mdp1.getText().toString().equals(mdp2.getText().toString())){
                        registerbtn.setEnabled(true);
                    }
                }
            }
        });

        registerbtn.setOnClickListener(v -> Cherry.getInstance().getAuth().createUserWithEmailAndPassword(email.getText().toString(), mdp1.getText().toString())
                .addOnCompleteListener(RegisterActivity.this, task -> {
                    if (!task.isSuccessful()) {
                        if(task.getException() instanceof FirebaseNetworkException) {
                            Toast.makeText(RegisterActivity.this, "Vous devez etre connecté à internet",
                                    Toast.LENGTH_SHORT).show();
                        }else if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(RegisterActivity.this, "Adresse email déjà utilisée",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        UserModel u = new UserModel("", task.getResult().getUser().getUid(), email.getText().toString(), "", "", "");
                        Cherry.getInstance().getDb().collection("users").add(u);
                        RegisterActivity.this.startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                        RegisterActivity.this.finish();
                    }
                }));
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
