package bowenowen.oakvilletransit.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import bowenowen.oakvilletransit.HomeActivity;
import bowenowen.oakvilletransit.R;
import bowenowen.oakvilletransit.RoutesActivity;
import bowenowen.oakvilletransit.SearchActivity;

/**
 * Created by owenchen on 15-04-22.
 */
public class NavigationFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.navigation_fragment, container, false);

        Button nearestButton;
        Button searchButton;
        final Button homeButton;
        Button routesButton;
        Button mapButton;

        nearestButton = (Button) view.findViewById(R.id.button_nearest);
        searchButton = (Button) view.findViewById(R.id.button_search);
        homeButton = (Button) view.findViewById(R.id.button_home);
        routesButton = (Button) view.findViewById(R.id.button_routes);
        mapButton = (Button) view.findViewById(R.id.button_map);

        nearestButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.button_nearest:
                        Fragment fragment = new NearestFragment();
                        //switchFragment(fragment);
                        //switchTitle("Nearest Stop");
                        break;
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.button_search:
                        newActivity("search");
                        //switchFragment(fragment);
                        //switchTitle("Search Stops");
                        break;
                }
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.button_home:

                        newActivity("home");
                        //switchFragment(fragment);
                        //switchTitle("Home");
                        break;
                }
            }
        });

        routesButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.button_routes:

                        newActivity("routes");
                        //switchFragment(fragment);
                        //switchTitle("All Routes");
                        break;
                }
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                switch (v.getId())
                {
                    case R.id.button_map:
                        Fragment fragment = new MapFragment();
                        //switchFragment(fragment);
                        //switchTitle("Map");
                        break;
                }
            }
        });
        return view;
    }




    private void newActivity(String activity_name)
    {
        Intent intent;
        switch (activity_name) {
            case "home":
                intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case "routes":
                intent = new Intent(getActivity(), RoutesActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case "search":
                intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }

    }


}


