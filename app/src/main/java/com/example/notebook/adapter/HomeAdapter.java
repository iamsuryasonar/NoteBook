package com.example.notebook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.notebook.R;
import com.example.notebook.model.notesModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;

public class HomeAdapter extends FirebaseRecyclerAdapter<notesModel,HomeAdapter.MyViewHolder> {
    public HomeAdapter(@NonNull FirebaseRecyclerOptions<notesModel> options, ListItemClickListener mOnClickListener) {
        super(options);
        this.mOnClickListener = mOnClickListener;
    }

    private ListItemClickListener mOnClickListener;

    public HomeAdapter(@NonNull FirebaseRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull notesModel model) {

        holder.title.setText(model.getTitle());
        holder.description.setText(model.getDescription());

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_home,parent,false);
        return new MyViewHolder(view);
    }


    //catches reference from single row of recycler view
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView title;
        TextView description;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.tvtitle);
            description = (TextView)itemView.findViewById(R.id.tvdescription);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION && mOnClickListener !=null) {
                mOnClickListener.onListItemClick(getSnapshots().getSnapshot(position),position);
            }
        }
    }

    public  interface ListItemClickListener{
        void onListItemClick(DataSnapshot snapshot, int position);
    }
}