package fr.cherry.app.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.cherry.app.Cherry;
import fr.cherry.app.R;
import fr.cherry.app.activities.auth.LoadingActivity;
import fr.cherry.app.activities.auth.MainActivity;

public class MyAccountActivity extends AppCompatActivity {

    ProgressBar pg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnChangeAvatar = findViewById(R.id.btnOpenGallery);
        btnChangeAvatar.setOnClickListener(v -> {
            checkPermission(new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
        });

        pg = findViewById(R.id.progressBar);
        pg.setVisibility(View.INVISIBLE);

        TextInputEditText prenom = findViewById(R.id.prenomText);
        prenom.setInputType(InputType.TYPE_CLASS_TEXT);

        TextInputEditText nom = findViewById(R.id.nomText);
        nom.setInputType(InputType.TYPE_CLASS_TEXT);

        TextInputEditText email = findViewById(R.id.emailText);
        email.setInputType(InputType.TYPE_CLASS_TEXT);

        prenom.setText(Cherry.getInstance().getAuthenticated().getSurname());
        nom.setText(Cherry.getInstance().getAuthenticated().getLastName());
        email.setText(Cherry.getInstance().getAuthenticated().getEmail());

        Button savePerso = findViewById(R.id.buttonSaveAccount);
        savePerso.setOnClickListener(v -> {
            Cherry.getInstance().getAuthenticated().setEmail(email.getText().toString());
            Cherry.getInstance().getAuthenticated().setLastName(nom.getText().toString());
            Cherry.getInstance().getAuthenticated().setSurname(prenom.getText().toString());
            Cherry.getInstance().getAuthenticated().save();

            Toast.makeText(this, "Profil mis a jour", Toast.LENGTH_SHORT);
        });

        ImageView imageView = findViewById(R.id.imgView);
        if(Cherry.getInstance().getAuthenticated().getPicture() != null){
           try {
               StorageReference mStorage = FirebaseStorage.getInstance().getReference();
               mStorage.child(Cherry.getInstance().getAuthenticated().getPicture()).getDownloadUrl().addOnSuccessListener(uri ->
                       Picasso.get().load(uri.toString()).into(imageView)
               ).addOnFailureListener(exception ->
                       imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.cartoon_man_in_love))
               );
           }catch(Exception e){
               imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.cartoon_man_in_love));
           }
        }
        else
            imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.cartoon_man_in_love));
    }

    private void selectAvatar(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = findViewById(R.id.imgView);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            uploadImageToFireBase(selectedImage);
        }
    }

    public void checkPermission(String[] permissions)
    {
        List<String> tmp = new ArrayList<>();
        for(String permision : permissions){
            if (ContextCompat.checkSelfPermission(MyAccountActivity.this, permision) == PackageManager.PERMISSION_DENIED) {
                tmp.add(permision);
            }
        }

        if(tmp.size() == 0) selectAvatar();
        else
        ActivityCompat
                .requestPermissions(
                        MyAccountActivity.this,
                        tmp.toArray(new String[]{}),
                        2);
    }

    private void uploadImageToFireBase(Uri img) {
        String fileName = "avatar/" + System.currentTimeMillis() + ".jpg";
        StorageReference sr = FirebaseStorage.getInstance().getReference(fileName);

        if(img != null){
            pg.setVisibility(View.VISIBLE);
            sr.putFile(img)
                    .addOnSuccessListener(taskSnapshot -> {
                        pg.setVisibility(View.GONE);
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if(user != null){
                            UserProfileChangeRequest request = new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(taskSnapshot.getMetadata().getReference().getDownloadUrl().toString())).build();
                            user.updateProfile(request)
                                    .addOnCompleteListener(task -> Toast.makeText(MyAccountActivity.this, "Avatar uploadé", Toast.LENGTH_SHORT));
                            Cherry.getInstance().getAuthenticated().setPicture(fileName);
                            Cherry.getInstance().getAuthenticated().save();

                            Toast.makeText(this, "Profil mis a jour", Toast.LENGTH_SHORT);
                        }
                    })
                    .addOnFailureListener(e -> pg.setVisibility(View.GONE));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == 2) {
            checkPermission(new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuId = item.getItemId();
        if(menuId == R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MyAccountActivity.this, MainActivity.class));
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
