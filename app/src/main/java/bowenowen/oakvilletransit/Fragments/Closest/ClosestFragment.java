package bowenowen.oakvilletransit.Fragments.Closest;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import bowenowen.oakvilletransit.Fragments.Search.SearchListFragment;
import bowenowen.oakvilletransit.R;

/**
 * Created by owenchen on 15-05-10.
 */
public class ClosestFragment extends Fragment {

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.closest_fragment, null, false);

        Button findBtn = (Button) view.findViewById(R.id.find_button);
        final EditText editText = (EditText) view.findViewById(R.id.search_box);

        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                ClosestListFragment closestListFragment = new ClosestListFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("query", editText.getText().toString());
//                closestListFragment.setArguments(bundle);
//
//                switchFragment(closestListFragment);
            }
        });

        return view;
    }

    private void switchFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.search_result_list, fragment).commit();
    }
}

