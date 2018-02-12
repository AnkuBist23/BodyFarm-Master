package aman.backender.com.bodyfarm.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import aman.backender.com.bodyfarm.R;
import aman.backender.com.bodyfarm.bean.GymUserInfo;
import aman.backender.com.bodyfarm.bean.UserInfo;

/**
 * Created by abc on 2/9/2018.
 */

public class adapter_client_data extends RecyclerView.Adapter<adapter_client_data.ViewHolder> {
    private final Context context;
    private LayoutInflater inflater;
    private List<GymUserInfo> DataAdapter;


    public adapter_client_data(List<GymUserInfo> DataAdapter, Context context) {

        this.DataAdapter = DataAdapter;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.adapter_client_data, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final GymUserInfo getDataAdapter1 = DataAdapter.get(position);

        holder.cl_name.setText(getDataAdapter1.getFirstName());
        holder.cl_fee_date.setText(getDataAdapter1.getFeeDate());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView cl_name, cl_fee_date;
        public ViewHolder(View itemView) {
            super(itemView);

            cl_fee_date = itemView.findViewById(R.id.cl_fee_date);
            cl_name = itemView.findViewById(R.id.cl_fee_date);
        }
    }
}
