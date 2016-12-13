package ec327.caffiene;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Database Model class. Handles interactions with the SQLite database
 *
 * @author Nehemiah Dureus
 * @version 1.0
 */
public class StoredData {

    public static String beverageListTableName;
    public static String consumptionListTableName;
    public static SQLiteDatabase myDB;

    /**
     * "Constructor" of the class. Sets up initial databases.
     *
     * @param inputDataBase            the SQLiteDatabase to be used
     * @param beverageListTableName    the name of the DB table containing available beverages
     * @param consumptionListTableName the name of the DB tables containing the consumed caffeine
     */
    public static void createTables(SQLiteDatabase inputDataBase, String beverageListTableName, String consumptionListTableName) {
        /* Create a Table in the Database. */
        //creates the types of caffeine offered table
        StoredData.beverageListTableName = beverageListTableName;
        StoredData.consumptionListTableName = consumptionListTableName;
        myDB = inputDataBase;

        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + beverageListTableName + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, Name CHAR(50), caffeineContent INT);");
        //this creates the caffeineconsumed table in the database. int index can be whatever number: it was used so that the function can be overrided
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + consumptionListTableName + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, Name CHAR(50), Caffeine INT, TimeConsumed FLOAT);");
        StoredData.addDefaultCaffeineList();
    }

    /**
     * Add a beverage to the list of possible options
     *
     * @param name            the name of the beverage
     * @param caffeineContent the caffeine content in mg
     */
    public static void addBeverageToDB(String name, int caffeineContent) {
        /* Insert data to a Table*/
        myDB.execSQL("INSERT INTO "
                + beverageListTableName
                + " (Name, caffeineContent)"
                + " VALUES ('" + name + "', " + caffeineContent + ");");
    }

    /**
     * Add a consumption record to the database
     *
     * @param name              the name of the beverage
     * @param CaffineAmount     the caffeine content of the consumed beverage
     * @param timeOfConsumption the time of consumption
     */
    public static void addConsumptionToDB(String name, int CaffineAmount, double timeOfConsumption) {
        /* Insert data to a Table*/
        myDB.execSQL("INSERT INTO "
                + consumptionListTableName
                + " (Name, Caffeine, TimeConsumed)"
                + " VALUES ('" + name + "', " + CaffineAmount + ", " + timeOfConsumption + ");");
    }


    /**
     * helper function: gets called through addConsumptiontoDB(int drinkindex, float time) in the database file
     * Adds a drink to the database.
     */
    public static void selectCaffeine(int index, double time) {

        //I want this row from caffine list table
        int id = index + 1;
        Cursor c = myDB.rawQuery("SELECT * FROM " + beverageListTableName + " WHERE ID=" + id + ";", null);
        int Column1 = c.getColumnIndex("Name");
        int Column2 = c.getColumnIndex("caffeineContent");
        // Check if our result was valid.
        c.moveToFirst();
        String drinkName = c.getString(Column1);
        int caffeineAmount = c.getInt(Column2);

        //push that into the caffine consumed table
        addConsumptionToDB(drinkName, caffeineAmount, time);
        System.out.println(index + " -Just added: " + drinkName + " at time " + time + " into a table");
        c.close();
    }

    /**
     * Simple getter for the qty of rows in a table
     * @param table the name of the table
     * @return number of rows
     */
    public static int getNumberOfRows(String table) {
        /*retrieve data from database */
        Cursor c = myDB.rawQuery("SELECT * FROM " + table, null);
        int i = 0;
        if (c != null) {
            // Loop through all Results
            while (c.moveToNext()) {
                i++;
            }
        }
        c.close();

        return i;
    }

    /**
     * Fetch the name of a beverage, based on ID
     * @param index the ID
     * @return drink name
     */
    public static String getName(int index) {
        index = index + 1;
        int rows = getNumberOfRows(beverageListTableName);
        if (index > rows) {
            return "";
        }
        Cursor c = myDB.rawQuery("SELECT * FROM " + beverageListTableName + " WHERE ID=" + index + ";", null);
        int Column1 = c.getColumnIndex("Name");
        c.moveToFirst();
        String s = c.getString(Column1);
        c.close();
        return s;
    }

    /**
     * Returns the time at which an item was drunk. Index referrs to a position in the consumed caffine table
     * @param index
     * @return time consumed
     */
    public static double getTime(int index) {
        index = index + 1;
        int rows = getNumberOfRows(consumptionListTableName);
        if (index > rows) return 0;
        Cursor c = myDB.rawQuery("SELECT * FROM " + consumptionListTableName + " WHERE ID=" + index + ";", null);
        int Column1 = c.getColumnIndex("TimeConsumed");
        c.moveToFirst();
        double d = c.getFloat(Column1);
        c.close();
        return d;
    }

    /**
     * Returns the caffine amount of an item that was drunk.
     *
     * @param index drink ID
     * @return
     */
    public static int getConsumedCaffeine(int index) {
        index = index + 1;
        int rows = getNumberOfRows(consumptionListTableName);
        if (index > rows) return 0;
        Cursor c = myDB.rawQuery("SELECT * FROM " + consumptionListTableName + " WHERE ID=" + index + ";", null);
        c.moveToFirst();
        int Column1 = c.getColumnIndex("Caffeine");
        int returnData = c.getInt(Column1);
        c.close();
        return returnData;
    }


    public static double[] timeTable() {
        int size = getNumberOfRows(consumptionListTableName);
        double[] times = new double[size];

        for (int i = 0; i < size; i++) {
            double d = getTime(i);
            d = Math.round(d * 10);
            d = d / 10;
            times[i] = d;
        }
        return times;
    }

    /**
     * Compiles a list of caffeine content consumed
     *
     * @returns column of data
     */
    public static int[] caffeineTable() {
        int size = getNumberOfRows(consumptionListTableName);
        int[] times = new int[size];

        for (int i = 0; i < size; i++) {
            times[i] = getConsumedCaffeine(i);
        }
        return times;
    }

    /**
     * Adds default drinks to DB
     */
    public static void addDefaultCaffeineList() {

        addBeverageToDB("Coffee", 100);
        addBeverageToDB("Coca-Cola (12 floz)", 35);
        addBeverageToDB("Pepsi (12 floz)", 38);
        addBeverageToDB("Mountain Dew", 54);
        addBeverageToDB("Monster Energy Drink", 180);
        addBeverageToDB("Caffeine Pill", 200);
        //addConsumptionToDB("Test", 500);

    }

    public static SQLiteDatabase getDatabase() {
        return myDB;
    }
}
