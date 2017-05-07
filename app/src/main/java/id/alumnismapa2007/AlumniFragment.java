package id.alumnismapa2007;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlumniFragment extends Fragment {

    private List<Result> resultList;
    private AdapterAlumni adapterAlumni;
    private ConnectingToNetwork mConnectingToNetwork;
    private boolean refresh;
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
    SharedPreferences sharedpreferences;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.btnVote)
    Button btnVote;
    @BindView(R.id.linearLayoutRefresh)
    LinearLayout linearLayoutRefresh;
    @BindView(R.id.txtEmpty)
    TextView txtEmpty;

    public AlumniFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_alumni, container, false);
        ButterKnife.bind(this, rootView);

        sharedpreferences = getActivity().getSharedPreferences(myPREFERENCES, getActivity().MODE_PRIVATE);
        mConnectingToNetwork = new ConnectingToNetwork(getContext());
        resultList = new ArrayList<>();
        RecyclerView.LayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        adapterAlumni = new AdapterAlumni(getContext(), resultList);
        recyclerView.setAdapter(adapterAlumni);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 ||dy<0 )
                {
                    btnVote.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    btnVote.setVisibility(View.VISIBLE);
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });
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
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            String message = data.getStringExtra("MESSAGE");
            if (message.equals("update")) {
                checkInternet();
            }
        }
    }

    @OnClick(R.id.btnVote)
    void btnVote() {
        Intent intent =new Intent(getContext(), VoteActivity.class);
        startActivityForResult(intent, 2);
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
            txtEmpty.setVisibility(View.GONE);
            getData();
        }
    }

    private void getData() {
        APIService apiService = APIClient.getService();
        Call<Value> call = apiService.view();
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(View.GONE);
                if (response.body() != null) {
                    if (response.body().getValue().equals("1")) {
                        resultList = response.body().getResult();
                        adapterAlumni = new AdapterAlumni(getContext(), resultList);
                        recyclerView.setAdapter(adapterAlumni);

                        if (resultList.size() < 1) {
                            txtEmpty.setVisibility(View.VISIBLE);
                        }
                        int ipa1 = 0;
                        int ipa2 = 0;
                        int ips1 = 0;
                        int ips2 = 0;
                        for (int i = 0; i < resultList.size(); i++) {
                            if (resultList.get(i).getKelas().equals("IPA 1")) {
                                ipa1 ++;
                            }
                            if (resultList.get(i).getKelas().equals("IPA 2")) {
                                ipa2 ++;
                            }
                            if (resultList.get(i).getKelas().equals("IPS 1")) {
                                ips1 ++;
                            }
                            if (resultList.get(i).getKelas().equals("IPS 2")) {
                                ips2 ++;
                            }
                        }

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(sekolah, response.body().getSekolah());
                        editor.putString(rumah, response.body().getRumah());
                        editor.putString(lain, response.body().getLain());
                        editor.putString(tgl27, response.body().getTgl27());
                        editor.putString(tgl28, response.body().getTgl28());
                        editor.putString(tgl29, response.body().getTgl29());
                        editor.putString(tgl30, response.body().getTgl30());
                        editor.putString(tgl1, response.body().getTgl1());
                        editor.putString(tgl2018, response.body().getTgl2018());
                        editor.putString(ip1, ipa1 + "");
                        editor.putString(ip2, ipa2 + "");
                        editor.putString(ip3, ips1 + "");
                        editor.putString(ip4, ips2 + "");
                        editor.commit();
                    } else {
                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                linearLayoutRefresh.setVisibility(View.VISIBLE);
                t.printStackTrace();
                FirebaseCrash.report(t);
            }
        });
    }
}
