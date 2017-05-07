package id.alumnismapa2007;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by TOSHIBA on 30/04/2017.
 */

public class AdapterAlumni extends RecyclerView.Adapter<AdapterAlumni.ViewHolder> {

    private Context mContext;
    private List<Result> resultList;

    public AdapterAlumni(Context mContext, List<Result> resultList) {
        this.mContext = mContext;
        this.resultList = resultList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_alumi, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(resultList.get(position));
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.linearLayout)
        LinearLayout linearLayout;
        @BindView(R.id.txtNama)
        TextView txtNama;
        @BindView(R.id.txtKelas)
        TextView txtKelas;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindData(Result result) {
            txtNama.setText(result.getNama());
            txtKelas.setText(result.getKelas());
            if (result.getKelas().equals("IPA 1")) {
                linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
            } else if (result.getKelas().equals("IPA 2")) {
                linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.green));
            } else if (result.getKelas().equals("IPS 1")) {
                linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.amber));
            } else if (result.getKelas().equals("IPS 2")) {
                linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.red));
            }
        }
    }
}
