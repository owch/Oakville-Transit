package bowenowen.oakvilletransit.Fragments.Home;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.text.format.Time;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class FavouriteAdapter extends BaseAdapter {
    List<Stop> stopArrayList;
    DBHelper db;
    Activity activity;
    String nextBus;

    public FavouriteAdapter(Activity a)
    {
        db= new DBHelper(a, 1);
        stopArrayList = db.getAllStops("favourites");
        activity = a;
        nextBus = "";
    }

    public FavouriteAdapter(Activity a, String query)
    {
        db= new DBHelper(a, 1);
        stopArrayList = getStops(query);
        activity = a;
        nextBus = "";
    }

    @Override
    public int getCount() {
        return stopArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return stopArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.favourite_row, viewGroup, false);


        TextView thumb = (TextView)row.findViewById(R.id.thumbImage);
        TextView title = (TextView)row.findViewById(R.id.stop_title);
        TextView description = (TextView)row.findViewById(R.id.decription);
        TextView number = (TextView)row.findViewById(R.id.stop_logo_num);
        ImageButton delete = (ImageButton)row.findViewById(R.id.remove_fav_button);

        final Stop selectedStop = stopArrayList.get(position);

        title.setText(selectedStop.getName());
        description.setText(getNextBus(selectedStop));
        number.setText(Integer.toString(selectedStop.getId()));

        thumb.setText(nextBus);


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteFavourite(selectedStop);
                stopArrayList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(activity, "Stop removed from favourites",
                            Toast.LENGTH_SHORT).show();

            }
        });


        return row;
    }

    private String getNextBus(Stop stop)
    {

        List<Pair<Pair<String, String>, Pair<String, String>>> stopTimes = new ArrayList<Pair<Pair<String, String>, Pair<String, String>>>();
        stopTimes = db.getStopTime(Integer.toString(stop.getId()));


        if (stopTimes == null)
        {
            nextBus = "";
            return "";
        }
        else if (stopTimes.size() == 0)
        {
            nextBus = "";
            return  "No more buses today";
        }

        Long difference = 137L;
        Long curDifference = -1L;

        Time now = new Time();
        now.setToNow();


        Date rightNow = convertStringToDate(Integer.toString(now.hour) + ":" + Integer.toString(now.minute)+ ":" +  Integer.toString(now.second));


        for (Pair<Pair<String, String>, Pair<String, String>> st:stopTimes)
        {
            curDifference = minuteDifference(convertStringToDate(st.first.first), rightNow);
            if(curDifference < difference && curDifference > 0)
            {
                difference = curDifference;
                nextBus = getRouteNumber(st.second.second);
            }
        }

        return "Next bus arriving in: " +  Long.toString(difference) + "m";
    }

    private Date convertStringToDate(String time)
    {
        Date date = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
        }

        return date;
    }

    private long minuteDifference(Date date, Date rightNow)
    {

        return getDateDiff(rightNow, date);
    }

    public static long getDateDiff(Date date1, Date date2) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public String getRouteNumber(String trip_id)
    {
        return db.getRouteWithTrip(trip_id);
    }

    private List<Stop> getStops(String query)
    {
        return db.queryStop(query);

    }
}
