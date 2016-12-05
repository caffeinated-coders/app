package ec327.caffiene;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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


public class HomePage extends AppCompatActivity {
    private GraphView graph;
    private DataPoint[] datapts;
    private LineGraphSeries<DataPoint> series;
    private Thread add;
    private Viewport port;
    public boolean stopflag = false;
    int color = 0xC8E8DEDE;
    LineGraphSeries<DataPoint> nowseries;
    float hour;
    float minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_page);
        graph = (GraphView) findViewById(R.id.graph);
        graph.setTitle("CAFFIENE CONTENT (mg) vs. TIME (h)");
        graph.setTitleColor(color);
        //plot data
        datapts = database.getData();
        series = new LineGraphSeries<>(datapts);
        //make it pretty
        series.setColor(color);
        //make it scrollable and scalable
        port = graph.getViewport();
        port.setScalableY(true);
        port.setScrollable(true);
        port.setScrollableY(true);
        port.setScrollable(true);
        port.setXAxisBoundsManual(true);
        port.setYAxisBoundsManual(true);
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
                        new DataPoint(0, 10),
                        new DataPoint(24, 10)
                });
        lethal.setColor(Color.RED);
        //graph lethal line
        graph.addSeries(lethal);
        //graph the values in the database
        graph.addSeries(series);
        //check if user is new:
        boolean isNew = database.newUser();
        if (isNew) //if new, we navigate to StartPage to allow user to enter info.
        {
            Intent intent = new Intent(this, StartPage.class);
            startActivity(intent);
        }
        add = new Thread(new updatePoint());
        add.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.stopflag = true;

    }


    //callback function for the add button
    public void add(View view) {
        Intent intent = new Intent(this, AddDrink.class);
        startActivity(intent);
    }

    public class updatePoint extends Thread {
        //keep updating the graph via this thread
        public void run() {
            while (true) {
                if (!stopflag) {
                    if (nowseries != null)
                    {
                        graph.removeSeries(nowseries);
                    }
                    Log.d("thread", "ran thread");
                    //get data for the now line
                    Date time = new Date();
                    Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                    calendar.setTime(time);   // assigns calendar to given date
                    hour = calendar.get(Calendar.HOUR_OF_DAY);
                    minutes = calendar.get(Calendar.MINUTE);
                    float now = hour + (minutes / 60);
                    //reset ranges
                    port.setMinX(now - 4);
                    port.setMaxX(now + 4);
                    port.setMaxY(12);
                    //set the now line
                    nowseries = new LineGraphSeries<>(new DataPoint[]
                            {
                                    new DataPoint(now, 0),
                                    new DataPoint(now, 12)
                            });
                    graph.addSeries(nowseries);
                    try
                    {
                        Thread.sleep(6000);
                    } catch (InterruptedException ie) {
                        return;
                    }
                } else {
                    return;
                }
            }

        }
    }
}

