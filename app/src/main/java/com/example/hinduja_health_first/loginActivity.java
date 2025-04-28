package com.example.hinduja_health_first;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginActivity extends AppCompatActivity {

    private EditText loginEmail, loginPassword;
    private Button loginButton;
    private TextView createAccount;
    private DatabaseReference reference;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize SharedPreferences manager
        sharedPrefManager = SharedPrefManager.getInstance(this);

        // Check if user is already logged in
        if (sharedPrefManager.isLoggedIn()) {
            startActivity(new Intent(loginActivity.this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        reference = FirebaseDatabase.getInstance().getReference("users");
        createAccount = findViewById(R.id.createAccount);
        TextView forgotPassword = findViewById(R.id.text2);

        loginButton.setOnClickListener(v -> {
            String email = loginEmail.getText().toString();
            String password = loginPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(loginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot child : snapshot.getChildren()) {
                            User user = child.getValue(User.class);
                            if (user != null && user.password.equals(password)) {
                                // Save user data in SharedPreferences
                                sharedPrefManager.userLogin(user);

                                Toast.makeText(loginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(loginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }
                        }
                        Toast.makeText(loginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(loginActivity.this, "Email not registered", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(loginActivity.this, "Database Error", Toast.LENGTH_SHORT).show();
                }
            });
        });

        createAccount.setOnClickListener(v -> {
            Intent intent = new Intent(loginActivity.this, SignUp_activity.class);
            startActivity(intent);
        });

        forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(loginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        });
    }
}
