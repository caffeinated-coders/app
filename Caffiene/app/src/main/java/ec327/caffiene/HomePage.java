package ec327.caffiene;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * "Main class" of the app. Mainly handles displaying the graph and action buttons. Landing page
 * for returning users.
 *
 * @author Trishita Tiwari
 * @version 1.0
 */
public class HomePage extends AppCompatActivity {
    public static LineGraphSeries<DataPoint> series;
    public static float now;
    public static SharedPreferences preferences;
    public boolean stopflag = false;
    int color = 0xC8E8DEDE;
    LineGraphSeries<DataPoint> nowseries;
    SQLiteDatabase DataBase;
    private GraphView graph;
    private DataPoint[] datapts;
    private Thread add;
    private Viewport port;
    private float hour;
    private float minutes;

    /**
     * Called upon application launch. "Constructs" front- and back-end resources
     *
     * @param savedInstanceState if "waking up" app from memory
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_page);
        graph = (GraphView) findViewById(R.id.graph);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        graph.setTitle("Here's your graph, " + database.getName());
        graph.setTitleColor(color);

        //make it scrollable and scalable
        port = graph.getViewport();
        port.setScalableY(true);
        port.setScrollable(true);
        port.setScrollableY(true);
        port.setScrollable(true);
        port.setXAxisBoundsManual(true);
        port.setYAxisBoundsManual(true);
        //set ranges
        Date time = new Date();
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(time);   // assigns calendar to given date
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minutes = calendar.get(Calendar.MINUTE);
        now = hour + (minutes / 60);

        port.setMinX(now - 4);
        port.setMaxX(now + 4);
        port.setMaxY(600);
        GridLabelRenderer rendrer = graph.getGridLabelRenderer();
        //setting titles and colors
        rendrer.setHorizontalAxisTitle("TIME (h)");
        rendrer.setHorizontalAxisTitleColor(color);
        rendrer.setVerticalAxisTitleColor(color);
        rendrer.setVerticalAxisTitle("CAFFIENE CONTENT (mg)");
        rendrer.setHorizontalLabelsColor(color);
        rendrer.setVerticalLabelsColor(color);
        LineGraphSeries<DataPoint> lethal = new LineGraphSeries<DataPoint>(new DataPoint[]
                {
                        new DataPoint(0, 500),
                        new DataPoint(24, 500)
                });
        LineGraphSeries<DataPoint> nervous = new LineGraphSeries<DataPoint>(new DataPoint[]
                {
                        new DataPoint(0, 400),
                        new DataPoint(24, 400)
                });
        //setting colors
        nervous.setColor(Color.YELLOW);
        lethal.setColor(Color.RED);
        //graph the benchmanrks
        graph.addSeries(lethal);
        graph.addSeries(nervous);

        boolean isNewUser = !getDatabasePath("coffeeData").exists();

        if (isNewUser) {
            //Create a new DB and populate it
            DataBase = openOrCreateDatabase("coffeeData", MODE_PRIVATE, null);
            StoredData.createTables(DataBase, "CaffineList", "ConsumedCaffine");
            //Navigate to StartPage to allow user to enter info.
            Intent intent = new Intent(this, StartPage.class);
            startActivity(intent);
        } else {
            DataBase = openOrCreateDatabase("coffeeData", MODE_PRIVATE, null);
            StoredData.myDB = DataBase;
            StoredData.beverageListTableName = "CaffineList";
            StoredData.consumptionListTableName = "ConsumedCaffine";
        }

        //plotting data
        Thread plotPoints = new Thread(new plotCaffeineLevelThread());
        plotPoints.start();

        add = new Thread(new updateNowLineThread());
        add.start();
    }

    /**
     * Called upon application close. Allows the threads to end gracefully
     */
    @Override
    protected void onStop() {
        super.onStop();
        this.stopflag = true;

    }


    /**
     * "Add" button callback. Launches the AddDrink screen
     *
     * @param view application view
     */
    public void add(View view) {
        Intent intent = new Intent(this, AddDrink.class);
        startActivity(intent);
    }

    /**
     * "Help" button callback. Launches the Instructions screen
     *
     * @param view application view
     */
    public void instructions(View view) {
        Intent intent = new Intent(this, Instructions.class);
        startActivity(intent);
    }

    /**
     * Thread: Manages the updating of the graph's "now" vertical line.
     *
     * @author Trishita Tiwari
     * @version 1.0
     */
    public class updateNowLineThread extends Thread {

        /**
         * Thread runner: Move the now line every 60 seconds
         */
        public void run() {
            while (true) {
                if (!stopflag) {
                    if (nowseries != null) {
                        graph.removeSeries(nowseries);
                    }
                    Log.d("thread", "ran thread");
                    //get data for the now line
                    Date time = new Date();
                    Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                    calendar.setTime(time);   // assigns calendar to given date
                    hour = calendar.get(Calendar.HOUR_OF_DAY);
                    minutes = calendar.get(Calendar.MINUTE);
                    now = hour + (minutes / 60);
                    //set the now line
                    nowseries = new LineGraphSeries<>(new DataPoint[]
                            {
                                    new DataPoint(now, 0),
                                    new DataPoint(now, 600)
                            });
                    graph.addSeries(nowseries);
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException ie) {
                        return;
                    }
                } else {
                    return;
                }
            }

        }
    }

    /**
     * Thread: Manages plotting of the actual caffeine content data points on the graph
     *
     * @author Trishita Tiwari
     * @version 1.0
     */
    public class plotCaffeineLevelThread extends Thread {

        /**
         * Thread runner: get the data from the back-end and plot it
         */
        @Override
        public void run() {
            datapts = database.getData(0, 24);
            series = new LineGraphSeries<>(datapts);
            //make it pretty
            series.setColor(color);
            //graph the values in the database
            graph.addSeries(series);
        }
    }
}

