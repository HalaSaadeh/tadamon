package com.example.tadamon;


// City class for the cities we query from the SQLLite database
public class City {
    private String cityName, country, subcountry;

    public City(String cityName, String country, String subcountry){
        this.cityName=cityName;
        this.country=country;
        this.subcountry=subcountry;

    }
    public String toString(){
        return cityName+", "+subcountry+", "+ country;
    }

}
