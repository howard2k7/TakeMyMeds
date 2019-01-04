package swe.fhbi.de.takeyourmeds.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import swe.fhbi.de.takeyourmeds.MedAppDatabaseHelper;
import swe.fhbi.de.takeyourmeds.R;
import swe.fhbi.de.takeyourmeds.model.Drug;
import swe.fhbi.de.takeyourmeds.model.Time;

/**
 * Created by Gruppe A.
 * WS 18/19, FH Bielefeld
 */

public class DrugActivity extends BaseActivity {//

    static String[] morningValues = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10"};
    static String[] noonValues = {"11", "12", "13", "14", "15", "16", "17"};
    static String[] eveningValues = {"18", "19", "20", "21", "22", "23", "00"};
    static String[] unitItems = new String[]{"Tablette(n)", "Tropfen", "TL", "mg", "ml"};

    MedAppDatabaseHelper databaseHelper = new MedAppDatabaseHelper(this);

    private SeekBar sbMorning;
    private SeekBar sbNoon;
    private SeekBar sbEvening;
    private CheckBox cbMorning;
    private CheckBox cbNoon;
    private CheckBox cbEvening;
    private EditText txtName;
    private EditText txtNote;
    private EditText txtAmount;
    private Spinner sQuantity;
    private FrameLayout btnSave;
    //    private Button btnCancel;
    private TextView txtMorningTime;
    private TextView txtNoonTime;
    private TextView txtEveningTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {//Werte gesetzt und definiert
        setContentView(R.layout.activity_drug);
        super.onCreate(savedInstanceState);//Base?

        setViews();

        sbMorning.setOnSeekBarChangeListener(new CustomSeekBar(txtMorningTime));
        sbNoon.setOnSeekBarChangeListener(new CustomSeekBar(txtNoonTime));
        sbEvening.setOnSeekBarChangeListener(new CustomSeekBar(txtEveningTime));

        cbMorning.setOnCheckedChangeListener(new CheckBoxListener(sbMorning));
        cbNoon.setOnCheckedChangeListener(new CheckBoxListener(sbNoon));
        cbEvening.setOnCheckedChangeListener(new CheckBoxListener(sbEvening));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, unitItems);
        sQuantity.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = getName();
                if (TextUtils.isEmpty(name)) {
                    txtName.setError(getString(R.string.empty));
                    return;
                }

                String amount = txtAmount.getText().toString();
                if (TextUtils.isEmpty(amount)) {
                    txtAmount.setError(getString(R.string.empty));
                    return;
                }

                String note = getNote();

                String unit = unitItems[(int) sQuantity.getSelectedItemId()];
                int drugId = (int) databaseHelper.insertDrug(new Drug(name, Integer.valueOf(amount), note, unit));//zu Datenbank hinzufugen
                for (Time time : getTimes()) {
                    time.setDrugId(drugId);
                    long timeId = databaseHelper.insertTime(time);
                    time.setId((int) timeId);
                }
                Toast.makeText(DrugActivity.this, R.string.drug_added, Toast.LENGTH_SHORT).show();
                resetValue();
            }
        });
    }

    private void resetValue() {
        txtName.setText("");
        txtAmount.setText("");
        txtNote.setText("");
        cbEvening.setChecked(false);
        cbMorning.setChecked(false);
        cbNoon.setChecked(false);
        sbEvening.setProgress(0);
        sbNoon.setProgress(0);
        sbMorning.setProgress(0);
        sbEvening.setEnabled(false);
        sbNoon.setEnabled(false);
        sbMorning.setEnabled(false);
    }

    @Override
    public void setupActionBar() {//Titel fuer Toolbar
        if (actionbar != null)
            return;

        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.new_medicament));
    }

    private void setViews() {//View initialisieren
        //seek bars
        sbMorning = findViewById(R.id.sb_morning);
        sbMorning.setMax(morningValues.length - 1);
        sbNoon = findViewById(R.id.sb_noon);
        sbNoon.setMax(noonValues.length - 1);
        sbEvening = findViewById(R.id.sb_evening);
        sbEvening.setMax(eveningValues.length - 1);

        //check boxes
        cbMorning = findViewById(R.id.ch_morning);
        sbMorning.setEnabled(false);
        cbNoon = findViewById(R.id.ch_noon);
        sbNoon.setEnabled(false);
        cbEvening = findViewById(R.id.ch_evening);
        sbEvening.setEnabled(false);

        //edit texts
        txtName = findViewById(R.id.txt_name);
        txtNote = findViewById(R.id.txt_note);
        txtAmount = findViewById(R.id.txt_amount);

        sQuantity = findViewById(R.id.s_quantity);

        //buttons
        btnSave = findViewById(R.id.fl_save);
//        btnCancel = findViewById(R.id.btn_cancel);

        //text views
        txtMorningTime = findViewById(R.id.txt_time_morning);
        txtMorningTime.setText(morningValues[0]);
        txtNoonTime = findViewById(R.id.txt_time_noon);
        txtNoonTime.setText(noonValues[0]);
        txtEveningTime = findViewById(R.id.txt_time_evening);
        txtEveningTime.setText(eveningValues[0]);
    }

    protected String getName() {
        return String.valueOf(txtName.getText());
    }

    protected String getNote() {
        return String.valueOf(txtNote.getText());
    }

    protected String getTimeValue(int progress, SeekBar seekBar) {
        if (seekBar == sbMorning)
            return morningValues[progress];
        else if (seekBar == sbNoon)
            return noonValues[progress];
        else
            return eveningValues[progress];

    }

    protected List<Time> getTimes() {
        List<Time> timeList = new ArrayList<>();
        if (cbMorning.isChecked())
            timeList.add(new Time(getString(R.string.morning), String.valueOf(txtMorningTime.getText()), 0));
        if (cbNoon.isChecked())
            timeList.add(new Time(getString(R.string.noon), String.valueOf(txtNoonTime.getText()), 0));
        if (cbEvening.isChecked())
            timeList.add(new Time(getString(R.string.evening), String.valueOf(txtEveningTime.getText()), 0));
        return timeList;
    }

    private class CustomSeekBar implements SeekBar.OnSeekBarChangeListener {
        private TextView txtTime;

        public CustomSeekBar(TextView txtTime) {
            this.txtTime = txtTime;
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            txtTime.setText(getTimeValue(i, seekBar));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private class CheckBoxListener implements CompoundButton.OnCheckedChangeListener {
        private SeekBar seekBar;

        public CheckBoxListener(SeekBar seekBar) {
            this.seekBar = seekBar;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            if (checked)
                seekBar.setEnabled(true);
            else
                seekBar.setEnabled(false);
        }
    }
}
