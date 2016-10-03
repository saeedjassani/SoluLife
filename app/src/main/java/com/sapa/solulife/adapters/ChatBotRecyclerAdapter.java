package com.sapa.solulife.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sapa.solulife.R;
import com.sapa.solulife.data.ChatData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saeed Jassani on 30-09-2016.
 */

public class ChatBotRecyclerAdapter extends RecyclerView.Adapter<ChatBotRecyclerAdapter.MyViewHolder> {

	private final Activity activity;
	List<ChatData> dataList = new ArrayList<>();

	public ChatBotRecyclerAdapter(Activity activity) {
		this.activity = activity;
	}

	public void addMessage(ChatData chatData) {
		dataList.add(chatData);
		notifyItemInserted(dataList.size());
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new MyViewHolder(activity.getLayoutInflater().inflate(R.layout.row_chat, parent, false));
	}

	@Override
	public int getItemCount() {
		return dataList.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		switch (dataList.get(position).getPlace()) {
			case 0:
				holder.textView.setText(dataList.get(position).getMessage());
				holder.textView.setTextColor(Color.WHITE);
				break;
			case 1:
				holder.textView.setText(dataList.get(position).getMessage());
				holder.textView.setTextColor(Color.BLACK);
				break;
		}
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		TextView textView;

		MyViewHolder(View itemView) {
			super(itemView);
			textView = (TextView) itemView.findViewById(R.id.chat_text);
		}
	}
}
