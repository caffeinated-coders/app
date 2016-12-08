package ec327.caffiene;


/**
 * Created by nmd1 on 12/6/2016.
 */


import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteClosable;
import android.nfc.Tag;
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
        myDB.execSQL("DROP TABLE IF EXISTS "+caffineConsumedTableName+";");
        myDB.execSQL("DROP TABLE IF EXISTS "+caffineListTableName+";");


        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + caffineOffered+"(ID INT PRIMARY KEY, Name CHAR(50), caffineContent INT);");
        //this creates the caffineconsumed table in the database. int index can be whatever number: it was used so that the function can be overrided
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + caffineTaken+"(ID INT PRIMARY KEY, Name CHAR(50), Caffine INT, TimeConsumed FLOAT);");

        //"id" is the index value
    }


    public static void addData(String name, int caffineContent) {
        /* Insert data to a Table*/
        myDB.execSQL("INSERT INTO "
                + caffineListTableName
                + " (Name, caffineContent)"
                + " VALUES ('"+name+"', "+caffineContent+");");
    }

    public static void addData(String name, int CaffineAmount, float timeOfConsumtion) {
        /* Insert data to a Table*/
        myDB.execSQL("INSERT INTO "
                + caffineConsumedTableName
                + " (Name, Caffine, TimeConsumed)"
                + " VALUES ('"+name+"', "+CaffineAmount+", "+timeOfConsumtion+");");
    }

    //TEMPERARY ADD DATA THING
    /*
    public static void addData(double caffine, boolean time) {
        /* Insert data to a Table
        myDB.execSQL("INSERT INTO "
                + "times"
                + "(Caffine, Time)"
                + " VALUES ("+caffine+", "+time+");");
    }*/

    //

    public static void returnRow(String TableName, int row) {
        /*retrieve data from database */
        Cursor c = myDB.rawQuery("SELECT * FROM " + TableName, null);

        int Column1 = c.getColumnIndex("Field1");
        int Column2 = c.getColumnIndex("Field2");

        // Check if our result was valid.
        c.moveToFirst();
        String Data = "";
        if (c != null) {
            // Loop through all Results
            do {
                String Name = c.getString(Column1);
                int Age = c.getInt(Column2);
                Data = Data + Name + "/" + Age + "\n";
            } while (c.moveToNext());
        }
    }

    public static void insertIntoTable(String s) {

    }

    public static void selectCaffine(int index, float time) {

        //I want this row from caffine list table
        /*retrieve data from database */
        //Cursor c = myDB.rawQuery("SELECT * FROM " + caffineListTableName, null);
        int id = index  +1;
        Cursor c = myDB.rawQuery("SELECT * FROM " + caffineListTableName + " WHERE ROWID="+id+";", null);
        int Column1 = c.getColumnIndex("Name");
        int Column2 = c.getColumnIndex("caffineContent");

        String drinkName = "";
        int caffineAmount = 0;
        // Check if our result was valid.
        c.moveToFirst();
        int i = 0;
        drinkName = c.getString(Column1);
        caffineAmount = c.getInt(Column2);
        /*
        if (c != null) {
            // Loop through all Results
            do {
                if(i*3 == index) {
                    drinkName = c.getString(Column1);
                    caffineAmount = c.getInt(Column2);
                    break;
                }

                i++;
            } while (c.moveToNext());
        }*/

        //push that into the caffine consumed table
        addData(drinkName, caffineAmount, time);
        System.out.println(index + " -Just added: " + drinkName + " at time " + time + " into a table");
    }

    public static int getNumberOfRows(String table) {
        /*retrieve data from database */
        Cursor c = myDB.rawQuery("SELECT * FROM " + table, null);
        int i = 0;
        if (c != null) {
            // Loop through all Results
            do {
                i++;
            } while (c.moveToNext());
        }

        return i;
    }

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

    }

    public static String getName(int index) {
        Cursor c = myDB.rawQuery("SELECT * FROM " + caffineListTableName, null);

        int Column1 = c.getColumnIndex("Name");
        //int Column2 = c.getColumnIndex("Field2");

        // Check if our result was valid.
        c.moveToFirst();
        String Data = "";
        int i = 0;
        if (c != null) {
            // Loop through all Results
            do {

                //int Age = c.getInt(Column2);
                if(i == index) {
                    return c.getString(Column1);
                }
                i++;
            } while (c.moveToNext());
        }

        return "Error";
    }

    public static void addDefaultCaffineList() {

        addData("Coffee", 100);
        addData("Coca-Cola (12 floz)", 35);
        addData("Pepsi (12 floz)", 38);
        addData("Mountain Dew", 54);
        addData("Monster Energy Drink", 180);
        addData("Caffine Pill", 200);
        addData("Test", 500);

    }
/*
    public static void dropTimeTable() {
        myDB.execSQL("DROP TABLE IF EXISTS times;");
       /// myDB.execSQL("DROP TABLE IF EXISTS "+caffineConsumedTableName+";");
        //myDB.execSQL("DROP TABLE IF EXISTS "+caffineListTableName+";");

    }*/

    public static SQLiteDatabase getDatabase() {
        return myDB;
    }
}
