package ec327.caffiene;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by anthony on 12/8/16.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class StoredDataInstTest {
    //public static HomePage home;

    private SQLiteDatabase DataBase;
    private Context ctx;
    private static final String TAG = "SDIT";

    @Before
    public void setUp() throws Exception {
        ctx = InstrumentationRegistry.getTargetContext();
        DataBase = ctx.openOrCreateDatabase("coffeeData", Context.MODE_PRIVATE, null);
    }

    @After
    public void tearDown() throws Exception {
        //Nothing to do
    }

    @Test
    public void createTables() throws Exception {
        Log.d(TAG + "-CT", "STAR Begin testing of StoredData.createTables()");
        StoredData.createTables(DataBase, "TestDrinkList", "TestConsumedList");

        //Test that the 7 default drinks are created
        assertThat(StoredData.getNumberOfRows("TestDrinkList"), is(7));
        Log.d(TAG + "-CT", "PASS Default Drink List Row Qty");

        //Test that the Consumed list has one entry
        assertThat(StoredData.getNumberOfRows("TestConsumedList"), is(1));
        Log.d(TAG + "-CT", "PASS Default Consumed List Row Qty");

        //Test that both tables have the correct columns
        Cursor cDrinkList = StoredData.getDatabase().rawQuery("SELECT * FROM TestDrinkList", null);
        Cursor cConsumedList = StoredData.getDatabase().rawQuery("SELECT * FROM TestConsumedList", null);
        String[] drinkListCols = {"ID", "Name", "caffineContent"};
        String[] consumedListCols = {"ID", "Name", "Caffine", "TimeConsumed"};

        assertThat(cDrinkList.getColumnNames(), is(drinkListCols));
        Log.d(TAG + "-CT", "PASS Drink List Column Names");

        assertThat(cConsumedList.getColumnNames(), is(consumedListCols));
        Log.d(TAG + "-CT", "PASS Consumed List Column Names");

        Log.d(TAG + "-CT", "COMP All createTables() tests passed!");
    }

    @Test
    public void addDataDrinkList() throws Exception {
        Log.d(TAG + "-ADL", "STAR Begin testing of StoredData.addData() (drink list)");
        StoredData.createTables(DataBase, "TestDrinkList", "TestConsumedList");

        StoredData.addData("TERRIER_FUEL", 9000);

        Cursor cDrinkList = StoredData.getDatabase().rawQuery("SELECT * FROM TestDrinkList WHERE `Name`='TERRIER_FUEL'", null);
        cDrinkList.moveToFirst();

        assertThat(cDrinkList.getCount(), is(1));
        Log.d(TAG + "-ADL", "PASS New Drink Created");

        assertThat(cDrinkList.getString(1), is("TERRIER_FUEL"));
        Log.d(TAG + "-ADL", "PASS New Drink Name");

        assertThat(cDrinkList.getInt(2), is(9000));
        Log.d(TAG + "-ADL", "PASS New Drink Caffeine Content");

        Log.d(TAG + "-ADL", "COMP All addData() (drink list) tests passed!");
    }

    @Test
    public void addDataConsumedList() throws Exception {
        Log.d(TAG + "-ADC", "STAR Begin testing of StoredData.addData() (consumed drink list)");
        StoredData.createTables(DataBase, "TestDrinkList", "TestConsumedList");

        //Add drink to be consumed
        StoredData.addData("TERRIER_FUEL", 9000);

        //Add consume record
        float time = (float) System.currentTimeMillis();
        StoredData.addData("TERRIER_FUEL", 9000, time);

        Cursor cConsumedList = StoredData.getDatabase().rawQuery("SELECT * FROM TestConsumedList WHERE `Name`='TERRIER_FUEL'", null);
        cConsumedList.moveToFirst();

        assertThat(cConsumedList.getCount(), is(1));
        Log.d(TAG + "-ADC", "PASS Consumption Record Created");

        assertThat(cConsumedList.getString(1), is("TERRIER_FUEL"));
        Log.d(TAG + "-ADC", "PASS Consumed Drink Name");

        assertThat(cConsumedList.getInt(2), is(9000));
        Log.d(TAG + "-ADC", "PASS Consumed Caffeine Content");

        assertThat(cConsumedList.getFloat(3), is(time));
        Log.d(TAG + "-ADC", "PASS Consumption Time");

        Log.d(TAG + "-ADC", "COMP All addData() (consumed drink list) tests passed!");
    }

    @Test
    public void returnRow() throws Exception {
        //"example function. never used. No need to test."
        Log.d(TAG + "-RRW", "SKIP Intentionally skipping StoredData.returnRow() tests");
    }

    @Test
    public void insertIntoTable() throws Exception {
        //No current implementation or usages
        Log.d(TAG + "-IIT", "SKIP Intentionally skipping StoredData.insertIntoTable() tests");
    }

    @Test
    public void selectCaffine() throws Exception {
        Log.d(TAG + "-SCF", "STAR Begin testing of StoredData.selectCaffine()");
        StoredData.createTables(DataBase, "TestDrinkList", "TestConsumedList");

        StoredData.addData("TERRIER_FUEL", 9000);

        Cursor cDrinkList = StoredData.getDatabase().rawQuery("SELECT * FROM TestDrinkList WHERE `Name`='TERRIER_FUEL'", null);
        cDrinkList.moveToFirst();

        assertThat(cDrinkList.getCount(), is(1));
        //Log.d(TAG + "-ADL", "PASS New Drink Created");

        assertThat(cDrinkList.getString(1), is("TERRIER_FUEL"));
        //Log.d(TAG + "-ADL", "PASS New Drink Name");

        //1 is added to index parameter, so we subtract here
        int drinkIndex = cDrinkList.getInt(0);// - 1;
        float time = (float) System.currentTimeMillis() - 20000;

        StoredData.selectCaffine(drinkIndex, time);

        // And from here, we can use the exact same testing code as addData() (consumed drinks)
        Cursor cConsumedList = StoredData.getDatabase().rawQuery("SELECT * FROM TestConsumedList WHERE `Name`='TERRIER_FUEL'", null);
        cConsumedList.moveToFirst();

        assertThat(cConsumedList.getCount(), is(1));
        Log.d(TAG + "-SCF", "PASS Consumption Record Created");

        assertThat(cConsumedList.getString(1), is("TERRIER_FUEL"));
        Log.d(TAG + "-SCF", "PASS Consumed Drink Name");

        assertThat(cConsumedList.getInt(2), is(9000));
        Log.d(TAG + "-SCF", "PASS Consumed Caffeine Content");

        assertThat(cConsumedList.getFloat(3), is(time));
        Log.d(TAG + "-SCF", "PASS Consumption Time");

        Log.d(TAG + "-SCF", "COMP All selectCaffine() tests passed!");
    }

    @Test
    public void getNumberOfRows() throws Exception {
        Log.d(TAG + "-GNR", "STAR Begin testing of StoredData.getNumberOfRows()");
        StoredData.createTables(DataBase, "TestDrinkList", "TestConsumedList");

        //Add drink to be consumed
        StoredData.addData("TERRIER_FUEL", 9000);

        assertThat(StoredData.getDatabase().rawQuery("SELECT * FROM TestDrinkList", null).getCount(), is(StoredData.getNumberOfRows("TestDrinkList")));
        Log.d(TAG + "-GNR", "PASS Row Qty Match");

        Log.d(TAG + "-GNR", "COMP All getNumberOfRows() tests passed!");
    }

    @Test
    public void print() throws Exception {
        //"more of a legacy debugging function. not needed."
        Log.d(TAG + "-PRT", "SKIP Intentionally skipping StoredData.print() tests");
    }

    @Test
    public void getName() throws Exception {
        Log.d(TAG + "-GNM", "STAR Begin testing of StoredData.getName()");
        StoredData.createTables(DataBase, "TestDrinkList", "TestConsumedList");

        assertThat(StoredData.getName(0), is("Coffee"));
        assertThat(StoredData.getName(6), is("Test"));
        Log.d(TAG + "-GTM", "PASS Spot checks");

        Log.d(TAG + "-GNM", "COMP All getName() tests passed!");
    }

    @Test
    public void getTime() throws Exception {
        Log.d(TAG + "-GTM", "STAR Begin testing of StoredData.getTime()");
        StoredData.createTables(DataBase, "TestDrinkList", "TestConsumedList");

        //Add consume record
        float time = (float) System.currentTimeMillis();
        StoredData.addData("TERRIER_FUEL", 9000, time);

        assertThat(StoredData.getTime(1), is((double) time));
        Log.d(TAG + "-GTM", "PASS Time check");

        Log.d(TAG + "-GTM", "COMP All getTime() tests passed!");
    }

    @Test
    public void getConsumedCaffine() throws Exception {
        Log.d(TAG + "-GCC", "STAR Begin testing of StoredData.getConsumedCaffine()");
        StoredData.createTables(DataBase, "TestDrinkList", "TestConsumedList");

        //Add consume record
        float time = (float) System.currentTimeMillis();
        StoredData.addData("TERRIER_FUEL", 9000, time);

        assertThat(StoredData.getConsumedCaffine(1), is(9000));
        Log.d(TAG + "-GCC", "PASS Caffeine level check");

        Log.d(TAG + "-GCC", "COMP All getConsumedCaffine() tests passed!");
    }

    @Test
    public void timeTable() throws Exception {
        //Does little other than call getTime, so no need to test
        Log.d(TAG + "-TTB", "SKIP Intentionally skipping StoredData.timeTable() tests");
    }

    @Test
    public void caffineTable() throws Exception {
        //Does little other than call getConsumedCaffine, so no need to test
        Log.d(TAG + "-CTB", "SKIP Intentionally skipping StoredData.caffineTable() tests");
    }

    @Test
    public void addDefaultCaffineList() throws Exception {
        //Does nothing other than call addData, so no need to test
        Log.d(TAG + "-AFL", "SKIP Intentionally skipping StoredData.addDefaultCaffineList() tests");
    }

    @Test
    public void getDatabase() throws Exception {
        Log.d(TAG + "-GDB", "STAR Begin testing of StoredData.getDatabase()");
        StoredData.createTables(DataBase, "TestDrinkList", "TestConsumedList");

        assertThat(StoredData.getDatabase(), is(DataBase));
        Log.d(TAG + "-GDB", "PASS Return DB");

        Log.d(TAG + "-GDB", "COMP All getDatabase() tests passed!");

    }

}