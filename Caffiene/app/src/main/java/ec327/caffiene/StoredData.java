package ec327.caffiene;


/**
 * Created by nmd1 on 12/6/2016.
 */
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class StoredData {

    public static String caffineListTableName;
    public static String caffineConsumedTableName;
    public static SQLiteDatabase myDB;

    //creates the tables that store data for this program. This function should only be called once.
    //Test Input -->(coffeeData, "CoffeeListTable", "CoffeeConsumedTable")
    public static void createTables(SQLiteDatabase inputDataBase, String caffineOffered, String caffineTaken) {
        /* Create a Table in the Database. */
        //creates the types of caffine offered table
        caffineListTableName = caffineOffered;
        caffineConsumedTableName = caffineTaken;
        myDB = inputDataBase;
        //DELETE TABLE DROP ONCE TESTING IS DONE
        //myDB.execSQL("DROP TABLE IF EXISTS "+caffineConsumedTableName+";");
        //myDB.execSQL("DROP TABLE IF EXISTS "+caffineListTableName+";");

        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + caffineOffered+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, Name CHAR(50), caffineContent INT);");
        //this creates the caffineconsumed table in the database. int index can be whatever number: it was used so that the function can be overrided
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + caffineTaken+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, Name CHAR(50), Caffine INT, TimeConsumed FLOAT);");
        StoredData.addDefaultCaffineList();
        //database.addDrinktoDB(6,HomePage.now);
        //"id" is the index value
    }


    public static void addData(String name, int caffineContent) {
        /* Insert data to a Table*/
        myDB.execSQL("INSERT INTO "
                + caffineListTableName
                + " (Name, caffineContent)"
                + " VALUES ('"+name+"', "+caffineContent+");");
    }

    public static void addData(String name, int CaffineAmount, double timeOfConsumtion) {
        /* Insert data to a Table*/
        myDB.execSQL("INSERT INTO "
                + caffineConsumedTableName
                + " (Name, Caffine, TimeConsumed)"
                + " VALUES ('"+name+"', "+CaffineAmount+", "+timeOfConsumtion+");");
    }


    //helper function: gets called through addDrinktoDB(int drinkindex, float time) in the database file
    public static void selectCaffine(int index, double time) {

        //I want this row from caffine list table
        int id = index + 1;
        Cursor c = myDB.rawQuery("SELECT * FROM " + caffineListTableName + " WHERE ID="+id+";", null);
        int Column1 = c.getColumnIndex("Name");
        int Column2 = c.getColumnIndex("caffineContent");
        // Check if our result was valid.
        c.moveToFirst();
        String drinkName = c.getString(Column1);
        int caffineAmount = c.getInt(Column2);

        //push that into the caffine consumed table
        addData(drinkName, caffineAmount, time);
        System.out.println(index + " -Just added: " + drinkName + " at time " + time + " into a table");
        c.close();
    }

    //gets the number of rows in a table
    //test the boundry conditions of this
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


    //should print the output of each table.
    //more of a legacy debugging function. not needed.
    //also never got it to work after discovering AVD
    public static void print(int i) {
        String name = "times";
//
        if(i == 0) name = caffineListTableName;
        if(i == 1) name = caffineConsumedTableName;
        Cursor c = myDB.rawQuery("SELECT * FROM " + name, null);

        int Column1 = c.getColumnIndex("Name");
        int Column2 = c.getColumnIndex("caffineContent");

        // Check if our result was valid.
        c.moveToFirst();
        String Data = "";
        if (c != null) {
            // Loop through all Results
            do {
                String Name = c.getString(Column1);
                int Age = c.getInt(Column2);
                Data = Data + Name + "/" + Age + "\n";
                Log.d("Output", Data);
            } while (c.moveToNext());
        }
        c.close();
    }

    //gets the name of a caffeine item in the caffine items to drink table
    //0 should give you "coffee" 6 should give you "Test"
    public static String getName(int index) {
        index = index + 1;
        int rows = getNumberOfRows(caffineListTableName);
        if(index > rows) return "";
        Cursor c = myDB.rawQuery("SELECT * FROM " + caffineListTableName + " WHERE ID=" + index + ";", null);
        int Column1 = c.getColumnIndex("Name");
        c.moveToFirst();
        String s = c.getString(Column1);
        c.close();
        return s;
    }

    //returns the time at which an item was drunk.
    //index referrs to a position in the consumed caffine table
    public static double getTime(int index) {
        index = index + 1;
        int rows = getNumberOfRows(caffineConsumedTableName);
        if(index > rows) return 0;
        Cursor c = myDB.rawQuery("SELECT * FROM " + caffineConsumedTableName + " WHERE ID=" + index + ";", null);
        int Column1 = c.getColumnIndex("TimeConsumed");
        c.moveToFirst();
        double d = c.getFloat(Column1);
        c.close();
        return d;
    }

    //returns the caffine amount of an item that was drunk.
    //index refers to a position in the consumed caffine table
    public static int getConsumedCaffine(int index) {
        index = index + 1;
        int rows = getNumberOfRows(caffineConsumedTableName);
        if(index > rows) return 0;
        Cursor c = myDB.rawQuery("SELECT * FROM " + caffineConsumedTableName + " WHERE ID=" + index + ";", null);
        c.moveToFirst();
        int Column1 = c.getColumnIndex("Caffine");
        int returnData = c.getInt(Column1);
        c.close();
        return returnData;
    }

    //compiles a list of times at which caffine was consumed
    //should basically output a column in the consumed database table
    public static double[] timeTable() {
        int size = getNumberOfRows(caffineConsumedTableName);
        double[] times = new double[size];

        for(int i = 0; i < size; i++) {
            double d = getTime(i);
            d = Math.round(d * 10);
            d = d/10;
            times[i] = d;
        }
        return times;
    }

    //compiles a list of caffine amounts that was consumed
    //should basically output a column in the consumed database table
    public static int[] caffineTable() {
        int size = getNumberOfRows(caffineConsumedTableName);
        int[] times = new int[size];

        for(int i = 0; i < size; i++) {
            times[i] = getConsumedCaffine(i);
        }
        return times;
    }

    //sets the list of default caffine values
    public static void addDefaultCaffineList() {

        addData("Coffee", 100);
        addData("Coca-Cola (12 floz)", 35);
        addData("Pepsi (12 floz)", 38);
        addData("Mountain Dew", 54);
        addData("Monster Energy Drink", 180);
        addData("Caffine Pill", 200);
        //addData("Test", 500);

    }

    public static SQLiteDatabase getDatabase() {
        return myDB;
    }
}
