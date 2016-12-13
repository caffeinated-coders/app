package ec327.caffiene;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Screen for adding a custom drink
 *
 * @author Trishita Tiwari
 * @version 1.0
 */
public class AddCustom extends AppCompatActivity {

    /**
     * Run on activity launch
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_custom);
    }

    /**
     * Add a new drink to the DB
     *
     * @param view
     */
    public void addNewDrink(View view) {
        EditText drinkview = (EditText) findViewById(R.id.custom_drink);
        String drink = drinkview.getText().toString();
        EditText caffieneviewer = (EditText) findViewById(R.id.caffiene_content);
        try {
            float caffiene = Float.parseFloat(caffieneviewer.getText().toString());
            database.addNewDrink(drink, caffiene);

            //go to homepage
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent);

            Toast toast = Toast.makeText(getApplicationContext(), "Drink Added!", Toast.LENGTH_SHORT);
            toast.show();
        } catch (NumberFormatException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter a number for caffiene content", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
