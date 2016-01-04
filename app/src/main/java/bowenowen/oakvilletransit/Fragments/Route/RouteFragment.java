package bowenowen.oakvilletransit.Fragments.Route;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;

import bowenowen.oakvilletransit.DBHelper;
import bowenowen.oakvilletransit.Model.Route;
import bowenowen.oakvilletransit.R;
import bowenowen.oakvilletransit.RoutesActivity;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by owenchen on 15-05-16.
 */
public class RouteFragment extends Fragment{
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.route_fragment, null, false);

        switchFragment(new RouteListFragment());

        SegmentedGroup segmentedControl = (SegmentedGroup) view.findViewById(R.id.segmented2);
        checkSegement(R.id.control_list, view);

        segmentedControl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.control_list:
                        switchFragment(new RouteListFragment());
                        ((RoutesActivity) getActivity()).setTitle("All Routes");
                        break;
                    case R.id.control_stops:
                        ((RoutesActivity) getActivity()).changeFragment();
                        break;
                    default:
                }
            }
        });

        return view;
    }

    public void switchFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.route_list_fragment, fragment).commit();
    }

    public void checkSegement(int id, View view)
    {
        SegmentedGroup segmentedControl = (SegmentedGroup) view.findViewById(R.id.segmented2);
        segmentedControl.check(id);
    }
}
