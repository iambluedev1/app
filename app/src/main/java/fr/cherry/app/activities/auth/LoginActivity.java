package fr.cherry.app.activities.auth;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fr.cherry.app.Cherry;
import fr.cherry.app.R;
import fr.cherry.app.activities.HomeActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button backbtn = findViewById(R.id.back_btn);
        backbtn.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        final EditText email = findViewById(R.id.mdp1);
        email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        final EditText mdp = findViewById(R.id.mdp);
        mdp.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);

        TextView mdpOublie = findViewById(R.id.mdpOublie);
        mdpOublie.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        final Button loginBtnA = findViewById(R.id.loginBtnA);
        loginBtnA.setEnabled(false);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(RegisterActivity.isValidEmail(email.getText().toString())){
                    if(mdp.getText().length() > 0)
                        loginBtnA.setEnabled(true);
                }
            }
        });

        mdp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(RegisterActivity.isValidEmail(email.getText().toString())){
                    if(mdp.getText().length() > 0)
                        loginBtnA.setEnabled(true);
                }
            }
        });

        loginBtnA.setOnClickListener(v -> Cherry.getInstance().getAuth().signInWithEmailAndPassword(email.getText().toString(), mdp.getText().toString())
            .addOnCompleteListener(LoginActivity.this, task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = Cherry.getInstance().getAuth().getCurrentUser();
                    Cherry.getInstance().setAuthenticated(u -> {
                        Log.d("authenticated", u.getEmail());
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        LoginActivity.this.finish();
                    });
                } else {
                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }));
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
