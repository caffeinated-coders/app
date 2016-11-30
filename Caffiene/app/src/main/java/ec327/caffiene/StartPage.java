package ec327.caffiene;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class StartPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_start_page);
    }

    public void submit(View view) {
        // function that adds stuff to the database
        EditText ageview = (EditText) findViewById(R.id.age);
        EditText weightview = (EditText) findViewById(R.id.weight);
        try
        {
            int age = Integer.parseInt(ageview.getText().toString());
            double weight = Double.parseDouble(weightview.getText().toString());
            EditText nameview = (EditText) findViewById(R.id.name);
            String name = nameview.getText().toString();

            Spinner genderview = (Spinner) findViewById(R.id.choose_gender);
            String gender = genderview.getSelectedItem().toString();

            database.addInfo(age,weight,name,gender); //the function that adds everything
            //navigate to the homepage after submission
            Intent intent = new Intent(this, HomePage.class);
            startActivity(intent);
        }
        catch (NumberFormatException n) //if the user does not enter a number, display a message.
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter a number",Toast.LENGTH_LONG);
            toast.show();
        }
        return;
    }

}
