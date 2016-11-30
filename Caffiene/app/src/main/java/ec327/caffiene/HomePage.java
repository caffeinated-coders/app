package ec327.caffiene;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class HomePage extends AppCompatActivity
{
    private GraphView graph = (GraphView) findViewById(R.id.graph);
    private DataPoint[] datapts = database.getData();
    private LineGraphSeries<DataPoint> series = new LineGraphSeries<>(datapts);
    private Thread add = new Thread(new updatePoint(series));

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        add.stop(); //will replace with appropriate function; this is deprecated
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_home_page, menu);
            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings)
            {
                return true;
            }

            return super.onOptionsItemSelected(item);
    }
    //callback function for the add button
    public void add(View view)
    {
        Intent intent = new Intent(this,AddDrink.class);
    }

}

