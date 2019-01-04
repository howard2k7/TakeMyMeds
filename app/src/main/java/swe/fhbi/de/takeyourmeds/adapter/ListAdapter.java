package swe.fhbi.de.takeyourmeds.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import swe.fhbi.de.takeyourmeds.MedAppDatabaseHelper;
import swe.fhbi.de.takeyourmeds.R;
import swe.fhbi.de.takeyourmeds.model.Drug;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */

//adapter wird mit recycleview verbunden und recycleview(ein view zur darstellung einer liste)
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {//Adapter wird fuer liste verwendet, was wie wo gesetzt werden sollen. Und wie das View von einem Item sein soll
    private List<Drug> list;//ListAdapter.ListViewHolder-was fuer view jedes item haben muss, 80. Zeile
    private MedAppDatabaseHelper databaseHelper;
    private Context context;

    public ListAdapter(Context context, List<Drug> list, MedAppDatabaseHelper databaseHelper) {
        this.list = list;
        this.databaseHelper = databaseHelper;
        this.context = context;
    }


    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//klasse wird definiert
        return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
    }//mit xml verbunden

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {//zusammengebaut, werte gesetzt
        final Drug drug = list.get(position);//position automatisch von android
        holder.tvName.setText(drug.getName());
        String amount = String.valueOf(drug.getQuantity()) + " " + drug.getUnit();
        holder.tvAmount.setText(amount);
        holder.tvNote.setText(drug.getNote());
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {//wenn delete geklickt wird
            @Override
            public void onClick(View view) {//dadurch, dass hier die elemente einzeln geloescht werden, wird das in adapter implementiert

                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
                builder.setTitle(view.getContext().getString(R.string.delete))
                        .setMessage(context.getString(R.string.delete_drug_permission))
                        .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                databaseHelper.deleteDrug(drug.getId());
                                list.remove(position);
                                notifyDataSetChanged();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {//manche methode muessen uberschreiben werden. wie viele item ich auf meine seite dargestellt haben will
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {//die klasse wird fuer onCreateViewHolder benoetigt
        private TextView tvName;
        private TextView tvAmount;
        private TextView tvNote;
        private ImageView ivDelete;

        public ListViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvNote = itemView.findViewById(R.id.tv_note);
            ivDelete = itemView.findViewById(R.id.iv_delete);
        }
    }
}
