package top.ss007.demolib2.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.ss007.annotation.annotations.RouterUri;
import top.ss007.businessbase.RouteTable;
import top.ss007.demolib2.R;

@RouterUri(path = RouteTable.LIB2_FRAG_BLANK)
public class BlankFragment extends Fragment {


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

}
