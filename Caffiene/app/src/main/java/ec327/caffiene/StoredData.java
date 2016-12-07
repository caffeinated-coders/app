package ec327.caffiene;

/**
 * Created by nmd1 on 12/6/2016.
 */


import android.database.Cursor;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteClosable;


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

        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + caffineOffered+"(ID INT PRIMARY KEY, Name CHAR(50), caffineContent INT);");
        //this creates the caffineconsumed table in the database. int index can be whatever number: it was used so that the function can be overrided
        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + caffineTaken+"(ID INT PRIMARY KEY, Name CHAR(50), Caffine INT, TimeConsumed FLOAT);");

        //test table

        myDB.execSQL("CREATE TABLE IF NOT EXISTS "
                + "times" +"(ID INT PRIMARY KEY, Caffine DOUBLE, Time DOUBLE);");
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
    public static void addData(double caffine, double time) {
        /* Insert data to a Table*/
        myDB.execSQL("INSERT INTO "
                + "times"
                + "(Caffine, Time)"
                + " VALUES ("+caffine+", "+time+");");
    }


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

        if(i == 0) name = caffineListTableName;
        if(i == 1) name = caffineConsumedTableName;

    }
    public static SQLiteDatabase getDatabase() {
        return myDB;
    }
}
