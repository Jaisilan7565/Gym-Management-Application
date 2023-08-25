package www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Payments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import www.gymcog.trainer_n_client.gymcog.R;
import www.gymcog.trainer_n_client.gymcog.Trainer.Trainee.model;
import www.gymcog.trainer_n_client.gymcog.Trainer.Trainee.myadapter;

public class Payment_History_Adapter extends FirebaseRecyclerAdapter<payment_model, Payment_History_Adapter.myviewholder> {

    public Payment_History_Adapter(@NonNull FirebaseRecyclerOptions<payment_model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull Payment_History_Adapter.myviewholder holder, int position, @NonNull payment_model model) {
        holder.payment_id.setText("Payment ID: "+model.getPayment_id());
        holder.payment_date.setText(model.getPayment_date());
        holder.membership_type.setText(model.getMembership_type());
        holder.amount.setText(model.getAmount());
    }

    @NonNull
    @Override
    public Payment_History_Adapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pay_singlerow, parent, false);
        return new Payment_History_Adapter.myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        TextView payment_id, payment_date, membership_type, amount;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            payment_id = (TextView) itemView.findViewById(R.id.paid_id);
            payment_date = (TextView) itemView.findViewById(R.id.paid_date);
            membership_type = (TextView) itemView.findViewById(R.id.plan);
            amount = (TextView) itemView.findViewById(R.id.paid_amount);
        }
    }
}
