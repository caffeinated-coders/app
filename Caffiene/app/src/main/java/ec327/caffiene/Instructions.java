package ec327.caffiene;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Shows instructions for app usage
 *
 * @author Trishita Tiwari
 * @version 1.0
 */
public class Instructions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
    }

    public void goHome(View view) {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}
