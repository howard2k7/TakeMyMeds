package swe.fhbi.de.takeyourmeds.fragment;

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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import swe.fhbi.de.takeyourmeds.MedAppDatabaseHelper;
import swe.fhbi.de.takeyourmeds.R;
import swe.fhbi.de.takeyourmeds.adapter.LogAdapter;
import swe.fhbi.de.takeyourmeds.model.Log;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */

public class LogFragment extends BaseFragment {//Einnahmeprotokoll,
    private List<Log> logs;
    private MedAppDatabaseHelper databaseHelper;
    private LogAdapter logAdapter;
    private ImageView ivDelete;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {//array aus datenbank und in array hinzugefuegt
        super.onCreate(savedInstanceState);
        logs = new ArrayList<>();
        databaseHelper = new MedAppDatabaseHelper(getActivity());
        logs.addAll(databaseHelper.getAllLogs());
    }

    @Override
    public String getTitlePage() {
        return getString(R.string.protocol);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,//mit xml verbunden
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {//definiert alle sachen
        super.onViewCreated(view, savedInstanceState);
        TextView tvMsg = view.findViewById(R.id.tv_msg);//xml verbunden
        if (logs.isEmpty())
            tvMsg.setVisibility(View.VISIBLE);//falls empty, wird text ausgegeben
        else
            tvMsg.setVisibility(View.INVISIBLE);

        ivDelete = view.findViewById(R.id.iv_delete);//fuer Delete ganzer liste
        ivDelete.setOnClickListener(new OnDeleteClick());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(layoutManager);
        rvList.setHasFixedSize(true);
        logAdapter = new LogAdapter(logs);
        rvList.setAdapter(logAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        logs.clear();
        logs.addAll(databaseHelper.getAllLogs());
        logAdapter.notifyDataSetChanged();
    }

    private class OnDeleteClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {//wenn delete gewaehlt
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(view.getContext(), R.style.AppCompatAlertDialogStyle);//Dialog erzeugt
            builder.setTitle(view.getContext().getString(R.string.delete))
                    .setMessage(view.getContext().getString(R.string.delete_protocol_permission))
                    .setPositiveButton(view.getContext().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {//wenn ja, werden die Werten geloescht
                            databaseHelper.deleteLog();
                            logs.clear();
                            logAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(view.getContext().getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {//falls nein, nichts passiert, dialog cancel
                            dialog.cancel();
                        }
                    })
                    .show();
        }
    }
}
