package com.sapa.solulife.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sapa.solulife.activities.MainActivity;
import com.sapa.solulife.R;

/**
 * Created by Pooja S on 9/30/2016.
 */

public class MainFragment extends Fragment implements View.OnClickListener {

    private View mainView;
    private Context context;

    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;
    CardView schedulec, notec, boredc, psychologyc, magnifyc;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_main, container, false);
        context = mainView.getContext();

        toolbar = (Toolbar) mainView.findViewById(R.id.toolbar_main);
        setActionBar(toolbar);
        toolbar.setTitle("Home");
        //toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_menu));

        coordinatorLayout = (CoordinatorLayout) mainView.findViewById(R.id.coordinatinglayout);

        schedulec = (CardView) mainView.findViewById(R.id.schedule);
        notec = (CardView) mainView.findViewById(R.id.note);
        boredc = (CardView) mainView.findViewById(R.id.bored);
        psychologyc = (CardView) mainView.findViewById(R.id.psychology);
        magnifyc = (CardView) mainView.findViewById(R.id.magnify);

        return mainView;
    }

    private void setActionBar(Toolbar toolbar) {
        MainActivity activity = ((MainActivity) getActivity());
        activity.setSupportActionBar(toolbar);
        activity.updateToggleButton(toolbar);
    }

    public void switchFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.fragment_holder, fragment)
                .commit();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){

            case R.id.schedule:

                break;

            case R.id.note:

                break;

            case R.id.bored:

                break;

            case R.id.psychology:

                break;

            case R.id.magnify:

                break;


        }
    }
}
