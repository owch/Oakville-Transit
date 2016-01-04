package bowenowen.oakvilletransit.Fragments.Search;

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
import bowenowen.oakvilletransit.Model.Stop;
import bowenowen.oakvilletransit.R;
import bowenowen.oakvilletransit.SearchActivity;
import bowenowen.oakvilletransit.StopInfoActivity;

/**
 * Created by owenchen on 15-04-22.
 */
public class SearchListFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_list_fragment, container, false);

        final ListView listView = (ListView)view.findViewById(R.id.search_list);

        String strtext=getArguments().getString("query");

        ((SearchActivity) getActivity()).setAdapter(listView, strtext);

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
