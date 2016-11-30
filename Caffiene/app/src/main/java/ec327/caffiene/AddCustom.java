package ec327.caffiene;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddCustom extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_custom);
    }

    public void addNewDrink(View view)
    {
        EditText drinkview = (EditText) findViewById(R.id.custom_drink);
        String drink = drinkview.getText().toString();
        EditText caffieneviewer = (EditText) findViewById(R.id.caffiene_content);
        try
        {
            float caffiene = Float.parseFloat(caffieneviewer.toString());
            database.addNewDrink(drink,caffiene);
        }
        catch (NumberFormatException e)
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter a number for caffiene content",Toast.LENGTH_LONG);
            toast.show();
        }
    }
}