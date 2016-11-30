package ec327.caffiene;

import android.app.Activity;
import android.widget.EditText;
import java.util.ArrayList;

/**
 * Created by trishita on 11/27/2016.
 */
public class dynamicSearch extends Thread
{
    public String typed;
    public static boolean stopflag = false;
    private AddDrink addDrink = new AddDrink();
    @Override
    public void run()
    {
        if (!stopflag)
        {
            //checks if user has started to type in search bar. If so, then start thread that narrows down the searches
            if (!(AddDrink.searchbar.getText().equals(R.string.search))) {
                this.typed = AddDrink.getQuery();
                //updates our list of matches
                database.matchQuery(this.typed);
                //adds toggle buttons for each of them
                addDrink.addResults(AddDrink.matches); //well shit. this doesn't work. Would work if static
            }
            try {
                Thread.sleep(10); //reruns every 10 milliseconds to update the search results
                run();
            } catch (InterruptedException ie) {
                return;
            }
        }
        else
        {
            return;
        }
    }
}
