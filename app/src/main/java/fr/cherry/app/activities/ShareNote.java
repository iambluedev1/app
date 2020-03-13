package fr.cherry.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import fr.cherry.app.Cherry;
import fr.cherry.app.R;
import fr.cherry.app.models.ListModel;

public class ShareNote extends AppCompatActivity {


    private ListModel note = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String from = intent.getStringExtra("from");

        if(from.equals("main"))
            note = Cherry.getInstance().getNotes().get(intent.getIntExtra("index", 0));
        else if(from.equals("edit_note"))
            note = Cherry.getInstance().getNotes().stream().filter(u -> u.getId() == intent.getIntExtra("id", 0)).findFirst().get();

        setContentView(R.layout.activity_share_note);
        ImageButton backBtn = findViewById(R.id.imageButton3);
        backBtn.setOnClickListener(v -> onBackPressed());

        final TextInputEditText email = findViewById(R.id.emailShare);
        Button btnShare = findViewById(R.id.btnShareWith);
        btnShare.setOnClickListener(v -> {
            String em = email.getText().toString();
            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            if(Cherry.getInstance().userExist(em)){
                Cherry.getInstance().shareNoteWith(em, note.getId());
                Snackbar.make(v, "La note a bien été partagée à ce compte", Snackbar.LENGTH_LONG)
                        .show();
            }else{
                Snackbar.make(v, "Cette adresse email n'est ratachée a aucun compte", Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = getIntent();
        String from = intent.getStringExtra("from");

        if(from.equals("main"))
            startActivity(new Intent(ShareNote.this, HomeActivity.class));
        else if(from.equals("edit_note")) {
            Intent newIntent = new Intent(ShareNote.this, EditNoteActivity.class);
            newIntent.putExtra("item", intent.getIntExtra("id", 0));
            newIntent.setFlags(newIntent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(newIntent);
        }
    }

}
