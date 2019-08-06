package com.jeremiahvaris.travelmantics;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminActivity extends AppCompatActivity {

    public static final String DATABASE_REFERENCE_PATH = "traveldeals";
    TextInputLayout titleTil;
    TextInputLayout priceTil;
    TextInputLayout descriptionTil;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        databaseReference = FirebaseDatabase.getInstance().getReference().child(DATABASE_REFERENCE_PATH);

        titleTil = findViewById(R.id.titleTil);
        descriptionTil = findViewById(R.id.descriptionTil);
        priceTil = findViewById(R.id.priceTil);

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
            saveDealToDatabase();
            cleanUi();
        }

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
    }

    private void saveDealToDatabase() {
        TravelDeal deals = new TravelDeal(
                descriptionTil.getEditText().getText().toString(),
                priceTil.getEditText().getText().toString(),
                titleTil.getEditText().getText().toString()
        );
        databaseReference.push().setValue(deals);
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

        return detailsAreValid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }


}
