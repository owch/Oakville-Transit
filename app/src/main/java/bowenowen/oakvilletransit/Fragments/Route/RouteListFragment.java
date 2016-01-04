package bowenowen.oakvilletransit.Fragments.Route;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import bowenowen.oakvilletransit.DBHelper;
import bowenowen.oakvilletransit.MainActivity;
import bowenowen.oakvilletransit.Model.Route;
import bowenowen.oakvilletransit.R;
import bowenowen.oakvilletransit.RoutesActivity;

/**
 * Created by owenchen on 15-04-22.
 */
public class RouteListFragment extends Fragment{
    ListView routeList;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.route_list_fragment, null, false);

        final ListView listView = (ListView)view.findViewById(R.id.route_list);
        ((RoutesActivity) getActivity()).setRouteAdapter(listView);

        listView.setItemsCanFocus(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Route route = (Route) parent.getItemAtPosition(position);

                DBHelper dbHelper = new DBHelper(getActivity(), 1);
                dbHelper.deleteCurrentRoute();
                dbHelper.insertCurrentRoute(route.getNumber());

                ((RoutesActivity) getActivity()).checkSegement(R.id.control_stops);
            }
        });



        return view;
    }

    private void switchFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.route_list_fragment, fragment).commit();
    }


}
