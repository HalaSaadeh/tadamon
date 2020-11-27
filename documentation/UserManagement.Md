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

### Signout
When the user attempts to sign out, a pop-up message appears to make sure that the user really wants to sign out.   
If yes, the Firebase Auth instance will be used to sign out and return the app to the Signin page.  

## SharedPreferences 
* Upon signin, a single key-value pair will be stored in SharedPreferences. This key-value pair will save the userID for the authenticated user. 
* Upon signout, the SharedPreferences will be cleared.
* Upon app startup, the SharedPreferences are checked. If empty, the signin page will show up. If not empty, i.e. someone is already signedin, the homepage directly shows up.