package com.example.hinduja_health_first;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.Source;

public class SignUp_activity extends AppCompatActivity {

    EditText inputName, inputEmail, inputPhone, inputPassword, inputConfirmPassword;
    Button signupButton;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputPhone = findViewById(R.id.inputPhone);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        signupButton = findViewById(R.id.signupButton);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        signupButton.setOnClickListener(v -> {
            String name = inputName.getText().toString();
            String email = inputEmail.getText().toString();
            String phone = inputPhone.getText().toString();
            String password = inputPassword.getText().toString();
            String confirmPassword = inputConfirmPassword.getText().toString();

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User(name, email, phone, password);
            FirebaseDatabase.getInstance().getReference("users")
            .child(phone).setValue(user)
                            .addOnSuccessListener(unused -> {
                                Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SignUp_activity.this, loginActivity.class));
                                finish();
                            })
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Failed to create account", Toast.LENGTH_SHORT).show());


        });
    }
}

