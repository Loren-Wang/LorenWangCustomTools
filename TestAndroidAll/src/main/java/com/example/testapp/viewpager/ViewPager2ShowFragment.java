package com.example.testapp.viewpager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.testapp.R;

import androidx.fragment.app.Fragment;

public class ViewPager2ShowFragment extends Fragment {
    private int posi;

    public ViewPager2ShowFragment(int posi) {
        this.posi = posi;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_view_pager_show, container, false);
        ((TextView) inflate.findViewById(R.id.tvTest)).setText(String.valueOf(posi));
        return inflate;
    }

}
