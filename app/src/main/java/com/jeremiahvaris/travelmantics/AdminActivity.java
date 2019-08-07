package com.jeremiahvaris.travelmantics;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AdminActivity extends AppCompatActivity {

    public static final String DATABASE_REFERENCE_PATH = "traveldeals";
    private static final int SELECT_IMAGE_REQUEST = 345;
    private static final String DEALS_PICTURES_PATH = "deals_pictures";
    TextInputLayout titleTil;
    TextInputLayout priceTil;
    TextInputLayout descriptionTil;
    Button button;
    ImageView imageView;
    private DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    TravelDeal deal;
    private String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(DATABASE_REFERENCE_PATH);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child(DEALS_PICTURES_PATH);

        titleTil = findViewById(R.id.titleTil);
        descriptionTil = findViewById(R.id.descriptionTil);
        priceTil = findViewById(R.id.priceTil);
        imageView = findViewById(R.id.imageView);
        button = findViewById(R.id.select_image_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromStorage();
            }
        });

    }

    private void getImageFromStorage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
//                intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        startActivityForResult(intent, SELECT_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_REQUEST && resultCode == RESULT_OK) {
            final Uri imageUri = data.getData();
            final StorageReference imageReference = storageReference.child(imageUri.getLastPathSegment());
            imageReference.putFile(imageUri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUrl = uri.toString();
                            showImage(imageUrl);
                        }
                    });

                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void showImage(String url) {
        if (url != null && !url.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get()
                    .load(url)
                    .resize(width, width * 2 / 3)
                    .centerCrop()
                    .into(imageView);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.save_menu: {
                initiateSave();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initiateSave() {
        if (detailsAreValid()) {
            clearErrors();
            getDealDetails();
            saveDealToDatabase();
            cleanUi();
        }

    }

    private void getDealDetails() {
        deal = new TravelDeal(
                descriptionTil.getEditText().getText().toString(),
                priceTil.getEditText().getText().toString(),
                titleTil.getEditText().getText().toString(),
                imageUrl
        );
    }

    private void clearErrors() {
        titleTil.setError("");
        priceTil.setError("");
        descriptionTil.setError("");
    }

    private void cleanUi() {
        titleTil.getEditText().setText("");
        priceTil.getEditText().setText("");
        descriptionTil.getEditText().setText("");
        imageUrl = "";
        imageView.setImageResource(R.drawable.image_placeholder);
    }

    private void saveDealToDatabase() {
        databaseReference.push().setValue(deal);
    }

    private boolean detailsAreValid() {
        boolean detailsAreValid = true;
        if (titleTil.getEditText().getText().toString().isEmpty()) {
            detailsAreValid = false;
            titleTil.setError("Please enter a title.");
        }
        if (priceTil.getEditText().getText().toString().isEmpty()) {
            detailsAreValid = false;
            priceTil.setError("Please enter a valid Price");
        }
        if (descriptionTil.getEditText().getText().toString().isEmpty()) {
            detailsAreValid = false;
            descriptionTil.setError("Please enter a description");
        }
        if (imageUrl == null || imageUrl.isEmpty()) {

            if (detailsAreValid)
                Toast.makeText(this, "Please select an image.", Toast.LENGTH_LONG).show();
            detailsAreValid = false;
        }

        return detailsAreValid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }


}
