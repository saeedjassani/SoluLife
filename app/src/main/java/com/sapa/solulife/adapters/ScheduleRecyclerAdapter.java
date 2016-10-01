package com.sapa.solulife.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sapa.solulife.R;
import com.sapa.solulife.data.ScheduleData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saeed Jassani on 30-09-2016.
 */

public class ScheduleRecyclerAdapter extends RecyclerView.Adapter<ScheduleRecyclerAdapter.MyHolderView> {

	Activity activity;
	List<ScheduleData> dataList = new ArrayList<>();

	public ScheduleRecyclerAdapter(Activity activity) {
		this.activity = activity;
	}

	public void addMessage(ScheduleData data) {
		dataList.add(data);
		notifyItemInserted(dataList.size());
	}

	@Override
	public MyHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
		return new MyHolderView(activity.getLayoutInflater().inflate(R.layout.row_schedule, parent, false));
	}

	@Override
	public void onBindViewHolder(MyHolderView holder, int position) {
		holder.type.setText(dataList.get(position).getType());
		holder.desc.setText(dataList.get(position).getDescription());
		holder.quantity.setText(dataList.get(position).getQty());
		holder.time.setText(dataList.get(position).getStart_time());
	}

	@Override
	public int getItemCount() {
		return dataList.size();
	}

	class MyHolderView extends RecyclerView.ViewHolder {

		TextView type, desc, quantity, time;

		public MyHolderView(View itemView) {
			super(itemView);
			type = (TextView) itemView.findViewById(R.id.schedule_type);
			desc = (TextView) itemView.findViewById(R.id.schedule_desc);
			quantity = (TextView) itemView.findViewById(R.id.schedule_quantity);
			time = (TextView) itemView.findViewById(R.id.schedule_time);
		}
	}
}
