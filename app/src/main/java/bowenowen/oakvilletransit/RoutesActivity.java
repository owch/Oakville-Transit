package bowenowen.oakvilletransit;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import bowenowen.oakvilletransit.Fragments.NearestFragment;
import bowenowen.oakvilletransit.Fragments.Route.RouteAdapter;
import bowenowen.oakvilletransit.Fragments.Route.RouteFragment;
import bowenowen.oakvilletransit.Fragments.Route.RouteInfoAdapter;
import bowenowen.oakvilletransit.Fragments.Route.RouteInfoFragment;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by owenchen on 15-05-09.
 */
public class RoutesActivity extends Activity {
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_routes);

        setTitle("All Routes");

        switchFragment(new RouteFragment());

        mDrawerList = (ListView)findViewById(R.id.navList);
        addDrawerItems();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle("All Routes");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle("Navigation");
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                if (position == 0) {
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position == 1) {
                    intent = new Intent(getApplicationContext(), SearchActivity.class);
                    startActivity(intent);
                    finish();
                } else if (position == 2)
                {
                    intent = new Intent(getApplicationContext(), ClosestActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });


    }

    private void switchFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.route_fragment, fragment).commit();
    }

    public void setRouteAdapter(ListView listView)
    {
        listView.setAdapter(new RouteAdapter(this));
    }
    public void setRouteInfoAdapter(ListView listView, String route_number)
    {
        listView.setAdapter(new RouteInfoAdapter(this, route_number));
    }

    public void checkSegement(int id)
    {
        SegmentedGroup segmentedControl = (SegmentedGroup) findViewById(R.id.segmented2);
        segmentedControl.check(id);
    }

    public void changeFragment()
    {
        DBHelper db = new DBHelper(this, 1);

        String routeNumber = db.getCurrentRoute();
        setTitle("Stops for Route " + routeNumber);
        if(routeNumber != null)
        {
            RouteInfoFragment infoFragment = new RouteInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString("route_number", routeNumber);
            infoFragment.setArguments(bundle);
            RouteFragment routeFragment = (RouteFragment)getFragmentManager().findFragmentById(R.id.route_fragment);
            routeFragment.switchFragment(infoFragment);
        }
        else
        {
            RouteFragment routeFragment = (RouteFragment)getFragmentManager().findFragmentById(R.id.route_fragment);
            routeFragment.switchFragment(new NearestFragment());
            setTitle("No Route Selected");
        }
    }

    private void addDrawerItems() {
        String[] menuItem = { "Favourite Stops", "Search Stops", "Closest Stops"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuItem);
        mDrawerList.setAdapter(mAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
}
