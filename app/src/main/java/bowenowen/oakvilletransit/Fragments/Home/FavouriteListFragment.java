package bowenowen.oakvilletransit.Fragments.Home;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bowenowen.oakvilletransit.DBHelper;
import bowenowen.oakvilletransit.HomeActivity;
import bowenowen.oakvilletransit.Model.Route;
import bowenowen.oakvilletransit.Model.Stop;
import bowenowen.oakvilletransit.R;
import bowenowen.oakvilletransit.RoutesActivity;
import bowenowen.oakvilletransit.StopInfoActivity;

/**
 * Created by owenchen on 15-05-10.
 */
public class FavouriteListFragment extends Fragment{
    ListView routeList;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.route_list_fragment, null, false);

        final ListView listView = (ListView) view.findViewById(R.id.route_list);
        ((HomeActivity) getActivity()).setFavouriteAdapter(listView);

            listView.setItemsCanFocus(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;

                Stop stop = (Stop) parent.getItemAtPosition(position);
                DBHelper db = new DBHelper(getActivity(),1);
                List<Pair<Pair<String, String>, Pair<String, String>>> stopTimes = new ArrayList<Pair<Pair<String, String>, Pair<String, String>>>();


                stopTimes = db.getStopTime(Integer.toString(stop.getId()));


                if (stopTimes == null)
                {
                        Toast.makeText(getActivity(), "No data available for this stop. Please refer to the closest flag stop",
                                Toast.LENGTH_LONG).show();
                }
                else
                {
                    intent = new Intent(getActivity(), StopInfoActivity.class);
                    Bundle b = new Bundle();
                    b.putInt("stopNumber",stop.getId());
                    intent.putExtras(b);

                    startActivity(intent);
                }
            }
        });

        return view;
    }
}
