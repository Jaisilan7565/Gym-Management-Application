package www.gymcog.com.Payments;

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

public class Salary_Adapter extends FirebaseRecyclerAdapter<model, Salary_Adapter.trainerViewHolder> {

    public Salary_Adapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Salary_Adapter.trainerViewHolder holder, int position, @NonNull model model) {
        holder.name.setText(model.getName());
        holder.ad_salary.setText(model.getSalary());

        String name = model.getName();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(view.getContext(),PayTrainer.class);
                intent.putExtra("name",model.getName());
                intent.putExtra("upi_id",model.getUpi_id());
                intent.putExtra("salary",model.getSalary());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);

            }

        });
    }


    @NonNull
    @Override
    public Salary_Adapter.trainerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.salary_singlerow,parent,false);
        return new trainerViewHolder(view);
    }

    public static class trainerViewHolder extends RecyclerView.ViewHolder {

        TextView name,ad_salary;
        public trainerViewHolder(View view){
            super(view);
            name=(TextView)view.findViewById(R.id.nametext);
            ad_salary=(TextView) view.findViewById(R.id.tv_salary);
        }

    }
}
