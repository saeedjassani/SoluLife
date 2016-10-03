package com.sapa.solulife.Reminders;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sapa.solulife.Database.DatabaseHelper;
import com.sapa.solulife.Notes.Note;
import com.sapa.solulife.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Pooja S on 10/1/2016.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder>{

    private List<Reminder> notes;
    Context context;
    private static ReminderAdapter.OnItemClickListener onItemClickListener;
    private static ReminderAdapter.OnLongItemClickListener onLongItemClickListener;
    public DatabaseHelper databaseHelper;
    Activity activity;
    private int which;
    private List<Reminder> filteredList;
    private int lastPosition = -1;
    View view;
    String drawableText;
    private static final String EXTRA_NOTE = "EXTRA_NOTE";
    String STAGGER_CONTENT = "EXTRA CONTENT";

    private static final DateFormat DATETIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

    public ReminderAdapter(List<Reminder> notes,Context context, Activity activity) {
        this.notes = notes;
        this.context = context;
        this.activity = activity;
    }

    public static void setOnItemClickListener(ReminderAdapter.OnItemClickListener onItemClickListener) {
        ReminderAdapter.onItemClickListener = onItemClickListener;
    }

    public static void setOnLongItemClickListener(ReminderAdapter.OnLongItemClickListener onLongItemClickListener) {
        ReminderAdapter.onLongItemClickListener = onLongItemClickListener;
    }


    @Override
    public long getItemId(int position) {
        return notes.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public static Comparator<Reminder> newestFirstComparator = new Comparator<Reminder>() {
        @Override
        public int compare(Reminder lhs, Reminder rhs) {
            String lDate = lhs.getUpdatedAt();
            String rDate = rhs.getUpdatedAt();
            return rDate.compareTo(lDate);
        }
    };

    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_row, parent, false);
        return new ReminderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ReminderAdapter.ViewHolder holder, int position) {

        int color = context.getResources().getColor(R.color.white);

        Reminder note = notes.get(position);
        holder.textRow.setText(note.getTitle());
        holder.textUpdated.setText(note.getUpdatedAt());

        if(note.getReminderStatus()==1){
            holder.star.setImageDrawable(context.getResources().getDrawable(R.drawable.blur));
        }

        holder.cardView.setCardBackgroundColor(note.getColor());

        holder.textRow.setTextColor(color);
        holder.textUpdated.setTextColor(color);

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView textRow;
        public TextView textContent;
        public TextView textUpdated;
        public ImageView imageView,star,lock;
        public final View view;
        public CardView cardView;
        public RelativeLayout relativeLayout;

        public ViewHolder(View parent) {
            super(parent);
            view = parent;
            textRow = (TextView) parent.findViewById(R.id.textRow);
            textUpdated = (TextView) parent.findViewById(R.id.note_date);
            star = (ImageView) parent.findViewById(R.id.star);
            cardView = (CardView) parent.findViewById(R.id.cardview);
            relativeLayout = (RelativeLayout) parent.findViewById(R.id.relativeLayout);
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getPosition(), v);
                }
            });
            parent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onLongItemClickListener.onLongItemClick(getPosition(), v);
                    return true;
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public interface OnLongItemClickListener {
        void onLongItemClick(int position, View v);
    }
}
