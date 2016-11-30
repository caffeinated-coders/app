package ec327.caffiene;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class updatePoint extends Thread {

    DataPoint point; //replace with actual data from the database //need method for this
    LineGraphSeries<DataPoint> series;

    public updatePoint(LineGraphSeries<DataPoint> series) {
        this.series = series;
    }

    public void run()
    {
        series.appendData(point,true,40); //display max 40 data points. Scroll to end = true.
        point = database.getPoint();
        try
        {
            Thread.sleep(1000);
            run(); //keep updating every minute
        }
        catch (InterruptedException ie)
        {
            return;
        }
    }
}
