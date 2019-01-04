package swe.fhbi.de.takeyourmeds.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import swe.fhbi.de.takeyourmeds.R;
import swe.fhbi.de.takeyourmeds.fragment.LogFragment;
import swe.fhbi.de.takeyourmeds.fragment.ListFragment;
import swe.fhbi.de.takeyourmeds.fragment.PlanFragment;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */
public class MainActivity extends BaseActivity {//Baseactivity ist Eltern
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {//Eingestellt: Liste mit Einnahmeplan
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);//MAn geht zu Baseactivity. Dann Toolbar erzeugt

        if (savedInstanceState == null) {//wird gezeigt, was als erstes nach dem Einschalten. Als Root
            replaceFragment(new PlanFragment());
        }
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new OnNavigationItemSelected());
    }
    //Fragmente agile. Wenn es um Switchen zwischen irgendwelche Daten oder Seiten geht.Dann benutzen Fragmente. Zu Eingabe Activity!
    //Fragmente werden mit XML definiert in Activity einetzen.

    @Override
    protected void onResume() {
        super.onResume();
        checkIntent(getIntent());
    }

    private void checkIntent(Intent intent) {
        if (intent.hasExtra("PLAN_PAGE") && intent.getBooleanExtra("PLAN_PAGE", false))
            replaceFragment(new PlanFragment());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//Fuer toolbar, was fuer item im toolbar dargestellt wird. Erfolgt uber ID
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class OnNavigationItemSelected implements NavigationView.OnNavigationItemSelectedListener {//Handeling der Drawer

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {//Menü Item werden gewaehlt.
            switch (item.getItemId()) {//Nach ID werden Fragmenten instanziiert und eventuell in Kointainer von meinem Activity gesetzt
                case R.id.plan:
                    replaceFragment(new PlanFragment());
                    break;
                case R.id.new_medikament://neue Seite, wird neues Activity erzeugt
                    Intent intent = new Intent(getApplicationContext(), DrugActivity.class);
                    startActivity(intent);
                    break;
                case R.id.list:
                    replaceFragment(new ListFragment());
                    break;
                case R.id.protocol:
                    replaceFragment(new LogFragment());
                    break;
            }
            drawerLayout.closeDrawers();
            return true;
        }
    }
}
