# User Management 

## Firebase Authentication
The Firebase Authentication service is used to handle user management for this application.  
Users can signin or signup using one of three ways: 
* Email and password
* Google
* Facbeook

### Signup
#### Signup with Email and Password
All TextViews are validated to ensure that the user does not attempt to sign up using empty fields, does not attempt to use a password smaller than 8 characters, and does not submit mismatching passwords.  
After validation, the Firebase Auth instance is used to signup with the given email and password, as long as the email does not exist.

### Signin
#### Signin with Email and Password
All TextViews are validated to ensure that the user does not attempt to sign in using empty fields.  
After validation, the Firebase Auth instance is used to signin with the given email and password, as long as the password is correct. 

#### Signin with Google
The Firebase SDK and Google Auth API were used. Refer to <APIs.Md>  
We configure GoogleSignInOptions and request an ID token. 
To sign in with Google, a GoogleSignIn intent is created to take the user to where the Google signin happens.  
After a user successfully signs in with Google, the ID token from the GoogleSignInAccount object is taken and exchanged it for a Firebase credential, and authenticated with Firebase using the Firebase credential. 

#### Signin with Facebook
The Facebook SDK was used and added to the Project. Refer to <APIs.Md>  
We use a LoginButton object to request permissions from Facebook. The permissions we choose to request include viewing the public profile as well as the person's email which is information needed to authenticate a user.  
After a user successfully signs in, we get an access token for the signed-in user and exchange it for a Firebase credential, and authenticate with Firebase using the Firebase credential. 

### After Authentication
After a user signs in for the first time, a new user account is created and linked to the credentials—that is, the user name and password, phone number, or auth provider information—the user signed in with.  
This new account is stored as part of the Firebase project, and can be used to identify the user across the app, no matter which of the providers the user signed in with.  
We can get the user's basic profile information from the FirebaseUser object created.  

### Signout
When the user attempts to sign out, a pop-up message appears to make sure that the user really wants to sign out.   
If yes, the Firebase Auth instance will be used to sign out and return the app to the Signin page.  

## SharedPreferences 
* Upon signin, a single key-value pair will be stored in SharedPreferences. This key-value pair will save the userID for the authenticated user. 
* Upon signout, the SharedPreferences will be cleared.
* Upon app startup, the SharedPreferences are checked. If empty, the signin page will show up. If not empty, i.e. someone is already signedin, the homepage directly shows up.