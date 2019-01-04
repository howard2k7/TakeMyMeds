package swe.fhbi.de.takeyourmeds.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import swe.fhbi.de.takeyourmeds.R;
import swe.fhbi.de.takeyourmeds.model.Drug;
import swe.fhbi.de.takeyourmeds.model.Plan;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */
public class PlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {//hier sind auch die headers vorhanden
    private static final int HEADER_TYPE = 0;//ein view fuer header, der andere fuer drug
    private static final int DRUG_TYPE = 1;
    private List<Object> list;
    private OnStatusCheckListener listener;

    public PlanAdapter(List<Object> list, OnStatusCheckListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof Drug)//wird abgefragt, ob das ein drug, ansonsten header
            return DRUG_TYPE;
        else
            return HEADER_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//fuer anzeige, header vs. drug
        View view;
        switch (viewType) {//view bestimmt
            case HEADER_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item_header, parent, false);
                return new HeaderViewHolder(view);
            case DRUG_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item_drug, parent, false);
                return new DrugViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case HEADER_TYPE:
                HeaderViewHolder vhHeader = (HeaderViewHolder) holder;
                vhHeader.tvHeader.setText(((String) list.get(position)));
                break;
            case DRUG_TYPE:
                DrugViewHolder vhDrug = (DrugViewHolder) holder;
                final Plan plan = ((Plan) list.get(position));
                vhDrug.tvDrugName.setText(plan.getName());
                vhDrug.tvDrugTimeTitle.setText(plan.getTimeTitle());
                vhDrug.tvDrugTime.setText(plan.getTime());
                vhDrug.tvDrugQuantity.setText(String.valueOf(plan.getQuantity()));
                vhDrug.tvDrugUnit.setText(plan.getUnit());
                vhDrug.cbStatus.setChecked(plan.getStatus());
                vhDrug.cbStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        listener.onStatusCheck(plan.getId(), b);
                    }
                });
                break;
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnStatusCheckListener {
        void onStatusCheck(int DrugId, boolean value);
    }

    private class DrugViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDrugName;
        private TextView tvDrugTimeTitle;
        private TextView tvDrugTime;
        private TextView tvDrugQuantity;
        private TextView tvDrugUnit;
        private CheckBox cbStatus;

        private DrugViewHolder(View itemView) {
            super(itemView);
            tvDrugName = itemView.findViewById(R.id.tv_drug_name);
            tvDrugTime = itemView.findViewById(R.id.tv_drug_time);
            tvDrugTimeTitle = itemView.findViewById(R.id.tv_drug_time_title);
            tvDrugQuantity = itemView.findViewById(R.id.tv_drug_quantity);
            tvDrugUnit = itemView.findViewById(R.id.tv_drug_unit);
            cbStatus = itemView.findViewById(R.id.cb_status);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHeader;

        private HeaderViewHolder(View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tv_header);
        }
    }
}