package ec327.caffiene;

import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

/**
 * Created by trishita on 11/27/2016.
 */
public class database {
    public static int numDrinks;
    public static int counter = 0; //DELETE WHEN YOU IMPLEMENT getPoint function!
    private static final String TAG = "threadDebug"; //remove later!

    //global caffine variables
    private static double caffineLevel = 500;
    private static double caffineInBrain = 0;

    private static int timeMult = 1; //1 for hours, 60 for minutes, 3600 for seconds.
    public static boolean newUser()
    {
        //check if user is new by checking if the database is already populated.
        return false;
    }
    /*
    public static DataPoint[] getData()
    {
        return new DataPoint[]
                {//we will only be displaying the data for the current day.
                        //PUT DATA POITNS HERE --Nehemiah
                        new DataPoint(0, 1), //replace these numbers with actual x-y vals
                        new DataPoint(1, 5), //x should range from 0-24 hrs
                        new DataPoint(2, 3),
                        new DataPoint(3, 2),
                        new DataPoint(4, 6),
                        new DataPoint(5, 1), //replace these numbers with actual x-y vals
                        new DataPoint(16, 5), //x should range from 0-24 hrs
                        new DataPoint(17, 3),
                        new DataPoint(18, 2),
                        new DataPoint(19, 6),
                        new DataPoint(20, 1), //replace these numbers with actual x-y vals
                        new DataPoint(21, 5), //x should range from 0-24 hrs
                        new DataPoint(22, 3),
                        new DataPoint(23, 2),
                        new DataPoint(24, 6)
                };
    }*/

    public static DataPoint[] getData(long start, long end) {

        //int iterations = 24;  //unneeded
        int size = longToInt(end - start) * timeMult;
        DataPoint[] data = new DataPoint[size];
        //x is time, y is caffine amount
        int i = 0;
        for (long time = start * timeMult; time < end * timeMult; time++) {
            double bout = Math.round(caffineInBrain * 100.0) / 100.0;
            double cafout = Math.round(caffineLevel * 100.0) / 100.0;

            long outTime = (time/timeMult);
            System.out.println("Time: " + time + " Brain Caffine: " + caffineInBrain + " Caffine Levels: " + caffineLevel);
            StoredData.addData(caffineInBrain,time);
            data[i] = new DataPoint(time, caffineInBrain);      //the current caffine level is the next one to be put into data
            caffineInBrain = bloodTick(caffineInBrain);         //incriment the caffine amount
            i++;
        }

        System.out.println("THE AMOUNT OF ELEMENTS IN THE DATABASE TIME TABLE: " + StoredData.getNumberOfRows("times"));
        return data;
    }

    //easy way to convert longs to ints;
    private static int longToInt(long number) {
        Long l = new Long(number);    //casting it into object type
        int i = l != null ? l.intValue() : null;
        return i;
    }
    //Next two functions used to caclulate the next caffine amount tick
    private static double caffineTick(double caffineAmount) {
        double halfLife = (5.7*timeMult); //in hours
        double changeInLevels = -(Math.log(2) / halfLife) * caffineAmount;

        double result = caffineAmount + changeInLevels;
        return result;
    }

    private static double bloodTick(double Blood) {
        caffineLevel = caffineTick(caffineLevel);
        double CAFFINE_METABOLISM = 1/(2.5 * timeMult); //per hour
        double CAFFINE_ABSORBTION = 1/(2.7 * timeMult); //per hour
        double changeInBlood = (-CAFFINE_METABOLISM * Blood) + (CAFFINE_ABSORBTION * caffineLevel);

        double result = Blood + changeInBlood;

        return result;
    }
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
    //////
}
