package ec327.caffiene;

import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class updatePoint extends Thread {

    DataPoint point = database.getPoint();
    LineGraphSeries<DataPoint> series;
    public static boolean stopflag = false;

    public updatePoint(LineGraphSeries<DataPoint> series)
    {
        this.series = series;
    }

    public void run()
    {

        if (!stopflag)
        {
            this.series.appendData(point,false,40); //display max 40 data points. Scroll to end = true.
            point = database.getPoint(); //update the point every time this runs
            try
            {
                Thread.sleep(60000);
                run(); //keep updating every minute
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
