package www.gymcog.com.Trainer_Schedule;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import www.gymcog.com.R;
import www.gymcog.com.Trainer.model;

public class TrainerAdapter extends FirebaseRecyclerAdapter<model,TrainerAdapter.trainerViewHolder> {

    public TrainerAdapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull trainerViewHolder holder, int position, @NonNull model model) {
        holder.name.setText(model.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),Set_Time.class);
                intent.putExtra("name",model.getName());
                intent.putExtra("phone",model.getPhone());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public trainerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.trainers_singlerow,parent,false);
        return new trainerViewHolder(view);
    }

 class trainerViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public trainerViewHolder(View view){
            super(view);
            name=(TextView)view.findViewById(R.id.nametext);
        }
 }
}
