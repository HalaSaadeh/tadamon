package com.example.tadamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class LoginWithEmailAndPassActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView emailTextView, passwordTextView;
    private Button signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_email_and_pass);
        mAuth = FirebaseAuth.getInstance();
        emailTextView = (TextView) findViewById(R.id.emailSignInField);
        passwordTextView = (TextView) findViewById(R.id.passwordSignInField);
        signIn = findViewById(R.id.signInButton);
        signIn.setOnClickListener(e->{signin();});

    }
    private void signin(){
        String email=emailTextView.getText().toString();
        String password = passwordTextView.getText().toString();
        if (validateEmail() && validatePassword()){
        // Use Firebase Auth to Signin with Email and Password
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, show success message and update SharedPreferences with the signed-in user's information
                            Toast.makeText(LoginWithEmailAndPassActivity.this, "Successfully logged in.",
                                    Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateSharedPreferences(user.getUid());

                            // Check if this is their first authorization with Firebase and act accordingly
                            boolean isNew = task.getResult().getAdditionalUserInfo().isNewUser();

                            if (!isNew){
                                // Go to homepage if not their first time
                                Intent startIntent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                                startActivity(startIntent);}
                            else{
                                // Start creating the profile
                                Intent startIntent = new Intent(getApplicationContext(), AccountCreationStep1Activity.class);
                                startActivity(startIntent);
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginWithEmailAndPassActivity.this, "Invalid email or password.",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }}
    private void updateSharedPreferences(String uID){
        // Once signed-in, update SharedPreferences to store the current user
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ID",uID);
        editor.putBoolean("thirdParty", false); // did not sign in with third party provider
        editor.apply();
    }
    private boolean validateEmail(){
        String email = emailTextView.getText().toString();
        if (email.length()==0) {
            emailTextView.setError("Field is empty");
            return false;
        }
        emailTextView.setError(null);
        return true;
    }
    private boolean validatePassword(){
        String password =passwordTextView.getText().toString();
        if (password.length()==0) {
            passwordTextView.setError("Field is empty");
            return false;
        }
        passwordTextView.setError(null);
        return true;
    }
}