package com.sapa.solulife.Notes;

/**
 * Created by Pooja S on 9/30/2016.
 */
import android.app.Activity;
import android.content.Context;

import android.graphics.Color;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.*;
import android.widget.*;
import com.sapa.solulife.R;

import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder>{

    private List<Note> notes;
    Context context;
    private static OnItemClickListener onItemClickListener;
    private static OnLongItemClickListener onLongItemClickListener;
    public DatabaseHelper databaseHelper;
    Activity activity;
    private int which;
    private List<Note> filteredList;
    private int lastPosition = -1;
    View view;
    String drawableText;
    private static final String EXTRA_NOTE = "EXTRA_NOTE";
    String STAGGER_CONTENT = "EXTRA CONTENT";

    private static final DateFormat DATETIME_FORMAT = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);

    public NotesAdapter(List<Note> notes,Context context, Activity activity) {
        this.notes = notes;
        this.context = context;
        this.activity = activity;
        this.filteredList = new ArrayList<>();
    }

    public static void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        NotesAdapter.onItemClickListener = onItemClickListener;
    }

    public static void setOnLongItemClickListener(OnLongItemClickListener onLongItemClickListener) {
        NotesAdapter.onLongItemClickListener = onLongItemClickListener;
    }


    @Override
    public long getItemId(int position) {
        return notes.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public static Comparator<Note> newestFirstComparator = new Comparator<Note>() {
        @Override
        public int compare(Note lhs, Note rhs) {
            String lDate = lhs.getUpdatedAt();
            String rDate = rhs.getUpdatedAt();
            return rDate.compareTo(lDate);
        }
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        int color = context.getResources().getColor(R.color.white);
        int color1 = context.getResources().getColor(R.color.black);
        int color2 = context.getResources().getColor(R.color.primary_dark);
        int black = context.getResources().getColor(R.color.toolbarblack);

        Note note = notes.get(position);
        holder.textRow.setText(note.getTitle());
        holder.textUpdated.setText(note.getUpdatedAt());

        SpannableString hashText = new SpannableString(note.getContent());
        Matcher matcher = Pattern.compile("#([A-Za-z1-9_-]+)").matcher(hashText);
        while (matcher.find()) {
            hashText.setSpan(new ForegroundColorSpan(Color.parseColor("#FF5722")), matcher.start(), matcher.end(), 0);
        }

        if(note.getFavourite() == 1){
            holder.star.setVisibility(View.INVISIBLE);
        }else if(note.getFavourite() == 0){
            holder.star.setVisibility(View.INVISIBLE);
        }


            holder.textContent.setMaxLines(8);

            holder.cardView.setCardBackgroundColor(note.getColor());


            holder.textRow.setTextColor(color);
            holder.textContent.setTextColor(color);
            holder.textContent.setLinkTextColor(color);
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
            textContent = (TextView) parent.findViewById(R.id.note_content);
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
