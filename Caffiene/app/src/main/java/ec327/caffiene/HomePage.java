package ec327.caffiene;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class HomePage extends AppCompatActivity
{
    private GraphView graph;
    private DataPoint[] datapts;
    private LineGraphSeries<DataPoint> series;
    private Thread add;
    public boolean stopflag = false;
    int color = 0xC8E8DEDE;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home_page);
        graph = (GraphView) findViewById(R.id.graph);
        datapts = database.getData(0,24);
        series = new LineGraphSeries<>(datapts);
        //make it pretty
        series.setColor(color);
        this.graph.setTitle("CAFFIENE CONTENT (mg) vs. TIME (h)");
        this.graph.setTitleColor(color);
        //make it scrollable and scalable
        Viewport port = this.graph.getViewport();
        port.isScalable();
        port.isScrollable();
        port.setMinX(0);
        port.setMaxX(24);
        GridLabelRenderer rendrer = this.graph.getGridLabelRenderer();
        //setting titles and colors
        rendrer.setHorizontalAxisTitle("TIME (h)");
        rendrer.setHorizontalAxisTitleColor(color);
        rendrer.setVerticalAxisTitleColor(color);
        rendrer.setVerticalAxisTitle("CAFFIENE CONTENT (mg)");
        rendrer.setHorizontalLabelsColor(color);
        rendrer.setVerticalLabelsColor(color);
        //display the content
        add = new Thread(new updatePoint(series));
        //check if user is new:

        boolean isNew = database.newUser();
        if (isNew) //if new, we navigate to StartPage to allow user to enter info.
        {
            Intent intent = new Intent(this, StartPage.class);
            startActivity(intent);
        }

        //graph the values in the database
        graph.addSeries(series);
        //keep updating the graph via this thread
        add.start();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        this.stopflag = true;

    }


    //callback function for the add button
    public void add(View view)
    {
        Intent intent = new Intent(this,AddDrink.class);
        startActivity(intent);
    }

    public class updatePoint extends Thread {

        DataPoint point = database.getPoint();
        LineGraphSeries<DataPoint> series;

        public updatePoint(LineGraphSeries<DataPoint> series)
        {
            this.series = series;
        }

        public void run()
        {
            while (true)
            {
                if (!stopflag)
                {
                    /*
                    System.out.println("X: " + point.getX() + ", Y: " + point.getY());
                    this.series.appendData(point,false,40); //display max 40 data points. Scroll to end = true.

                    point = database.getPoint(); //update the point every time this
                    */
                    try
                    {
                        Thread.sleep(6000);
                    }
                    catch (InterruptedException ie)
                    {
                        return;
                    }
                }
                else
                {
                    return;
                }
            }
        }
    }


}

