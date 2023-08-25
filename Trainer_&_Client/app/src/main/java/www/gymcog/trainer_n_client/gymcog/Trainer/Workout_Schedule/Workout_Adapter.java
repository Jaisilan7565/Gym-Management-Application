package www.gymcog.trainer_n_client.gymcog.Trainer.Workout_Schedule;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Trainer.Trainee.model;

public class Workout_Adapter extends FirebaseRecyclerAdapter<model,Workout_Adapter.traineeViewHolder> {

    public Workout_Adapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Workout_Adapter.traineeViewHolder holder, int position, @NonNull model model) {

        holder.name.setText(model.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),Schedule.class);
                intent.putExtra("name",model.getName());
                intent.putExtra("phone",model.getPhone());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public Workout_Adapter.traineeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.trainee_singlerow,parent,false);
        return new traineeViewHolder(view);
    }

    public class traineeViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        public traineeViewHolder(@NonNull View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.nametext);
        }
    }
}
