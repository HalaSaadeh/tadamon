package com.example.tadamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
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

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LoggedIn", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateSharedPreferences(user.getUid());
                            // Store in SharedPreferences
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("NotLoggedIn", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginWithEmailAndPassActivity.this, "Invalid email or password.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
    private void updateSharedPreferences(String uID){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ID",uID);
        editor.apply();
    }
}