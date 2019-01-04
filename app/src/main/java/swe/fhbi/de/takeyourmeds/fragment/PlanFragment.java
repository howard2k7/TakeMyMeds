package swe.fhbi.de.takeyourmeds.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import swe.fhbi.de.takeyourmeds.MedAppDatabaseHelper;
import swe.fhbi.de.takeyourmeds.R;
import swe.fhbi.de.takeyourmeds.adapter.PlanAdapter;
import swe.fhbi.de.takeyourmeds.model.Log;
import swe.fhbi.de.takeyourmeds.model.Time;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */
public class PlanFragment extends BaseFragment {
    private List<Object> plans;//liste von objekte
    private MedAppDatabaseHelper databaseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plans = new ArrayList<>();//array, um daten zu speichern
        databaseHelper = new MedAppDatabaseHelper(getActivity());//datenbank wird 3 Mal abgefragt
        plans.add(getString(R.string.morning));// string zu liste hinzugefuegt
        plans.addAll(databaseHelper.getAllDrugs(getString(R.string.morning)));//liste von Drug
        plans.add(getString(R.string.noon));
        plans.addAll(databaseHelper.getAllDrugs(getString(R.string.noon)));
        plans.add(getString(R.string.evening));
        plans.addAll(databaseHelper.getAllDrugs(getString(R.string.evening)));

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan, container, false);//xml verbunden
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView rvList = view.findViewById(R.id.rv_plan);
        rvList.setLayoutManager(layoutManager);
        rvList.setHasFixedSize(true);
        PlanAdapter planAdapter = new PlanAdapter(plans, new PlanAdapter.OnStatusCheckListener() {
            @Override
            public void onStatusCheck(final int drugId, final boolean value) {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getContext(), R.style.AppCompatAlertDialogStyle);
                builder.setTitle("")
                        .setMessage(value ? getString(R.string.check_permission) : getString(R.string.uncheck_permission))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int status = value ? 1 : 0;
                                databaseHelper.updateStatusById(drugId, status);//datenbank updated
                                insertLog(drugId, status);
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
        rvList.setAdapter(planAdapter);
    }

    @SuppressLint("SimpleDateFormat")
    private void insertLog(int drugId, int status) {//speichert das datum bei einnahme
        Calendar c = Calendar.getInstance();//methode von android selber
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());
        SimpleDateFormat dftime = new SimpleDateFormat("HH:mm");
        String formattedTime = dftime.format(c.getTime());
        databaseHelper.insertLog(new Log(formattedDate, formattedTime, drugId, status));//in datenbank gespeichert
    }

    @Override
    public String getTitlePage() {
        return getString(R.string.plan);
    }
}
