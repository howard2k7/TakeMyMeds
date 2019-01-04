package swe.fhbi.de.takeyourmeds.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import swe.fhbi.de.takeyourmeds.R;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */

public class BaseActivity extends AppCompatActivity {//Ist Eltern, von anderen Aktivitaeten wird benutzt, damit die App startet. Es besteht aus Action bar und Toolbar
    private Toolbar toolbar;
    protected ActionBar actionbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(getToolbar());
        setupActionBar();
    }

    public void setupActionBar(){
        if (actionbar != null)
            return;

        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    public Toolbar getToolbar() {//Setup fuer Toolbar wird durchgefuehrt
        if (toolbar == null)//Lesen und Aenderungen machen bezueglich name, farbe
            return toolbar = findViewById(R.id.toolbar);
        return toolbar;
    }

    protected void replaceFragment(Fragment fragment) {//nur einmal, statt mehrmals fuer alle. Neue instanzen und fragmente erzeugen und darstellen
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
