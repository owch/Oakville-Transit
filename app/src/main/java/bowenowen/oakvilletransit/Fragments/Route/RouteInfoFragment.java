package bowenowen.oakvilletransit.Fragments.Route;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import bowenowen.oakvilletransit.MainActivity;
import bowenowen.oakvilletransit.Model.Route;
import bowenowen.oakvilletransit.R;
import bowenowen.oakvilletransit.RoutesActivity;

/**
 * Created by owenchen on 15-05-09.
 */
public class RouteInfoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.route_info_list_fragment, null, false);

        final ListView listView = (ListView)view.findViewById(R.id.route_info_list);

        String strtext=getArguments().getString("route_number");

        ((RoutesActivity) getActivity()).setRouteInfoAdapter(listView, strtext);

        listView.setItemsCanFocus(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        return view;
    }
}
