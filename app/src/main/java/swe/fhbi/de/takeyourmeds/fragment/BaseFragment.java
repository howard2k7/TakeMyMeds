package swe.fhbi.de.takeyourmeds.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import swe.fhbi.de.takeyourmeds.activity.BaseActivity;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */
public abstract class BaseFragment extends Fragment { // abstrct Methode:für den Titel, der auf Tollbar dargestellt wird. Die Methode kann für alle Fragmente, die von Base abgeleitet sind, implementiert werden.
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) { // wird der Titel gesetzt.
        super.onCreate(savedInstanceState);
        BaseActivity baseActivity = (BaseActivity) getActivity();
        if (baseActivity != null)
            baseActivity.getToolbar().setTitle(getTitlePage());
    }

    public abstract String getTitlePage();
}
