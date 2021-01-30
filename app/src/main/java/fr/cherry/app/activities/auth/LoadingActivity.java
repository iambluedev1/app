package fr.cherry.app.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.List;
import java.util.concurrent.Callable;

import fr.cherry.app.Cherry;
import fr.cherry.app.R;
import fr.cherry.app.activities.HomeActivity;
import fr.cherry.app.models.ListModel;
import fr.cherry.app.models.UserModel;
import fr.cherry.app.utils.Callback;

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
            Cherry.getInstance().setAuthenticated(user -> {
                Log.d("authenticated", user.getEmail());
                Cherry.getInstance().setAuthenticated(user);
                Cherry.getInstance().getDb().collection("notes").whereEqualTo("owner.id", user.getId()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        for(DocumentSnapshot doc : documents){
                            Log.d("before", doc.toString());
                            ListModel.fromDocument(doc, new Callback<ListModel>() {
                                @Override
                                public void call(ListModel listModel) {
                                    Cherry.getInstance().addNote(listModel);
                                }
                            });
                        }
                        startActivity(intentHome);
                        finish();
                    }
                });
            });
        }
    }
}
