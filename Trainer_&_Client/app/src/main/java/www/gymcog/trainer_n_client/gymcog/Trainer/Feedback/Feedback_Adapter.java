package www.gymcog.trainer_n_client.gymcog.Trainer.Feedback;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Trainer.Trainee.model;
import www.gymcog.trainer_n_client.gymcog.Trainer.Workout_Schedule.Workout_Adapter;

public class Feedback_Adapter extends FirebaseRecyclerAdapter<Feedback_Model, Feedback_Adapter.feedbackViewHolder> {

    public Feedback_Adapter(@NonNull FirebaseRecyclerOptions<Feedback_Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Feedback_Adapter.feedbackViewHolder holder, int position, @NonNull Feedback_Model model) {

        holder.feed_text.setText(model.getFeedback());
        holder.feed_name.setText(model.getName());
        holder.feed_phone.setText(model.getPhone());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.feed_name.getContext());
                builder.setTitle("Delete now ?");
                builder.setMessage("Deleting will remove this Feedback from the list.");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String tmp_phone = model.getPhone();
                        DatabaseReference feedbacks = FirebaseDatabase.getInstance().getReference()
                                .child("feedbacks");

                        feedbacks.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String tmp_name=holder.feed_name.getText().toString();
                                String tmp_phone=holder.feed_phone.getText().toString();
                                String tmp_feedback=holder.feed_text.getText().toString();

                                for (DataSnapshot timestamp : snapshot.getChildren()) {
                                    String timestampKey = timestamp.getKey();
                                    String name= (String) timestamp.child("name").getValue();
                                    String phone= (String) timestamp.child("phone").getValue();
                                    String feedback= (String) timestamp.child("feedback").getValue();

                                    if (tmp_feedback.equals(feedback) && tmp_name.equals(name) && tmp_phone.equals(phone)){
                                        FirebaseDatabase.getInstance().getReference().child("feedbacks")
                                                .child(timestampKey).removeValue();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();

            }
        });

    }

    @NonNull
    @Override
    public Feedback_Adapter.feedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_row,parent,false);
        return new Feedback_Adapter.feedbackViewHolder(view);
    }

    public class feedbackViewHolder extends RecyclerView.ViewHolder {

        ImageView delete;
        TextView feed_text,feed_name,feed_phone;
        public feedbackViewHolder(@NonNull View itemView) {
            super(itemView);

            feed_name=itemView.findViewById(R.id.feed_name);
            feed_phone=itemView.findViewById(R.id.feed_phone);
            feed_text=itemView.findViewById(R.id.feed_text);
            delete=itemView.findViewById(R.id.delete_feedback);

        }
    }
}
