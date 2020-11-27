package com.example.tadamon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 1;
    private Button emailPassSignin, googleSignin;
    private LoginButton facebookSignin;
    private TextView newUser;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // If email and pass signin is selected
        emailPassSignin = findViewById(R.id.signInWithEmailAndPassButton);
        emailPassSignin.setOnClickListener(e->{
            Intent startIntent = new Intent(getApplicationContext(), LoginWithEmailAndPassActivity.class);
            startActivity(startIntent);});

        // Google Signin
        googleSignin = findViewById(R.id.signInWithGoogleButton);
        googleSignin.setOnClickListener(e->{signInWithGoogle();});

        // New user, go to signup page
        newUser = findViewById(R.id.newUserTextView);
        newUser.setOnClickListener(e -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });

        // Facebook Signin
        facebookSignin = findViewById(R.id.signInWithFacebookButton);
        facebookSignin.setOnClickListener(e->{signInWithFacebook();});
    }
    private void signInWithGoogle() {
        // Open Google Signin activity from API
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("in", "in");
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Log.d("in", "in again");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
                Log.d("in", "Signin google success");
            } catch (ApiException e) {
                // Google Sign In failed
                Log.d("in", "Signin google fail", e);
                }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(SignInActivity.this, "Successfully logged in.",
                                    Toast.LENGTH_LONG).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateSharedPreferences(user.getUid());
                            Log.d("in", "Signin firebase success");
                            // Go to homepage
                            Intent startIntent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                            startActivity(startIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("in", "Signin firebase fail", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed. Please try again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void updateSharedPreferences(String uID){
        // Once signed-in, update SharedPreferences to store the current user
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ID",uID);
        editor.apply();
    }

    private void signInWithFacebook(){
        mCallbackManager = CallbackManager.Factory.create();
        facebookSignin.setReadPermissions("email", "public_profile");
        facebookSignin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Facebook Login", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FB Login", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FB Login", "facebook:onError", error);

            }
        });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FB Login", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FB Login", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateSharedPreferences(user.getUid());
                            Log.d("in", "Signin firebase success");
                            // Go to homepage
                            Toast.makeText(SignInActivity.this, "Logged in with Facebook.",
                                    Toast.LENGTH_SHORT).show();
                            Intent startIntent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                            startActivity(startIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}