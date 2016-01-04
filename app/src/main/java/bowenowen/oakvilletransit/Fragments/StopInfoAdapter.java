package bowenowen.oakvilletransit.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.format.Time;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import bowenowen.oakvilletransit.DBHelper;
import bowenowen.oakvilletransit.Model.Route;
import bowenowen.oakvilletransit.Model.Stop;
import bowenowen.oakvilletransit.Model.StopTimes;
import bowenowen.oakvilletransit.Model.Trip;
import bowenowen.oakvilletransit.R;

/**
 * Created by owenchen on 15-05-10.
 */
public class StopInfoAdapter extends BaseAdapter{
    List<Pair<Pair<String, String>, Pair<String, String>>> tripArrayList;
    DBHelper db;
    Activity activity;
    int StopNumber;

    public StopInfoAdapter(Activity a, int stopNumber)
    {
        db = new DBHelper(a, 1);
        tripArrayList = getTrips(stopNumber);
        activity = a;
        StopNumber = stopNumber;
    }

    @Override
    public int getCount() {
        return tripArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return tripArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.route_row, viewGroup, false);

        TextView thumb = (TextView)row.findViewById(R.id.thumbImage);
        TextView title = (TextView)row.findViewById(R.id.route_title);
        TextView description = (TextView)row.findViewById(R.id.route_description);

        Pair<Pair<String, String>, Pair<String, String>> selectedTrip = tripArrayList.get(position);
        Pair<String,String> route = getRouteName(selectedTrip.second.second);


        //db.checkDate(selectedTrip.first.second);

        title.setText("Arrival Time - " + selectedTrip.first.first);
        description.setText(route.first);


        thumb.setText(route.second);

        return row;
    }

    private List<Pair<Pair<String, String>, Pair<String, String>>> getTrips(int stopNumber)
    {
        List<Pair<Pair<String, String>, Pair<String, String>>> st = db.getStopTime(Integer.toString(stopNumber));

        return st;
    }



    private Pair<String, String> getRouteName(String trip_number)
    {
        String routeNumber = db.getRouteWithTrip(trip_number);
        String routeName = db.getRouteName(routeNumber);

        return Pair.create(routeName, routeNumber);

    }
}
