package bowenowen.oakvilletransit.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bowenowen.oakvilletransit.R;

/**
 * Created by owenchen on 15-04-22.
 */
public class NearestFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.nearest_fragment, container, false);
        return view;
    }
}
