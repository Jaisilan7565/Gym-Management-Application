package www.gymcog.trainer_n_client.gymcog.Trainer.Trainee;

import static android.view.View.OnClickListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import www.gymcog.trainer_n_client.gymcog.R;


public class myadapter extends FirebaseRecyclerAdapter<model, myadapter.myviewholder> {
    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull final model model) {
        holder.name.setText(model.getName());
        holder.phone.setText(model.getPhone());
        holder.plan.setText(model.getLastPlan());
        holder.valid.setText(model.getPlan_valid_till());

//       holder.pass.setText(model.getPass());
//       Glide.with(holder.img.getContext()).load(model.getPass()).into(holder.img);

        holder.edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.phone.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_dialogcontent))
                        .setBackgroundColorResId(R.color.mint)
                        .setExpanded(true, 1300)
                        .create();

                View myview = dialogPlus.getHolderView();
                final EditText pass = myview.findViewById(R.id.tpassword);
                final EditText name = myview.findViewById(R.id.tname);
                final EditText phone = myview.findViewById(R.id.tphone);
                final EditText email = myview.findViewById(R.id.temail);
                Button submit = myview.findViewById(R.id.tsubmit);

                pass.setText(model.getPass());
                name.setText(model.getName());
                phone.setText(model.getPhone());
                email.setText(model.getEmail());

                dialogPlus.show();

                submit.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (name.getText().toString().trim().equals("")) {
                            name.setHint("Name Cannot be Empty.");
                            name.requestFocus();
                        } else if (email.getText().toString().trim().equals("")) {
                            email.setHint("Email Cannot be Empty.");
                            email.requestFocus();
                        } else if (phone.getText().toString().trim().equals("")) {
                            phone.setHint("Phone Number Cannot be Empty.");
                            phone.requestFocus();
                        } else if (pass.getText().toString().trim().equals("")) {
                            pass.setHint("Password Cannot be Empty.");
                            pass.requestFocus();
                        } else  {
                            String tmp_phone = phone.getText().toString();
                            String old_phone=model.getPhone();

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                                    .child("clients").child(old_phone);
                            DatabaseReference client_payment = FirebaseDatabase.getInstance().getReference()
                                    .child("client_payments").child(old_phone);
                            DatabaseReference client_workout = FirebaseDatabase.getInstance().getReference()
                                    .child("client_workout").child(old_phone);

                            if (!tmp_phone.equals(old_phone)) {

                                client_workout.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot days : snapshot.getChildren()) {
                                            String daysKey = days.getKey();
                                            String no_rep= (String) days.child("no_rep").getValue();
                                            String no_set= (String) days.child("no_set").getValue();
                                            String title= (String) days.child("title").getValue();
                                            String workout_1= (String) days.child("workout_1").getValue();
                                            String workout_2= (String) days.child("workout_2").getValue();
                                            String workout_3= (String) days.child("workout_3").getValue();
                                            String workout_4= (String) days.child("workout_4").getValue();
                                            String workout_5= (String) days.child("workout_5").getValue();
                                            String workout_6= (String) days.child("workout_6").getValue();

                                            Map<String, Object> map1 = new HashMap<>();
                                            map1.put("no_rep",no_rep);
                                            map1.put("no_set",no_set);
                                            map1.put("title", title);
                                            map1.put("workout_1", workout_1);
                                            map1.put("workout_2", workout_2);
                                            map1.put("workout_3", workout_3);
                                            map1.put("workout_4", workout_4);
                                            map1.put("workout_5", workout_5);
                                            map1.put("workout_6", workout_6);

                                            FirebaseDatabase.getInstance().getReference().child("client_workout")
                                                    .child(tmp_phone).child(daysKey).updateChildren(map1);

                                            FirebaseDatabase.getInstance().getReference().child("client_workout")
                                                    .child(old_phone).removeValue();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                client_payment.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot timestamp : snapshot.getChildren()) {
                                            String timestampKey = timestamp.getKey();
                                            String amount= (String) timestamp.child("amount").getValue();
                                            String membership_type= (String) timestamp.child("membership_type").getValue();
                                            String name= (String) timestamp.child("name").getValue();
                                            String payment_date= (String) timestamp.child("payment_date").getValue();
                                            String payment_id= (String) timestamp.child("payment_id").getValue();

                                            Map<String, Object> map2 = new HashMap<>();
                                            map2.put("amount",amount);
                                            map2.put("membership_type",membership_type);
                                            map2.put("name", name);
                                            map2.put("payment_date", payment_date);
                                            map2.put("payment_id", payment_id);


                                            FirebaseDatabase.getInstance().getReference().child("client_payments")
                                                    .child(tmp_phone).child(timestampKey).updateChildren(map2);

                                            FirebaseDatabase.getInstance().getReference().child("client_payments")
                                                    .child(old_phone).removeValue();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                databaseReference.child(old_phone).get().addOnSuccessListener(dataSnapshot -> {
                                    databaseReference.child(tmp_phone).setValue(dataSnapshot.getValue());
                                    FirebaseDatabase.getInstance().getReference().child("clients")
                                            .child(old_phone).removeValue();
                                });
                            }

                            Map<String, Object> map = new HashMap<>();
                            map.put("pass", pass.getText().toString().trim());
                            map.put("name", name.getText().toString().trim());
                            map.put("email", email.getText().toString().trim());
                            map.put("phone", phone.getText().toString().trim());
                            map.put("lastPlan", holder.plan.getText().toString());
                            map.put("plan_valid_till", holder.valid.getText().toString());

                            FirebaseDatabase.getInstance().getReference().child("clients").child(tmp_phone).updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            dialogPlus.dismiss();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialogPlus.dismiss();
                                        }
                                    });
                        }
                    }
                });
            }
        });


        holder.delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.phone.getContext());
                builder.setTitle("Delete now ?");
                builder.setMessage("Deleting will remove the Trainee from your Trainees list.");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String tmp_phone = model.getPhone();
                        FirebaseDatabase.getInstance().getReference().child("clients")
                                .child(tmp_phone).removeValue();
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
    } // End of OnBindViewMethod

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false);
        return new myviewholder(view);
    }

    static class myviewholder extends RecyclerView.ViewHolder {
        //        CircleImageView img;
        ImageView edit, delete;
        TextView name, phone, plan, valid;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
//            img=(CircleImageView) itemView.findViewById(R.id.img1);
            name = (TextView) itemView.findViewById(R.id.nametext);
            phone = (TextView) itemView.findViewById(R.id.phonetext);
            plan = (TextView) itemView.findViewById(R.id.plantext);
            phone = (TextView) itemView.findViewById(R.id.phonetext);
            valid= (TextView) itemView.findViewById(R.id.valid_till);
            edit = (ImageView) itemView.findViewById(R.id.editicon);
            delete = (ImageView) itemView.findViewById(R.id.deleteicon);

        }
    }
}
