package ec327.caffiene;

import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

/**
 * Created by trishita on 11/27/2016.
 */
public class database {
    public static int numDrinks;
    public static int counter = 5; //DELETE WHEN YOU IMPLEMENT getPoint function!
    private static final String TAG = "threadDebug"; //remove later!
    public static boolean newUser()
    {
        //check if user is new by checking if the database is already populated.
        return false;
    }
    public static DataPoint[] getData()
    {
        return new DataPoint[]
                {//we will only be displaying the data for the current day.
                        //PUT DATA POITNS HERE --Nehemiah
                        new DataPoint(0, 100), //replace these numbers with actual x-y vals
                        new DataPoint(1, 577), //x should range from 0-24 hrs
                        new DataPoint(2, 34),
                        new DataPoint(3, 233),
                        new DataPoint(4, 60),
                        new DataPoint(5, 134), //replace these numbers with actual x-y vals
                        new DataPoint(16, 523), //x should range from 0-24 hrs
                        new DataPoint(17, 334),
                        new DataPoint(18, 234),
                        new DataPoint(19, 64),
                        new DataPoint(20, 134), //replace these numbers with actual x-y vals
                        new DataPoint(21, 534), //x should range from 0-24 hrs
                        new DataPoint(22, 334),
                        new DataPoint(23, 234),
                        new DataPoint(24, 63)
                };
    }
    //don't touch


    //info data
    public static void addInfo(int age, double weight, String name, String gender)
    {
        //add these to the database
    }

    //
    public static void addDrinktoDB(int drinkindex, long time)
    {
        //add drink to database
        //called when user decides that he/she wants to drink a specefic drink at the specefied time
    }

    public static void matchQuery(String query) //this is called when the user is searching our database for a drink to add.
    {//We will implement a dynamic search, this function should remove all strings from the static arraylist in AddDrink.class
        //that don't don't have query as a substring
        //AddDrink.matches.remove(0); //dummy index.
        Log.d(TAG,"ran this function in the thread");
        //
        //NOTE: this function is repeatedly called in a thread, so you must make sure it is efficient.
        //NOTE: THE ARRAYLIST YOU HAVE TO MODIFY IS AddDrink.matches
    }

    public static void addNewDrink(String drink,float caffiene)
    {
        //adds this custom drink to the database
    }

    //this is all of the drinks.
    public static ArrayList<String> allDrinks()
    {
        numDrinks = 5; //the number of drinks in the database
        //return all the drink names in the database as an arrayList of strings IN ALPHABETICAL ORDER!
        ArrayList<String> list =  new ArrayList<String>(numDrinks);
        list.add(0, "coke") ;
        list.add(1, "coffee") ;
        list.add(2, "tea") ;
        list.add(3, "fanta") ;
        list.add(4, "lemonade") ;
        return list;
    }
}
