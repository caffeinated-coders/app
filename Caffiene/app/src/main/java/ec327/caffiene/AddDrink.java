package ec327.caffiene;


import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddDrink extends AppCompatActivity {
    private Thread searchresults;
    public static ArrayList<String> matches;
    private static LinearLayout sublayout;
    public static EditText searchbar;
    public static ArrayList<String> alldrinks;                  //made this static too
    public boolean stopflag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_drink);
        searchbar = (EditText) findViewById(R.id.search_bar);
        searchresults = new Thread(new dynamicSearch());
        //matches initially contains all possible drinks
        alldrinks = database.allDrinks();
        matches = database.allDrinks(); //create a new array to copy
        //sublayout which contains all buttons
        sublayout = (LinearLayout) findViewById(R.id.search_results);
        //buttons for each of these drinks is initially displayed
        addResults(matches);
        //start the thread that updates the search results
        searchresults.start();
    }


    @Override
    protected void onStop()
    {
        super.onStop();
        this.stopflag = true;

    }

    public void addDrink(View view) //callback function for add drink button
    {
        int numresults = sublayout.getChildCount();
        String drink = "unchecked";
        float time;
        for (int i = 0; i < numresults; i++)
        {
            ToggleButton child = (ToggleButton) sublayout.getChildAt(i);
            if (child.isChecked()) //get isChecked attribute to see if user has checked.
            {
                drink = child.getTextOn().toString();
                break; //ensures we only take into consideration the first checked button
            }
        }
        //if the user hasn't picked a drink, remind him to.

        if (drink.equals("unchecked"))
        {
            //display message if all drinks unchecked
            Toast toast = new Toast(this);
            toast.makeText(this,"Please select a drink",Toast.LENGTH_LONG);
            return;
        }
        int drinkindex = alldrinks.indexOf(drink);
        CheckBox drinknowbutton = (CheckBox) findViewById(R.id.drink_now);

        if (drinknowbutton.isChecked())
        {
            //time = (new Date()).getTime()/1000; //this "time" didn't corrolate with the time in other files
            //time = (time / (60 * 60 * 1000));  //this doesnt work
            Date timed = new Date();
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(timed);   // assigns calendar to given date
            float hour = calendar.get(Calendar.HOUR_OF_DAY);
            float minutes = calendar.get(Calendar.MINUTE);
            time = hour + (minutes / 60);
        }
        else
        {
            EditText timeview = (EditText) findViewById(R.id.time);
            try
            {
                //Date today = new Date();
                //SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                //Date timed = dateFormat.parse(timeview.getText().toString());
                //time = timeparsed.getTime();

                 //as time is in milliseconds.
                //find time now
                Date timed = new Date();
                Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
                calendar.setTime(timed);   // assigns calendar to given date
                float hour = calendar.get(Calendar.HOUR_OF_DAY);
                float minutes = calendar.get(Calendar.MINUTE);
                time = hour + (minutes / 60) +  Float.parseFloat(timeview.getText().toString());

            }
            catch (Exception p)
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Please enter a number",Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }

        database.addDrinktoDB(drinkindex,time);
        Intent intent = new Intent(getApplicationContext(),HomePage.class);
        startActivity(intent);

    }

    public static String getQuery()
    {
        return searchbar.getText().toString();
    }

    public void addCustom(View view) //callback function for adding custom drink button
    {
        Intent intent = new Intent(this,AddCustom.class);
        startActivity(intent);
    }

    public void checkIfSelected()
    {
        int numresults = sublayout.getChildCount();
        for (int i = 0; i < numresults; i++)
        {
            ToggleButton child = (ToggleButton) sublayout.getChildAt(i);
            if (child.isChecked()) //get isChecked attribute to see if user has checked.
            {
                stopflag = true;
                break; //ensures we only take into consideration the first checked button
            }
        }
    }

    public void addResults(final ArrayList<String> drinks) {
        for (int i = 0; i < drinks.size(); i++)
        {
            final String drink = drinks.get(i);
            final ToggleButton drinkview = new ToggleButton(this);
            drinkview.setText(drink);
            drinkview.setTextColor(Color.WHITE);
            drinkview.setBackgroundColor(ContextCompat.getColor(this, R.color.button));
            sublayout.addView(drinkview);
            //set a listener for each toggle button. This changes the color of the button if user clicks on it
            drinkview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        drinkview.setBackgroundColor(0x64443737);
                        drinkview.setChecked(true);
                        drinkview.setTextOn(drink);
                        drinkview.setTextColor(Color.WHITE);
                    }
                    else
                    {
                        drinkview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.button));
                        drinkview.setChecked(false);
                        drinkview.setTextOff(drink);
                        drinkview.setTextColor(Color.WHITE);
                    }
                }
            });
        }
    }
    public class dynamicSearch extends Thread
    {
        public String typed;
        @Override
        public void run()
        {
            while (true)
            {
                checkIfSelected();
                if (!stopflag) {
                    //checks if user has started to type in search bar. If so, then start thread that narrows down the searches
                    if (searchbar.getText().toString().length() != 0)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                sublayout.removeAllViews();

                            }
                        });
                        this.typed = AddDrink.getQuery();
                        //updates our list of matches
                        database.matchQuery(this.typed);
                        //adds toggle buttons for each of them
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {

                                addResults(matches);
                            }
                        });
                    }
                    try
                    {
                        Thread.sleep(1000); //reruns every 10 milliseconds to update the search results
                    } catch (InterruptedException ie)
                    {
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }

}
