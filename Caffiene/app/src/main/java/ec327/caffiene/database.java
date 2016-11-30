package ec327.caffiene;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by trishita on 11/27/2016.
 */
public class database {
    public static int numDrinks;
    public static boolean newUser()
    {
        //check if user is new by checking if the database is already populated.
        return true;
    }
    public static DataPoint[] getData()
    {
        return new DataPoint[]
                {//we will only be displaying the data for the current day.
                        new DataPoint(0, 1), //replace these numbers with actual x-y vals
                        new DataPoint(1, 5), //x should range from 0-24 hrs
                        new DataPoint(2, 3),
                        new DataPoint(3, 2),
                        new DataPoint(4, 6)
                };
    }

    public static DataPoint getPoint()
    {
        Long time = System.currentTimeMillis()/1000;
        return new DataPoint(time,2); //replace with actual value from database
    }

    public static void addInfo(int age, double weight, String name, String gender)
    {
        //add these to the database
    }

    public static void addDrinktoDB(String drink, long time)
    {
        //add drink to database
        //called when user decides that he/she wants to drink a specefic drink at the specefied time
    }

    public static void matchQuery(String query) //this is called when the user is searching our database for a drink to add.
    {//We will implement a dynamic search, this function should remove all strings from the static arraylist in AddDrink.class
        //that don't don't have query as a substring
        AddDrink.matches.remove(5); //dummy index.
        //NOTE: THE ARRAYLIST YOU HAVE TO MODIFY IS AddDrink.matches
    }

    public static void addNewDrink(String drink,float caffiene)
    {
        //adds this custom drink to the database
    }

    public static ArrayList<String> allDrinks()
    {
        numDrinks = 20; //the number of drinks in the database
        //return all the drink names in the database as an arrayList of strings IN ALPHABETICAL ORDER!
        return new ArrayList<String>(numDrinks);
    }
}
