package ec327.caffiene;


import android.util.Log;
import android.view.View;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

/**
 * Created by trishita on 11/27/2016.
 */
public class database {

    public static int numDrinks;
    public static int counter = 0; //DELETE WHEN YOU IMPLEMENT getPoint function!
    private static final String TAG = "threadDebug"; //remove later!

    //global caffine variables
    private static double caffineLevel = 0;
    private static double caffineInBrain = 0;

    private static int timeMult = 60; //1 for hours, 60 for minutes, 3600 for seconds.
    public static boolean newUser()
    {
        //check if user is new by checking if the database is already populated.
        return false;
    }

    public static String getName()
    {
        //returns name of the user to customize the graph
        return "Trishita";
    }

    //gets data from the database table of caffeine items drunk
    //and plots them on the chart, based off of the time they were drunk.
    public static DataPoint[] getData(double start, double end) {
        //200, 5
        //int iterations = 24;  //unneeded
        int size = (int)(end - start) * timeMult;
        DataPoint[] data = new DataPoint[size];
        //x is time, y is caffine amount

        //get times where the graph will need to be updated:
        double[] times = StoredData.timeTable();
        int[] addCaff = StoredData.caffineTable();
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
            for(int j = 0; j < nestedsize; j++) {
                if(time == times[j]) {
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
        double halfLife = (5.7*timeMult); //in hours
        double changeInLevels = -(Math.log(2) / halfLife) * caffineAmount;

        double result = caffineAmount + changeInLevels;
        return result;
    }
    //The other function that's used to caclulate the next caffine amount tick
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
        //add this data to a new table. Will ultimately never be used.
    }


    //The function that gets called when a User wants to drink a paticular drink
    //input 6,2.5 to drink the test case at time 2hr and 20 minutes
    public static void addDrinktoDB(int drinkindex, double time)
    {
        //add drink to database
        //called when user decides that he/she wants to drink a specefic drink at the specefied time
        //float time = HomePage.getTime();
        StoredData.selectCaffine(drinkindex, time * timeMult);
        //////update the graph////////////////////
    }

    //Not implimented yet
    public static void matchQuery(String query) //this is called when the user is searching our database for a drink to add.
    {//We will implement a dynamic search, this function should remove all strings from the static arraylist in AddDrink.class
        //that don't don't have query as a substring
        //AddDrink.matches.remove(0); //dummy index.
        Log.d(TAG,"ran this function in the thread");
        //
        //NOTE: this function is repeatedly called in a thread, so you must make sure it is efficient.
        //NOTE: THE ARRAYLIST YOU HAVE TO MODIFY IS AddDrink.matches
    }

    //insert a new choice in a list of drink choices.
    //currently not showing new choices. I believe its due to
    public static void addNewDrink(String drink,float caffiene)
    {
        int caff = (int)caffiene;
        StoredData.addData(drink, caff);
        AddDrink.alldrinks = allDrinks();
    }

    //this is all of the drinks.
    //test by tapping 'add drink'
    public static ArrayList<String> allDrinks()
    {
        numDrinks = StoredData.getNumberOfRows(StoredData.caffineListTableName); //the number of drinks in the database
        //return all the drink names in the database as an arrayList of strings IN ALPHABETICAL ORDER!
        ArrayList<String> list =  new ArrayList<String>(numDrinks);
        int size =StoredData.getNumberOfRows(StoredData.caffineListTableName);
        for(int i = 0; i < size; i++) {
            list.add(i, StoredData.getName(i));
        }
        return list;
    }
    //////
}
