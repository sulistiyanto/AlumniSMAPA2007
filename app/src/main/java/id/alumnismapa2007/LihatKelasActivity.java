package id.alumnismapa2007;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LihatKelasActivity extends AppCompatActivity {

    private List<Result> resultList;
    private AdapterAlumni adapterAlumni;
    private ConnectingToNetwork mConnectingToNetwork;
    private boolean refresh;
    String key;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.linearLayoutRefresh)
    LinearLayout linearLayoutRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_kelas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        getSupportActionBar().setTitle("Kelas " +key);

        mConnectingToNetwork = new ConnectingToNetwork(this);
        resultList = new ArrayList<>();
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        adapterAlumni = new AdapterAlumni(this, resultList);
        recyclerView.setAdapter(adapterAlumni);
        checkInternet();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh = true;
                checkInternet();
            }
        });


        linearLayoutRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternet();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkInternet() {
        if (!mConnectingToNetwork.isConnecting()) {
            progressBar.setVisibility(View.GONE);
            linearLayoutRefresh.setVisibility(View.VISIBLE);
        } else {
            if (refresh) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
            linearLayoutRefresh.setVisibility(View.GONE);
            getData();
        }
    }

    private void getData() {
        APIService apiService = APIClient.getService();
        Call<Value> call = apiService.lihatKelas(key);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    if (response.body().getValue().equals("1")) {
                        resultList = response.body().getResult();
                        adapterAlumni = new AdapterAlumni(LihatKelasActivity.this, resultList);
                        recyclerView.setAdapter(adapterAlumni);
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                t.printStackTrace();
                FirebaseCrash.report(t);
                linearLayoutRefresh.setVisibility(View.VISIBLE);
            }
        });
    }


}
