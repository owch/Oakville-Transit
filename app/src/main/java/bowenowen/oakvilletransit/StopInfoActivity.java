package bowenowen.oakvilletransit;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ListView;

import bowenowen.oakvilletransit.Fragments.Home.FavouriteListFragment;
import bowenowen.oakvilletransit.Fragments.StopInfoAdapter;

/**
 * Created by owenchen on 15-05-10.
 */
public class StopInfoActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stop_info);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        int stopNumber = b.getInt("stopNumber");

        setAdapter(stopNumber);

        setTitle("Upcoming Buses for " + Integer.toString(stopNumber));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setAdapter(int stopNumber)
    {
        final ListView listView = (ListView) findViewById(R.id.stop_info_list);
        listView.setAdapter(new StopInfoAdapter(this, stopNumber));
    }
}
