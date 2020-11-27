# APIs

## Auth and User Management
For more in-depth technical specifics about Auth and User Management, refer to <UserManagement.Md>

### Firebase API
The Firebase API was used to authenticate users and perform management. The Firebase API was used for email & password signin and signup. It was also used together with the Facebook Login API and Google Authentication API to enable users to sign in using third-party providers.   
A Firebase project was created on console.firebase.google.com.   
The app was registered with Firebase.  
Authentication was enabled for email and password, Google, and Facebook.  
The Firebase dependency was added to our Android project. 

### Google Auth API
To enable Google signin, a series of steps were followed:
* Google Play Services was added to the app.
* A Google API Console project was configured for our current application.
* Configuration credentials were downloaded and added to the project. 
* OAuth 2.0 was setup to enable client authentication. 


### Facebook Login API
To enable Facebook signin, a series of steps were followed:
* A Facebook Developers account was created.
* A Facebook app was setup. 
* The Firebase project was configured to work with the Facebook app.
* The Facebook dependencies were added to Android. 
