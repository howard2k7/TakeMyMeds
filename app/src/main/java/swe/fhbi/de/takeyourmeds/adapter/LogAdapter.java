package swe.fhbi.de.takeyourmeds.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import swe.fhbi.de.takeyourmeds.R;
import swe.fhbi.de.takeyourmeds.model.Log;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {
    private List<Log> logs;

    public LogAdapter(List<Log> logs) {
        this.logs = logs;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//die klasse wird erzeugt
        return new LogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final LogViewHolder holder, final int position) {//position von android wird mit daten aus datenbank verbunden und dargestellt
        final Log log = logs.get(position);
        holder.tvName.setText(log.getDrug().getName());
        holder.tvDate.setText(log.getDate());
        holder.tvTime.setText(log.getTime());
        holder.checkBox.setChecked(log.getStatus() == 1);//wird angezeigt, dass das eingenommen wurde
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public class LogViewHolder extends RecyclerView.ViewHolder {//von android. alles definiert, initialitiiert
        private TextView tvName;
        private TextView tvDate;
        private TextView tvTime;
        private CheckBox checkBox;

        public LogViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTime = itemView.findViewById(R.id.tv_time);
            checkBox = itemView.findViewById(R.id.cb_status);
        }
    }
}
