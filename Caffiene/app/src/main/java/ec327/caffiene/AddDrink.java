package ec327.caffiene;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddDrink extends AppCompatActivity {
    private Thread searchresults;
    public static ArrayList<String> matches;
    private static LinearLayout sublayout;
    public static EditText searchbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_drink);
        searchbar = (EditText) findViewById(R.id.search_bar);
        searchresults = new Thread(new dynamicSearch());
        sublayout = (LinearLayout) findViewById(R.id.search_results);
        //matches initially contains all possible drinks
        matches = database.allDrinks();
        //buttons for each of these drinks is initially displayed
        addResults(matches);
        //start the thread that updates the search results
        searchresults.start();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        dynamicSearch.stopflag = true; //will replace with appropriate function; this is deprecated
    }

    public void addDrink(View view) //callback function for add drink button
    {
        int numresults = sublayout.getChildCount();
        String drink = "unchecked";
        long time;
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
        CheckBox drinknowbutton = (CheckBox) findViewById(R.id.drink_now);

        if (drinknowbutton.isChecked())
        {
            time = (new Date()).getTime()/1000;
        }
        else
        {
            EditText dateview = (EditText) findViewById(R.id.date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-YYYY");
            try
            {
                java.util.Date Date = dateFormat.parse(dateview.getText().toString());
                time = Date.getTime()/1000; //as time is in milliseconds.

            }
            catch (ParseException p)
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Please enter a number",Toast.LENGTH_LONG);
                toast.show();
                return;
            }
        }
        database.addDrinktoDB(drink,time);
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

    public void addResults(final ArrayList<String> drinks) {
        for (int i = 0; i < database.numDrinks; i++)
        {
            final String drink = drinks.get(i);
            final ToggleButton drinkview = new ToggleButton(this);
            drinkview.setText(drink);
            drinkview.setBackgroundColor(ContextCompat.getColor(this, R.color.button));
            sublayout.addView(drinkview);
            //set a listener for each toggle button. This changes the color of the button if user clicks on it
            drinkview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        drinkview.setBackgroundColor(0xc2c2a3);
                        drinkview.setChecked(true);
                        drinkview.setTextOn(drink);
                        drinkview.setTextColor(0xffffff);
                    }
                    else
                    {
                        drinkview.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.button));
                        drinkview.setTextOff(drink);
                        drinkview.setTextColor(0xffffff);
                    }
                }
            });
        }
    }

}
