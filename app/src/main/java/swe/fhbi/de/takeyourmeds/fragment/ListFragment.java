package swe.fhbi.de.takeyourmeds.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import swe.fhbi.de.takeyourmeds.MedAppDatabaseHelper;
import swe.fhbi.de.takeyourmeds.R;
import swe.fhbi.de.takeyourmeds.adapter.ListAdapter;
import swe.fhbi.de.takeyourmeds.model.Drug;
import swe.fhbi.de.takeyourmeds.receiver.GeneralAlarmReceiver;
import swe.fhbi.de.takeyourmeds.util.AppSharePreference;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */
public class ListFragment extends BaseFragment {//Medikamentenliste
    private List<Drug> drugs;
    private MedAppDatabaseHelper databaseHelper;
    private ListAdapter listAdapter;
    private AppSharePreference appSharePreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {//Array erzeugt und bekommt eine Instanz von Datenbank Helper
        super.onCreate(savedInstanceState);
        drugs = new ArrayList<>();
        databaseHelper = new MedAppDatabaseHelper(getActivity());
        drugs.addAll(databaseHelper.getAllDrugs());//Alle Medikamente aus Datenbank abgefragt und in Liste gespeichert
    }

    @Override
    public String getTitlePage() {
        return getString(R.string.list);
    }//Titel gesetzt fuer Toolbar

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,//Fragment Liste wird mit XML verbunden
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {//ListVIEW ODER Recycleview fuer Darstellen wird benutzt
        super.onViewCreated(view, savedInstanceState);
        TextView tvMsg = view.findViewById(R.id.tv_msg);
        if (drugs.isEmpty())
            tvMsg.setVisibility(View.VISIBLE);
        else
            tvMsg.setVisibility(View.INVISIBLE);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());//wird Layout von Liste festgelegt
        RecyclerView rvList = view.findViewById(R.id.rv_list);//linear, untereinander
        rvList.setLayoutManager(layoutManager);
        rvList.setHasFixedSize(true);
        listAdapter = new ListAdapter(getContext(),drugs,databaseHelper);//sind benoetigt, um Liste benutzen zu koennen, eine Klasse, die nur fuer Liste benutzt wird. Extend von Recycler view.
        rvList.setAdapter(listAdapter);

        appSharePreference = new AppSharePreference(getContext());//platz, der uns von App zur Verfugung gestellt wird, damit wir daten mit niedrigeren Volumen da speichern koennen
        if (!appSharePreference.isExistAlarm())//kann man anstatt Datenbank benutzen, appsharepreference!!!
            setGeneralAlarm();
    }

    private void setGeneralAlarm() {//aktiv, am bestimmten Zeitpunkt. man muss nicht jedes Mal aufrufen, nur einmal.
        Intent intent = new Intent(getContext(), GeneralAlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0,
                intent, 0);

        AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 00);

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        appSharePreference.setExistAlarm();
    }

    @Override
    public void onResume() {//Wird aktualisiert, Liste geloescht, adapter ruft nue werte aus Datenbank
        super.onResume();
        drugs.clear();
        drugs.addAll(databaseHelper.getAllDrugs());
        listAdapter.notifyDataSetChanged();//When an ArrayAdapter is constructed, it holds the reference for the List that was passed in. If you were to pass in a List that was a member of an Activity, and change that Activity member later, the ArrayAdapter is still holding a reference to the original List. The Adapter does not know you changed the List in the Activity.
    }
}
