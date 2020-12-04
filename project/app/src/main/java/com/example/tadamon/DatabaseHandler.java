package com.example.tadamon;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
    // Database name
    protected static final String CITIES_DATABASE_NAME = "data.sqlite";
    // Database version
    private static final int DATABASE_VERSION = 4;

    // table details
    public String tableName = "worldcities";
    public String cityField = "name";
    public String countryField = "country";
    public String subcountryField = "subcountry";

    // Database details
    public String DB_PATH;
    public File DB_FILE;
    private final Context mContext;
    private SQLiteDatabase db;

    // Constructor
    public DatabaseHandler(Context context) {
        super(context, CITIES_DATABASE_NAME, null, DATABASE_VERSION);
        DB_FILE=context.getDatabasePath(CITIES_DATABASE_NAME);
        this.mContext=context;
        DB_PATH = mContext.getDatabasePath(CITIES_DATABASE_NAME)
                .toString();
        boolean dbExist = checkDataBase(); // check if the SQLite DB has been cached locally

        if (!dbExist) {
            // if not, copy the database locally
            this.getWritableDatabase(); // get the database from the "assets" folder
            try {
                copyDataBase();
            }
            catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    // Read records related to the search term
    public List<City> read(String searchTerm) {

        List<City> recordsList = new ArrayList<City>();

        // Build the query: get all DB locations that contain the search term characters
        String sql = "";
        sql += "SELECT * FROM " + tableName;
        sql += " WHERE " + cityField + " LIKE '%" + searchTerm + "%' OR " + countryField + " LIKE '%" + searchTerm + "%' OR " + subcountryField + " LIKE '%" + searchTerm+ "%' ORDER BY priority ASC";

        // Execute the query
        Cursor cursor = db.rawQuery(sql, null);

        // Looping through all rows and adding City objects to a list
        if (cursor.moveToFirst()) {
            do {
                String cityName = cursor.getString(cursor.getColumnIndex(cityField));
                String country = cursor.getString(cursor.getColumnIndex(countryField));
                String subcountry = cursor.getString(cursor.getColumnIndex(subcountryField));
                City currentCity = new City(cityName, country, subcountry);

                // add to list
                recordsList.add(currentCity);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return the list of City objects
        return recordsList;
    }

    // creating table
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Must implement methods of SQLiteOpenHelper
        // This includes create
        // We don't need it since we will be only reading cities from the static SQLite database
    }

    // When upgrading the database, it will drop the current table and recreate.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Must implement methods of SQLiteOpenHelper
        //This includes onUpgrade
        // We don't need it since we will be only reading cities from the static SQLite database
    }

    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        boolean flag;
        try {
            String myPath = DB_PATH;
            checkDB= SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            flag=true;
        }
        catch (SQLiteException e) {
            flag = false;
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return flag;
    }

    private void copyDataBase() throws IOException {
        // Open the local db as the input stream
        InputStream myInput = mContext.getAssets().open(CITIES_DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // Transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(!dbExist) {
            // By calling this method and the empty database will be created into the default system
            // path of the application so we overwrite that database with ours
            this.getWritableDatabase();
            try {
                copyDataBase();
            }
            catch (IOException e) {
                throw new Error(
                        "Error copying database");
            }
        }
    }

    public void openDataBase() throws SQLException {
        // Open the database
        String myPath = DB_PATH;
        db = SQLiteDatabase.openDatabase(myPath, null,SQLiteDatabase.OPEN_READONLY);
    }

}