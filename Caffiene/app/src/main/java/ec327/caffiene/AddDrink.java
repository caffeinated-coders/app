package ec327.caffiene;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Screen for adding a consumed drink record
 *
 * @author Trishita Tiwari
 * @version 1.0
 */
public class AddDrink extends AppCompatActivity {
    public static ArrayList<String> matches;
    public static EditText searchbar;
    public static ArrayList<String> alldrinks;
    private static LinearLayout sublayout;
    public boolean stopflag = false;
    private Thread searchresults;
    private TimePicker timeview;

    public static String getQuery() {
        return searchbar.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_drink);
        searchbar = (EditText) findViewById(R.id.search_bar);
        timeview = (TimePicker) findViewById(R.id.time);
        timeview.setIs24HourView(true);
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

    /**
     * Run on application stop. Safely stops thread
     */
    @Override
    protected void onStop() {
        super.onStop();
        this.stopflag = true;

    }

    /**
     * Callback function for add drink button
     *
     * @param view application view
     */
    public void addDrink(View view) //
    {
        int numresults = sublayout.getChildCount();
        String drink = "unchecked";
        double time;
        for (int i = 0; i < numresults; i++) {
            ToggleButton child = (ToggleButton) sublayout.getChildAt(i);
            if (child.isChecked()) //get isChecked attribute to see if user has checked.
            {
                drink = child.getTextOn().toString();
                break; //ensures we only take into consideration the first checked button
            }
        }
        //if the user hasn't picked a drink, remind him to.

        if (drink.equals("unchecked")) {
            //display message if all drinks unchecked
            Toast toast = Toast.makeText(getApplicationContext(), "Please select a drink", Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        int drinkindex = alldrinks.indexOf(drink);

        //Date today = new Date();
        //SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        //Date timed = dateFormat.parse(timeview.getText().toString());
        //time = timeparsed.getTime();
        //as time is in milliseconds.
        //find time now
        int hour = timeview.getHour();
        int minutes = timeview.getMinute();
        time = hour + minutes / 60;

        database.addDrinktoDB(drinkindex, time);
        Toast toast = Toast.makeText(getApplicationContext(), "Drink Added!", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(getApplicationContext(), HomePage.class);
        startActivity(intent);

    }

    public void addCustom(View view) //callback function for adding custom drink button
    {
        Intent intent = new Intent(this, AddCustom.class);
        startActivity(intent);
    }

    public void checkIfSelected() {
        int numresults = sublayout.getChildCount();
        for (int i = 0; i < numresults; i++) {
            ToggleButton child = (ToggleButton) sublayout.getChildAt(i);
            if (child.isChecked()) //get isChecked attribute to see if user has checked.
            {
                stopflag = true;
                break; //ensures we only take into consideration the first checked button
            }
        }
    }

    public void addResults(final ArrayList<String> drinks) {
        for (int i = 0; i < drinks.size(); i++) {
            final String drink = drinks.get(i);
            final ToggleButton toggleButton = new ToggleButton(this);
            toggleButton.setText(drink);
            toggleButton.setTextColor(Color.WHITE);
            toggleButton.setBackgroundColor(ContextCompat.getColor(this, R.color.button));
            sublayout.addView(toggleButton);
            //set a listener for each toggle button. This changes the color of the button if user clicks on it
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        toggleButton.setBackgroundColor(0x64443737);
                        toggleButton.setChecked(true);
                        toggleButton.setTextOn(drink);
                        toggleButton.setTextColor(Color.WHITE);
                    } else {
                        toggleButton.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.button));
                        toggleButton.setChecked(false);
                        toggleButton.setTextOff(drink);
                        toggleButton.setTextColor(Color.WHITE);
                    }
                }
            });
        }
    }

    public class dynamicSearch extends Thread {
        public String typed;

        @Override
        public void run() {
            String lastTyped = "";
            while (true) {
                checkIfSelected();
                if (!stopflag) {
                    //checks if user has started to type in search bar. If so, then start thread that narrows down the searches
                    if (searchbar.getText().toString().length() != 0 && !searchbar.getText().toString().equals(lastTyped)) {
                        lastTyped = searchbar.getText().toString();
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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addResults(matches);
                            }
                        });
                    }
                    try {
                        Thread.sleep(1000); //reruns every 1000 milliseconds to update the search results

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