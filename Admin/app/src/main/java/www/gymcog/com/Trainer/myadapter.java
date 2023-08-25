package www.gymcog.com.Trainer;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

import www.gymcog.com.R;


public class myadapter extends FirebaseRecyclerAdapter<model, myadapter.myviewholder> {
    public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull final model model) {
        holder.name.setText(model.getName());
        holder.phone.setText(model.getPhone());
        holder.email.setText(model.getEmail());

//       holder.pass.setText(model.getPass());
//       Glide.with(holder.img.getContext()).load(model.getPass()).into(holder.img);

        holder.edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.phone.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_dialogcontent))
                        .setExpanded(true, 1300)
                        .create();

                View myview = dialogPlus.getHolderView();
                final EditText pass = myview.findViewById(R.id.tpassword);
                final EditText name = myview.findViewById(R.id.tname);
                final EditText phone = myview.findViewById(R.id.tphone);
                final EditText email = myview.findViewById(R.id.temail);
                final EditText upi_id = myview.findViewById(R.id.tupi_id);
                final EditText salary = myview.findViewById(R.id.tsalary);
                Button submit = myview.findViewById(R.id.tsubmit);

                pass.setText(model.getPass());
                name.setText(model.getName());
                phone.setText(model.getPhone());
                email.setText(model.getEmail());
                upi_id.setText(model.getUpi_id());
                salary.setText(model.getSalary());

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
                        } else if (salary.getText().toString().trim().equals("")) {
                            salary.setHint("Salary Cannot be Empty.");
                            salary.requestFocus();
                        } else {
                            String tmp_phone = phone.getText().toString();
                            String old_phone = model.getPhone();

                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                                    .child("trainers").child(old_phone);
                            if (!tmp_phone.equals(old_phone)) {
                                databaseReference.child("Working_Time").get().addOnSuccessListener(dataSnapshot -> {
                                    FirebaseDatabase.getInstance().getReference().child("trainers")
                                            .  child(tmp_phone).child("Working_Time").setValue(dataSnapshot.getValue());
                                    FirebaseDatabase.getInstance().getReference().child("trainers")
                                            .child(old_phone).removeValue();
                                });
                        }

                        Map<String, Object> map = new HashMap<>();
                        map.put("pass", pass.getText().toString().trim());
                        map.put("name", name.getText().toString().trim());
                        map.put("email", email.getText().toString().trim());
                        map.put("phone", phone.getText().toString().trim());
                        map.put("upi_id", upi_id.getText().toString().trim());
                        map.put("salary", salary.getText().toString().trim());

                        FirebaseDatabase.getInstance().getReference().child("trainers").child(tmp_phone).updateChildren(map)
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


        holder.delete.setOnClickListener(new

    OnClickListener() {
        @Override
        public void onClick (View view){
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.phone.getContext());
            builder.setTitle("Delete now ?");
            builder.setMessage("Deleting will remove the Trainer from your Trainers list.");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String tmp_phone = model.getPhone();
                    FirebaseDatabase.getInstance().getReference().child("trainers")
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
    TextView name, phone, email;

    public myviewholder(@NonNull View itemView) {
        super(itemView);

        name = (TextView) itemView.findViewById(R.id.nametext);
        phone = (TextView) itemView.findViewById(R.id.phonetext);
        email = (TextView) itemView.findViewById(R.id.emailtext);
        phone = (TextView) itemView.findViewById(R.id.phonetext);
        edit = (ImageView) itemView.findViewById(R.id.editicon);
        delete = (ImageView) itemView.findViewById(R.id.deleteicon);

    }
}

}
