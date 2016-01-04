package bowenowen.oakvilletransit;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import bowenowen.oakvilletransit.Fragments.Route.RouteAdapter;
import bowenowen.oakvilletransit.Fragments.Route.RouteInfoAdapter;
import bowenowen.oakvilletransit.Model.Route;
import bowenowen.oakvilletransit.Model.Stop;
import bowenowen.oakvilletransit.Model.StopTimes;
import bowenowen.oakvilletransit.Model.Trip;



public class MainActivity extends ActionBarActivity{
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDB();
        startHomeActivity();

    }

    private void createDB()
        {
        getApplicationContext().deleteDatabase("transit.sqlite");

        File database=getApplicationContext().getDatabasePath("transit.sqlite");

        if(!database.exists())
        {
            db = new DBHelper(getApplicationContext(), 1);
            database.getParentFile().mkdirs();
            copyDatabase();

            //manuallyPoplateDatebase();
        }
    }

    private void parseRoutes(DBHelper db)
    {
        AssetManager am = this.getAssets();

        try
        {
            InputStream is = am.open("routes.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            String[] parsedLine;

            line = br.readLine();

            while (line != null)
            {
                parsedLine = line.split(",");
                Route route = new Route(parsedLine[2], parsedLine[3], parsedLine[1]);

                db.insertRoute(route);
                line = br.readLine();
           }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseStops(DBHelper db)
    {
        AssetManager am = this.getAssets();

        try
        {
            InputStream is = am.open("stops.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            String[] parseLine;

            line = br.readLine();

            while (line != null)
            {
                parseLine = line.split(",");
                Stop stop = new Stop(Integer.parseInt(parseLine[0]),parseLine[1], Double.parseDouble(parseLine[2]), Double.parseDouble(parseLine[3]));

                db.insertStop(stop, "stops");
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseTrips(DBHelper db)
    {
        AssetManager am = this.getAssets();

        try
        {
            InputStream is = am.open("trips.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            String[] parseLine;

            line = br.readLine();

            while (line != null)
            {
                parseLine = line.split(",");
                Trip trip = new Trip(Integer.parseInt(parseLine[2]),parseLine[0],parseLine[1]);

                db.insertTrip(trip);
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseStopTime(DBHelper db)
    {
        AssetManager am = this.getAssets();

        try
        {
            InputStream is = am.open("stop_times.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = br.readLine();
            String[] parseLine;

            line = br.readLine();

            while (line != null)
            {
                parseLine = line.split(",");
                if(!parseLine[1].equals(""))
                {
                    db.insertMainStop(parseLine[3], parseLine[2], parseLine[0]);
                }

                StopTimes stopTimes = new StopTimes(parseLine[0],parseLine[3],parseLine[2], parseLine[1]);

                db.insertStopTime(stopTimes);

                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void manuallyPoplateDatebase()
    {
        parseRoutes(db);
        parseStops(db);
        parseTrips(db);
        parseStopTime(db);
    }

    private void startHomeActivity()
    {
        Intent intent = new Intent(this, HomeActivity.class);

        startActivity(intent);
        finish();
    }

    private void copyDatabase()
    {
        try {
            db.copyDataBase();

        } catch (IOException e) {

            throw new Error("Error copying database");

        }
    }
}
