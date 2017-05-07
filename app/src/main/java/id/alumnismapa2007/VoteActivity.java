package id.alumnismapa2007;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoteActivity extends AppCompatActivity {

    private RadioButton radioKelasButton;

    @BindView(R.id.radioKelas)
    RadioGroup radioKelasGroup;
    @BindView(R.id.etNama)
    EditText etNama;
    @BindView(R.id.etBudget)
    EditText etBudget;
    @BindView(R.id.chkSMA)
    CheckBox chkSMA;
    @BindView(R.id.chkBarinten)
    CheckBox chkBarinten;
    @BindView(R.id.chkLain)
    CheckBox chkLain;
    @BindView(R.id.etLain)
    EditText etLain;
    @BindView(R.id.chk27)
    CheckBox chk27;
    @BindView(R.id.chk28)
    CheckBox chk28;
    @BindView(R.id.chk29)
    CheckBox chk29;
    @BindView(R.id.chk30)
    CheckBox chk30;
    @BindView(R.id.chk1)
    CheckBox chk1;
    @BindView(R.id.chk2018)
    CheckBox chk2018;
    @BindView(R.id.etNo)
    EditText etNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent=new Intent();
                intent.putExtra("MESSAGE","");
                setResult(2,intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent();
        intent.putExtra("MESSAGE","");
        setResult(2,intent);
        finish();
        super.onBackPressed();
    }

    @OnClick(R.id.btnVote)
    void btnVote() {
        String nama = etNama.getText().toString();
        String no = etNo.getText().toString();
        int selectedId = radioKelasGroup.getCheckedRadioButtonId();
        radioKelasButton = (RadioButton) findViewById(selectedId);
        int sma = checkValue(chkSMA);
        int rumah = checkValue(chkBarinten);
        int lain = checkValue(chkLain);
        int tgl27 = checkValue(chk27);
        int tgl28 = checkValue(chk28);
        int tgl29 = checkValue(chk29);
        int tgl30 = checkValue(chk30);
        int tgl1 = checkValue(chk1);
        int tgl2018 = checkValue(chk2018);
        String budget = etBudget.getText().toString();
        String keterangan = etLain.getText().toString();

        if (nama.equals("")) {
            Toast.makeText(VoteActivity.this, "Silahkan lengkapi nama Anda", Toast.LENGTH_SHORT).show();
        } else if (no.equals("")) {
            Toast.makeText(VoteActivity.this, "Silahkan isi no hp/ whatsapp Anda", Toast.LENGTH_SHORT).show();
        } else if (radioKelasButton == null) {
            Toast.makeText(VoteActivity.this, "Pilih kelas Anda waktu SMA", Toast.LENGTH_SHORT).show();
        } else if (!chkSMA.isChecked() && !chkBarinten.isChecked() && !chkLain.isChecked()) {
            Toast.makeText(VoteActivity.this, "Pilih lokasi reuni", Toast.LENGTH_SHORT).show();
        } else if (chkLain.isChecked() && etLain.getText().toString().equals("")) {
            Toast.makeText(VoteActivity.this, "Isi pilihan lokasi lainnya", Toast.LENGTH_SHORT).show();
        } else if (!chk27.isChecked() && !chk28.isChecked() && !chk29.isChecked()&& !chk30.isChecked()
                && !chk1.isChecked() && !chk2018.isChecked()) {
            Toast.makeText(VoteActivity.this, "Pilih tanggal reuni", Toast.LENGTH_SHORT).show();
        } else {
            final ProgressDialog progress = new ProgressDialog(this);
            progress.setMessage("Mohon tunggu...");
            progress.show();
            APIService apiService = APIClient.getService();
            Call<Value> call = apiService.insert(nama, no, radioKelasButton.getText().toString(), sma,
                    rumah, lain, tgl27, tgl28, tgl29, tgl30, tgl1, tgl2018, budget, keterangan);
            call.enqueue(new Callback<Value>() {
                @Override
                public void onResponse(Call<Value> call, Response<Value> response) {
                    progress.dismiss();
                    if (response.body().value.equals("1")) {
                        Intent intent=new Intent();
                        intent.putExtra("MESSAGE","update");
                        setResult(2,intent);
                        finish();
                    } else {
                        Toast.makeText(VoteActivity.this, "Vote gagal silahkan coba lagi", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Value> call, Throwable t) {
                    progress.dismiss();
                    t.printStackTrace();
                    Toast.makeText(VoteActivity.this, "Vote gagal silahkan coba lagi", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private int checkValue(CheckBox checkBox){
        int i;
        if (checkBox.isChecked()) {
            i = 1;
        } else {
            i = 0;
        }
        return i;
    }

}
