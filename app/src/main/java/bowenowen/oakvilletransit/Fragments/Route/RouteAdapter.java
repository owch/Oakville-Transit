package bowenowen.oakvilletransit.Fragments.Route;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import bowenowen.oakvilletransit.DBHelper;
import bowenowen.oakvilletransit.MainActivity;
import bowenowen.oakvilletransit.Model.Route;
import bowenowen.oakvilletransit.R;



/**
 * Created by owenchen on 15-04-25.
 */
public class RouteAdapter extends BaseAdapter {

    List<Route> routeArrayList;
    DBHelper db;
    Activity activity;

    public RouteAdapter(Activity a)
    {
        db = new DBHelper(a, 1);
        routeArrayList = getRoutes();
        activity = a;
    }

    @Override
    public int getCount() {
        return routeArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return routeArrayList.get(position);
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

        Route selectedRoute = routeArrayList.get(position);

        title.setText(selectedRoute.getName());
        description.setText("");

        Resources res = activity.getResources();
        thumb.setText(selectedRoute.getNumber());

        return row;
    }

    private List<Route> getRoutes()
    {
        return db.getAllRoutes();
    }
}
