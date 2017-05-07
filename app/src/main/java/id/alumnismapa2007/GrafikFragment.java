package id.alumnismapa2007;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class GrafikFragment extends Fragment {

    ArrayList<BarEntry> BARENTRY;
    ArrayList<String> BarEntryLabels;
    BarDataSet Bardataset;
    BarData BARDATA;

    ArrayList<BarEntry> BARENTRYTANGGAL;
    ArrayList<String> BarEntryLabelsTanggal;
    BarDataSet BardatasetTanggal;
    BarData BARDATATANGGAL;

    public static final String myPREFERENCES = "vote" ;
    public static final String sekolah = "sekolah";
    public static final String rumah = "rumah";
    public static final String lain = "lain";
    public static final String tgl27 = "tgl27";
    public static final String tgl28 = "tgl28";
    public static final String tgl29 = "tgl29";
    public static final String tgl30 = "tgl30";
    public static final String tgl1 = "tgl1";
    public static final String tgl2018 = "tgl2018";
    public static final String ip1 = "ipa1";
    public static final String ip2 = "ipa2";
    public static final String ip3 = "ips1";
    public static final String ip4 = "ips2";

    @BindView(R.id.chart)
    BarChart barChart;
    @BindView(R.id.chartTanggal)
    HorizontalBarChart barChartTanggal;
    @BindView(R.id.txtIPA1)
    TextView txtIPA1;
    @BindView(R.id.txtIPA2)
    TextView txtIPA2;
    @BindView(R.id.txtIPS1)
    TextView txtIPS1;
    @BindView(R.id.txtIPS2)
    TextView txtIPS2;

    public GrafikFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grafik, container, false);
        ButterKnife.bind(this, rootView);

        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();
        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();
        Bardataset = new BarDataSet(BARENTRY, "Lokasi");
        BARDATA = new BarData(BarEntryLabels, Bardataset);
        Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.setData(BARDATA);
        barChart.animateY(3000);
        barChart.setDescription("");

        BARENTRYTANGGAL = new ArrayList<>();
        BarEntryLabelsTanggal = new ArrayList<String>();
        AddValuesToBARENTRYTANGGAL();
        AddValuesToBarEntryLabelsTanggal();
        BardatasetTanggal = new BarDataSet(BARENTRYTANGGAL, "Tanggal");
        BARDATATANGGAL = new BarData(BarEntryLabelsTanggal, BardatasetTanggal);
        BardatasetTanggal.setColors(ColorTemplate.COLORFUL_COLORS);
        barChartTanggal.setData(BARDATATANGGAL);
        barChartTanggal.animateY(3000);
        barChartTanggal.setDescription("");

        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(myPREFERENCES, getActivity().MODE_PRIVATE);
        txtIPA1.setText(sharedpreferences.getString(ip1, ""));
        txtIPA2.setText(sharedpreferences.getString(ip2, ""));
        txtIPS1.setText(sharedpreferences.getString(ip3, ""));
        txtIPS2.setText(sharedpreferences.getString(ip4, ""));

        return rootView;
    }

    public void AddValuesToBARENTRY() {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(myPREFERENCES, getActivity().MODE_PRIVATE);
        if (sharedpreferences.getString(sekolah, "").isEmpty() && sharedpreferences.getString(rumah, "").isEmpty() && sharedpreferences.getString(lain, "").isEmpty()) {

        } else {
            int sekolah1 = Integer.parseInt(sharedpreferences.getString(sekolah, ""));
            int rumah1 = Integer.parseInt(sharedpreferences.getString(rumah, ""));
            int lain1 = Integer.parseInt(sharedpreferences.getString(lain, ""));
            BARENTRY.add(new BarEntry(sekolah1, 0));
            BARENTRY.add(new BarEntry(rumah1, 1));
            BARENTRY.add(new BarEntry(lain1, 2));
        }
    }

    public void AddValuesToBarEntryLabels() {
        BarEntryLabels.add("SMA");
        BarEntryLabels.add("Barinten");
        BarEntryLabels.add("Lainnya");
    }

    public void AddValuesToBARENTRYTANGGAL() {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(myPREFERENCES, getActivity().MODE_PRIVATE);
        if (sharedpreferences.getString(tgl27, "").isEmpty()) {

        } else {
            int tgl271 = Integer.parseInt(sharedpreferences.getString(tgl27, ""));
            int tgl281 = Integer.parseInt(sharedpreferences.getString(tgl28, ""));
            int tgl291 = Integer.parseInt(sharedpreferences.getString(tgl29, ""));
            int tgl301 = Integer.parseInt(sharedpreferences.getString(tgl30, ""));
            int tgl11 = Integer.parseInt(sharedpreferences.getString(tgl1, ""));
            int tgl20181 = Integer.parseInt(sharedpreferences.getString(tgl2018, ""));
            BARENTRYTANGGAL.add(new BarEntry(tgl271, 5));
            BARENTRYTANGGAL.add(new BarEntry(tgl281, 4));
            BARENTRYTANGGAL.add(new BarEntry(tgl291, 3));
            BARENTRYTANGGAL.add(new BarEntry(tgl301, 2));
            BARENTRYTANGGAL.add(new BarEntry(tgl11, 1));
            BARENTRYTANGGAL.add(new BarEntry(tgl20181, 0));
        }

    }

    public void AddValuesToBarEntryLabelsTanggal() {
        BarEntryLabelsTanggal.add("2018");
        BarEntryLabelsTanggal.add("1 Juli");
        BarEntryLabelsTanggal.add("30 Juni");
        BarEntryLabelsTanggal.add("29 Juni");
        BarEntryLabelsTanggal.add("28 Juni");
        BarEntryLabelsTanggal.add("27 Juni");
    }

    @OnClick(R.id.linkedin)
    void linkedin() {
        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.linkedin.com/in/sulistiyanto/"));
        startActivity(browserIntent);
    }

    @OnClick(R.id.github)
    void github() {
        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://github.com/sulistiyanto"));
        startActivity(browserIntent);
    }

    @OnClick(R.id.facebook)
    void facebook() {
        Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/tiyan.cyrus"));
        startActivity(browserIntent);
    }

    @OnClick(R.id.linearLayoutIPA1)
    void ipa1() {
        if (txtIPA1.getText().toString().equals("0")) {
            Toast.makeText(getContext(), "Belum ada data alumni IPA 1", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getContext(), LihatKelasActivity.class);
            intent.putExtra("key", "IPA 1");
            startActivity(intent);
        }
    }

    @OnClick(R.id.linearLayoutIPA2)
    void ipa2() {
        if (txtIPA2.getText().toString().equals("0")) {
            Toast.makeText(getContext(), "Belum ada data alumni IPA 2", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getContext(), LihatKelasActivity.class);
            intent.putExtra("key", "IPA 2");
            startActivity(intent);
        }
    }

    @OnClick(R.id.linearLayoutIPS1)
    void ips1() {
        if (txtIPS1.getText().toString().equals("0")) {
            Toast.makeText(getContext(), "Belum ada data alumni IPS 1", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getContext(), LihatKelasActivity.class);
            intent.putExtra("key", "IPS 1");
            startActivity(intent);
        }
    }

    @OnClick(R.id.linearLayoutIPS2)
    void ips2() {
        if (txtIPS2.getText().toString().equals("0")) {
            Toast.makeText(getContext(), "Belum ada data alumni IPS 2", Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(getContext(), LihatKelasActivity.class);
            intent.putExtra("key", "IPS 2");
            startActivity(intent);
        }
    }
}
