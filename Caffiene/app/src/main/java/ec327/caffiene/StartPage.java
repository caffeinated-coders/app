package ec327.caffiene;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Accepts user information on first launch
 *
 * @author Trishita Tiwari
 * @version 1.0
 */
public class StartPage extends AppCompatActivity {
    private EditText ageview;
    private EditText weightview;
    private EditText nameview;
    private Spinner genderview;

    /**
     * Run on launch
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start_page);
        ageview = (EditText) findViewById(R.id.age);
        weightview = (EditText) findViewById(R.id.weight);
        nameview = (EditText) findViewById(R.id.name);
        genderview = (Spinner) findViewById(R.id.choose_gender);
    }

    /**
     * Adds user data to persistent storage
     *
     * @param view
     */
    public void submit(View view) {
        // function that adds stuff to the database
        try {
            int age = Integer.parseInt(ageview.getText().toString());
            double weight = Double.parseDouble(weightview.getText().toString());
            String name = nameview.getText().toString();
            String gender = genderview.getSelectedItem().toString();
            database.addInfo(age, weight, name, gender); //the function that adds everything
            //navigate to the homepage after submission
        } catch (NumberFormatException n) //if the user does not enter a number, display a message.
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter a number", Toast.LENGTH_LONG);
            toast.show();
        }
        finish();
        return;
    }

}
