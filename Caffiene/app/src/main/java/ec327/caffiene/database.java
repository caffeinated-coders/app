package ec327.caffiene;


import android.content.SharedPreferences;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;

/**
 * Handles connections between our view and StoredData model
 *
 * @author Trishita Tiwari
 * @version 1.0
 */
public class database {

    private static final String TAG = "threadDebug";
    public static int numDrinks;
    public static int counter = 0;
    //global caffine variables
    private static double caffineLevel = 0;
    private static double caffineInBrain = 0;

    //store person settings
    private static int timeMult = 60; //1 for hours, 60 for minutes, 3600 for seconds.

    public static String getName() {
        return HomePage.preferences.getString("name", "Oh Noes");
    }

    //gets data from the database table of caffeine items drunk
    //and plots them on the chart, based off of the time they were drunk.
    public static DataPoint[] getData(double start, double end) {
        int size = (int) (end - start) * timeMult;
        DataPoint[] data = new DataPoint[size];
        //x is time, y is caffine amount

        //get times where the graph will need to be updated:
        double[] times = StoredData.timeTable();
        int[] addCaff = StoredData.caffeineTable();
        int nestedsize = times.length;
        //Multiday problem: set a day bit to true if previous elements are less than 24 hours
        //or have an int instead and delete caffine data after five days. It's not like the user is going to drink thousands of cups of coffee
        //I hope

        int i = 0;
        for (double time = start * timeMult; time < end * timeMult; time++) {

            double bout = Math.round(caffineInBrain * 100.0) / 100.0;
            double cafout = Math.round(caffineLevel * 100.0) / 100.0;

            //check to see if we need to add more caffine on this cycle
            //Love this code such elegance would rate 9.2/10
            for (int j = 0; j < nestedsize; j++) {
                if (time == times[j]) {
                    caffineLevel = caffineLevel + addCaff[j];
                }
            }

            double timeM = timeMult;
            double ti = time;

            double outTime = ti / timeM;
            //System.out.println("Time: "+ outTime +" Brain Caffine: " + caffineInBrain + " Caffine Levels: " + caffineLevel);
            data[i] = new DataPoint(outTime, caffineInBrain);      //the current caffine level is the next one to be put into data
            caffineInBrain = bloodTick(caffineInBrain);         //incriment the caffine amount
            i++;
        }

        //reset
        caffineLevel = 0;
        caffineInBrain = 0;
        return data;
    }

    //easy way to convert longs to ints;
    //not really used anymore
    private static int longToInt(long number) {
        Long l = new Long(number);    //casting it into object type
        int i = l != null ? l.intValue() : null;
        return i;
    }

    //Next two functions used to caclulate the next caffine amount tick
    private static double caffineTick(double caffineAmount) {
        double halfLife = (5.7 * timeMult); //in hours
        double changeInLevels = -(Math.log(2) / halfLife) * caffineAmount;

        double result = caffineAmount + changeInLevels;
        return result;
    }

    //The other function that's used to caclulate the next caffine amount tick
    private static double bloodTick(double Blood) {
        caffineLevel = caffineTick(caffineLevel);
        double CAFFINE_METABOLISM = 1 / (2.5 * timeMult); //per hour
        double CAFFINE_ABSORBTION = 1 / (2.7 * timeMult); //per hour
        double changeInBlood = (-CAFFINE_METABOLISM * Blood) + (CAFFINE_ABSORBTION * caffineLevel);

        double result = Blood + changeInBlood;

        return result;
    }

    //info data
    public static void addInfo(int age, double weight, String name, String gender) {
        //add this data to a new table. Will ultimately never be used.
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context

        SharedPreferences.Editor editor = HomePage.preferences.edit();

        editor.putString("name", name);
        editor.putInt("age", age);
        editor.putFloat("weight", (float) weight);
        editor.putString("gender", gender);
        // Commit the edits!
        editor.commit();
    }


    //The function that gets called when a User wants to drink a paticular drink
    //input 6,2.5 to drink the test case at time 2hr and 20 minutes
    public static void addDrinktoDB(int drinkindex, double time) {
        //add drink to database
        //called when user decides that he/she wants to drink a specefic drink at the specefied time
        //float time = HomePage.getTime();
        StoredData.selectCaffeine(drinkindex, time * timeMult);
        //////update the graph////////////////////
    }

    //Not implimented yet
    public static void matchQuery(String query) //this is called when the user is searching our database for a drink to add.
    {//We will implement a dynamic search, this function should remove all strings from the static arraylist in AddDrink.class
        //that don't don't have query as a substring
        //AddDrink.matches.remove(0); //dummy index.
        //Log.d(TAG,"ran this function in the thread");

        int size = StoredData.getNumberOfRows(StoredData.beverageListTableName);
        for (int i = 0; i < size; i++) {
            String drinkelement = StoredData.getName(i);
            if (query.isEmpty()) {
                AddDrink.matches.contains(drinkelement);
                continue;
            }

            boolean test = drinkelement.toLowerCase().contains(query.toLowerCase());

            //System.out.println(AddDrink.matches.toString());
            if (!test) AddDrink.matches.remove(drinkelement);
            else if (!AddDrink.matches.contains(drinkelement)) AddDrink.matches.add(drinkelement);
        }

        //NOTE: this function is repeatedly called in a thread, so you must make sure it is efficient.
        //NOTE: THE ARRAYLIST YOU HAVE TO MODIFY IS AddDrink.matches
    }

    //insert a new choice in a list of drink choices.
    //currently not showing new choices. I believe its due to
    public static void addNewDrink(String drink, float caffiene) {
        int caff = (int) caffiene;
        StoredData.addBeverageToDB(drink, caff);
        AddDrink.alldrinks = allDrinks();
    }

    //this is all of the drinks.
    //test by tapping 'add drink'
    public static ArrayList<String> allDrinks() {
        numDrinks = StoredData.getNumberOfRows(StoredData.beverageListTableName); //the number of drinks in the database
        //return all the drink names in the database as an arrayList of strings IN ALPHABETICAL ORDER!
        ArrayList<String> list = new ArrayList<String>(numDrinks);
        int size = StoredData.getNumberOfRows(StoredData.beverageListTableName);
        for (int i = 0; i < size; i++) {
            list.add(i, StoredData.getName(i));
        }
        return list;
    }
    //////
}
