# Auto Complete Feature
## Overview 
During step 3 of account creation, the user must type their location into a text field.  
This location can be a city or district.  
As the user types, there is an **auto-complete** feature where suggestions are displayed, based on the local SQLite database.  

*Example: If the user types in "b", all cities whose names contain a b are displayed. Then, if the user proceeds to  type "e", all cities with "be" in them will be displayed.* 

## AutoCompleteTextView
Our auto-complete feature is based on the official Android Studio **AutoCompleteTextView** class.  (Android Documentation [here](https://developer.android.com/reference/android/widget/AutoCompleteTextView.html))  
We write our own class, **CustomAutoCompleteTextView.java**. This class is inherited from AutoCompleteTextView with its own customization to enable querying and requerying whenever the user types in a new letter.  

## CustomAutoCompleteTextChangedListener 
Our **CustomAutoCompeleteTextView** object will have a TextChangeListener. Again, we write our own class. This class is called **CustomAutoCompleteTextChangedListener.java**.  
This listener listens to changes made in the AutoCompleteTextView, i.e. if a new character is typed/erased.  
Its method, **onTextChanged()**, will be the one to call a new query from the SQLite database.  (Details: [SQLiteLocalDatabase.Md](SQLiteLocalDatabase.Md))

## After Selection
After a city is selected from the drop-down, based on a suggestion, the text field will display this city/location. This location will be part of this user's information, to be used throughout the application. 