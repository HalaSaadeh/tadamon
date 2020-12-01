package com.example.tadamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class SignupWithEmailAndPassActivity extends AppCompatActivity {


    private Button signup;
    private ImageButton backButton;
    private TextView emailTextView, passwordTextView, confirmPasswordTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_with_email_and_pass);
        mAuth = FirebaseAuth.getInstance();

        signup = findViewById(R.id.signUpButton);
        emailTextView = (TextView) findViewById(R.id.emailField);
        passwordTextView = (TextView) findViewById(R.id.passwordField);
        confirmPasswordTextView = (TextView) findViewById(R.id.confirmPasswordField);

        signup.setOnClickListener(e -> {
            signUpWithEmailAndPass();
        });

        backButton = findViewById(R.id.signUpWEAPBackButton);
        backButton.setOnClickListener(e -> {
            startActivity(new Intent(
                    getApplicationContext(), SignInActivity.class
            ));
        });

    }

    private void signUpWithEmailAndPass() {
        if (validateEmail() && validatePassword() && confirmPassword()) {
            String password = passwordTextView.getText().toString();
            String email = emailTextView.getText().toString();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(SignupWithEmailAndPassActivity.this, "Successfully signed up.",
                                        Toast.LENGTH_LONG).show();
                                Intent startIntent = new Intent(getApplicationContext(), SignInActivity.class);
                                startActivity(startIntent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignupWithEmailAndPassActivity.this, "Email already exists.",
                                        Toast.LENGTH_LONG).show();
                            }

                            // ...
                        }
                    });
        }
    }

    private boolean validatePassword() {
        String password = passwordTextView.getText().toString();
        if (password.length() == 0) {
            passwordTextView.setError("Field is empty");
            return false;
        }
        if (password.length() < 8) {
            passwordTextView.setError("Password must be at least 8 characters.");
            return false;
        }
        passwordTextView.setError(null);
        return true;
    }

    private boolean confirmPassword() {
        String confirmPass = confirmPasswordTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        if (confirmPass.length() == 0) {
            confirmPasswordTextView.setError("Field is empty");
            return false;
        }
        if (!confirmPass.equals(password)) {
            confirmPasswordTextView.setError("Passwords do not match");
            return false;
        }
        confirmPasswordTextView.setError(null);
        return true;
    }

    private boolean validateEmail() {
        String email = emailTextView.getText().toString();
        if (email.length() == 0) {
            emailTextView.setError("Field is empty");
            return false;
        }
        emailTextView.setError(null);
        return true;
    }
}