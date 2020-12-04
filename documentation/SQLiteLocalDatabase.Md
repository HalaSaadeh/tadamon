# SQLite Local Database 

## Overview

Other than our cloud-hosted Firebase Cloud Firestore database that we use for our dynamic data, we also use an SQLite database that gets stored locally and packaged with the application.  
This data is static. It will not be changing in real time, so it does not need to be hosted on the cloud. 
This database contains **one table** and this table contains the world cities and their locations.  
We exploit this database in Step 3 of creating a user profile.  
When the user enters their location, there is a predictive text that shows up and suggests cities and locations. More details about how the predictive text and auto-complete works can be found in [AutoCompleteFeature.Md](SQLiteLocalDatabase.Md)

## Dataset

Our dataset contains records of world cities and their locations.   
We initially get a csv version of the dataset from the following [Github repository](https://github.com/teo029/World-countries-states-and-cities).  
Then, these cities are imported into an SQLite database.  
   
The database contains one table: **worldcities**. The table has the following fields:
* name: name of the city
* country: country in which the city is located in
* subcountry: district or state in which the city is located in  
* priority: given that this is a Lebanese app, priority for Lebanese cities will be 1 and the others will have a priority of 2; this way, Lebanese cities get returned first

## City.java Class
We create our own City object class. This class will have variables for the name, country, and subcountry. It will also have a toString() method that prints out *city, subcountry, country*.   
As we query from the database, we will be later creating City objects and keeping them in a List<City>.

## DatabaseHandler.java Class
The **DatabaseHandler** is the class we use for all things database. It will extend **SQLiteOpenHelper** which is Android's standard library for SQLite.  

### Creating and Copying the Database
We save the database as *data.sqlite* and add it to our *assets* folder. However, this *does not* mean we can query from it.  
To query from the database, we must first copy it locally on the user's phone into their **databases** directory.  
  
What we do is the following: 
* Check if the database is copied and cached locally 
* If yes, move on to the queries and start querying it
* If no, perform a database copy from the assets folder into this databases directory.
* This copying is done using InputStreams and OutputStreams in a method called *copyDatabase()*
  
In other words, this is usually done only once on a user's device, the very first time they need to query this database. Other times, the query will refer to the caches. 

## Querying the Database
There is no adding of new data or updating. We only want to query cities whenever the user is typing into the locations text field during Account Creation Step 3.  
We perfom querying in the *read()* function.  
If the user has typed "be" into the location text field, our query will look like the following:   
**SELECT * FROM worldcities WHERE name LIKE "%be%" OR country LIKE "%be" OR subcountry LIKE "%be%" ORDER BY priority ASC**  
We get any city whose name, country, or subcountry matches the search term.  
We order the results by priority.  

Executing an SQL query in Java returns a Cursor object. This cursor will be looping through the returned records from the database.  
We will extract these records into City objects, store these objects in a List, and use this List to display them in the dropdown of the AutoCompleteTextView. 