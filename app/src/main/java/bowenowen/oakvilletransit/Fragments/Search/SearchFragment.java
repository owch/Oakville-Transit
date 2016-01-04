package bowenowen.oakvilletransit.Fragments.Search;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import bowenowen.oakvilletransit.R;

/**
 * Created by owenchen on 15-05-16.
 */
public class SearchFragment extends Fragment{
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, null, false);

        ImageButton searchButton = (ImageButton) view.findViewById(R.id.search_stop_button);
            final EditText editText = (EditText) view.findViewById(R.id.search_box);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchListFragment searchListFragment = new SearchListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("query", editText.getText().toString());
                searchListFragment.setArguments(bundle);

                switchFragment(searchListFragment);
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
