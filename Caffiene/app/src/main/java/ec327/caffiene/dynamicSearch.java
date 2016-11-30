package ec327.caffiene;

import android.widget.EditText;
import java.util.ArrayList;

/**
 * Created by trishita on 11/27/2016.
 */
public class dynamicSearch extends Thread
{
    public String typed;
    public ArrayList<String> matches;

    @Override
    public void run()
    {
        AddDrink addDrink = new AddDrink();
        //checks if user has started to type in search bar. If so, then start thread that narrows down the searches
        if (!addDrink.searchbar.getText().toString().equals(R.string.search)) {
            this.typed = addDrink.getQuery();
            //updates our list of matches
            database.matchQuery(this.typed);
            //adds toggle buttons for each of them
            addDrink.addResults(AddDrink.matches);
        }
        try
        {
            Thread.sleep(10); //reruns every 10 seconds to update the search results
            run();
        }
        catch (InterruptedException ie)
        {
            return;
        }
    }
}
