package com.sapa.solulife.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sapa.solulife.R;

/**
 * Created by Saeed Jassani on 30-09-2016.
 */

public class ChapBotRecyclerAdapter extends RecyclerView.Adapter<ChapBotRecyclerAdapter.MyViewHolder> {

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return null;
	}

	@Override
	public int getItemCount() {
		return 0;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {

	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		TextView textView;

		public MyViewHolder(View itemView) {
			super(itemView);
			textView = (TextView) itemView.findViewById(R.id.chat_text);
		}
	}
}
