package fr.cherry.app.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fr.cherry.app.R;
import fr.cherry.app.activities.HomeActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

    @Override
    public void onStart() {
        super.onStart();
        final Intent intentHome = new Intent(LoadingActivity.this, HomeActivity.class);
        final Intent intentMain = new Intent(LoadingActivity.this, MainActivity.class);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(intentMain);
            finish();
        }else{
            startActivity(intentHome);
            finish();
        }
    }
}
