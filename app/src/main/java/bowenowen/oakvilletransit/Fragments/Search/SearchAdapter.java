package bowenowen.oakvilletransit.Fragments.Search;

import android.app.Activity;
import android.content.Context;
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
import bowenowen.oakvilletransit.Model.Stop;
import bowenowen.oakvilletransit.Model.Trip;
import bowenowen.oakvilletransit.R;
import bowenowen.oakvilletransit.SearchActivity;

/**
 * Created by owenchen on 15-05-11.
 */
public class SearchAdapter extends BaseAdapter{

    List<Stop> stopArrayList;
    DBHelper db;
    Activity activity;

    public SearchAdapter(Activity a, String query)
    {
        db = new DBHelper(a, 1);
        stopArrayList = getStops(query);
        activity = a;
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.stop_search_row, viewGroup, false);

        TextView title = (TextView)row.findViewById(R.id.stop_title);
        TextView number = (TextView)row.findViewById(R.id.stop_logo_num);
        TextView description = (TextView)row.findViewById(R.id.decription);

        final Stop selectedStop = stopArrayList.get(position);

        String nextBus = getNextBus(selectedStop);

        description.setText(nextBus);

        title.setText(selectedStop.getName());
        number.setText(Integer.toString(selectedStop.getId()));

        final ImageButton favBtn = (ImageButton)row.findViewById(R.id.add_fav_button);

        setImage(favBtn, selectedStop);

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.getStop(Integer.toString(selectedStop.getId()), "favourites") == null)
                {
                    db.insertStop(selectedStop,"favourites");
                    favBtn.setImageResource(R.drawable.star_on);
                    Toast.makeText(activity, "Stop Added to favourites",
                            Toast.LENGTH_SHORT).show();
                }
                else
                {
                    db.deleteFavourite(selectedStop);
                    favBtn.setImageResource(R.drawable.star_off);
                    Toast.makeText(activity, "Stop removed from favourites",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return row;
    }

    private List<Stop> getStops(String query)
    {
            return db.queryStop(query);

    }

    private void setImage(ImageButton imageButton, Stop stop) {
        int i = 0;

        if (db.getStop(Integer.toString(stop.getId()), "favourites") == null)
        {
            imageButton.setImageResource(R.drawable.star_off);
        }
        else{
            imageButton.setImageResource(R.drawable.star_on);
        }
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

    private String getNextBus(Stop stop)
    {

        List<Pair<Pair<String, String>, Pair<String, String>>> stopTimes = new ArrayList<Pair<Pair<String, String>, Pair<String, String>>>();
        stopTimes = db.getStopTime(Integer.toString(stop.getId()));


        if (stopTimes == null)
        {

            return " ";
        }
        else if (stopTimes.size() == 0)
        {
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
            }
        }

        return "Next bus arriving in: " +  Long.toString(difference) + "m";
    }
}
