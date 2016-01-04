package bowenowen.oakvilletransit.Fragments.Route;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bowenowen.oakvilletransit.DBHelper;
import bowenowen.oakvilletransit.Model.Route;
import bowenowen.oakvilletransit.Model.Stop;
import bowenowen.oakvilletransit.Model.Trip;
import bowenowen.oakvilletransit.R;

/**
 * Created by owenchen on 15-05-09.
 */
public class RouteInfoAdapter extends BaseAdapter{

    List<Stop> stopArrayList;
    DBHelper db;
    Activity activity;

    public RouteInfoAdapter(Activity a, String route_number)
    {
        db = new DBHelper(a, 1);
        stopArrayList = getStops(route_number);
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
        final View row = inflater.inflate(R.layout.route_info_row, viewGroup, false);

        TextView title = (TextView)row.findViewById(R.id.stop_title);
        TextView number = (TextView)row.findViewById(R.id.stop_logo_num);

        final Stop selectedStop = stopArrayList.get(position);

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

    private List<Stop> getStops(String route_number)
    {
        List<Stop> stopList = new ArrayList<Stop>();
        Trip trip = db.getTripWithRoute(route_number);

        stopList = db.getStopList(trip.getTripNumber());

        return stopList;
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
}
