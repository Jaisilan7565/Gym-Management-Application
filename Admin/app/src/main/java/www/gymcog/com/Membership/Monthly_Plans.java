package www.gymcog.com.Membership;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import www.gymcog.com.R;


public class Monthly_Plans extends Fragment {
    TextView pb1, pb2, pb3, pprice, gb1, gb2, gb3, gprice, sb1, sb2, sb3, sprice;
    ImageView pedit, gedit, sedit;

    String pb1_value, pb2_value, pb3_value, pprice_value,
            gb1_value, gb2_value, gb3_value, gprice_value,
            sb1_value, sb2_value, sb3_value, sprice_value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monthly_plans, container, false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refrence = FirebaseDatabase.getInstance().getReference()
                .child("memberships").child("monthly");

        pedit = view.findViewById(R.id.pedit);
        pb1 = view.findViewById(R.id.pb1);
        pb2 = view.findViewById(R.id.pb2);
        pb3 = view.findViewById(R.id.pb3);
        pprice = view.findViewById(R.id.pprice);

        gedit = view.findViewById(R.id.gedit);
        gb1 = view.findViewById(R.id.gb1);
        gb2 = view.findViewById(R.id.gb2);
        gb3 = view.findViewById(R.id.gb3);
        gprice = view.findViewById(R.id.gprice);

        sedit = view.findViewById(R.id.sedit);
        sb1 = view.findViewById(R.id.sb1);
        sb2 = view.findViewById(R.id.sb2);
        sb3 = view.findViewById(R.id.sb3);
        sprice = view.findViewById(R.id.sprice);

//Platinum
        pedit.setOnClickListener(new View.OnClickListener() {
            DialogPlus dialogPlus = DialogPlus.newDialog(getContext())
                    .setExpanded(true, 1500)
                    .setContentHolder(new ViewHolder(R.layout.membership_update))
                    .setBackgroundColorResId(R.color.transparent).create();

            @Override
            public void onClick(View v) {
                View myview = dialogPlus.getHolderView();
                final EditText b1 = myview.findViewById(R.id.b1);
                final EditText b2 = myview.findViewById(R.id.b2);
                final EditText b3 = myview.findViewById(R.id.b3);
                final EditText price = myview.findViewById(R.id.price);
                Button submit = myview.findViewById(R.id.update_btn);

                b1.setText(pb1_value);
                b2.setText(pb2_value);
                b3.setText(pb3_value);
                price.setText(pprice_value);

                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (b1.getText().toString().trim().equals("") ||
                                b2.getText().toString().trim().equals("") ||
                                b3.getText().toString().trim().equals("") ||
                                price.getText().toString().trim().equals("")) {
                            Toast.makeText(getContext(), "Fields Cannot be Empty.", Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, Object> map = new HashMap<>();
                            map.put("Benifit_1", b1.getText().toString().trim());
                            map.put("Benifit_2", b2.getText().toString().trim());
                            map.put("Benifit_3", b3.getText().toString().trim());
                            map.put("Price", price.getText().toString().trim());

                            refrence.child("Platinum").updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialogPlus.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
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

        refrence.child("Platinum").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pb1_value = String.valueOf(dataSnapshot.child("Benifit_1").getValue());
                pb2_value = String.valueOf(dataSnapshot.child("Benifit_2").getValue());
                pb3_value = String.valueOf(dataSnapshot.child("Benifit_3").getValue());
                pprice_value = String.valueOf(dataSnapshot.child("Price").getValue());

                pb1.setText(pb1_value);
                pb2.setText(pb2_value);
                pb3.setText(pb3_value);
                pprice.setText("Rs. " + pprice_value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });


//GOLD
        gedit.setOnClickListener(new View.OnClickListener() {
            DialogPlus dialogPlus = DialogPlus.newDialog(getContext())
                    .setExpanded(true, 1500)
                    .setContentHolder(new ViewHolder(R.layout.membership_update))
                    .setBackgroundColorResId(R.color.transparent).create();

            @Override
            public void onClick(View v) {
                View myview = dialogPlus.getHolderView();
                final EditText b1 = myview.findViewById(R.id.b1);
                final EditText b2 = myview.findViewById(R.id.b2);
                final EditText b3 = myview.findViewById(R.id.b3);
                final EditText price = myview.findViewById(R.id.price);
                Button submit = myview.findViewById(R.id.update_btn);

                b1.setText(gb1_value);
                b2.setText(gb2_value);
                b3.setText(gb3_value);
                price.setText(gprice_value);

                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (b1.getText().toString().trim().equals("") ||
                                b2.getText().toString().trim().equals("") ||
                                b3.getText().toString().trim().equals("") ||
                                price.getText().toString().trim().equals("")) {
                            Toast.makeText(getContext(), "Fields Cannot be Empty.", Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, Object> map = new HashMap<>();
                            map.put("Benifit_1", b1.getText().toString().trim());
                            map.put("Benifit_2", b2.getText().toString().trim());
                            map.put("Benifit_3", b3.getText().toString().trim());
                            map.put("Price", price.getText().toString().trim());

                            refrence.child("Gold").updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialogPlus.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
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

        refrence.child("Gold").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                gb1_value = String.valueOf(dataSnapshot.child("Benifit_1").getValue());
                gb2_value = String.valueOf(dataSnapshot.child("Benifit_2").getValue());
                gb3_value = String.valueOf(dataSnapshot.child("Benifit_3").getValue());
                gprice_value = String.valueOf(dataSnapshot.child("Price").getValue());

                gb1.setText(gb1_value);
                gb2.setText(gb2_value);
                gb3.setText(gb3_value);
                gprice.setText("Rs. " + gprice_value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

//SILVER
        sedit.setOnClickListener(new View.OnClickListener() {
            DialogPlus dialogPlus = DialogPlus.newDialog(getContext())
                    .setExpanded(true, 1500)
                    .setContentHolder(new ViewHolder(R.layout.membership_update))
                    .setBackgroundColorResId(R.color.transparent).create();

            @Override
            public void onClick(View v) {
                View myview = dialogPlus.getHolderView();
                final EditText b1 = myview.findViewById(R.id.b1);
                final EditText b2 = myview.findViewById(R.id.b2);
                final EditText b3 = myview.findViewById(R.id.b3);
                final EditText price = myview.findViewById(R.id.price);
                Button submit = myview.findViewById(R.id.update_btn);

                b1.setText(sb1_value);
                b2.setText(sb2_value);
                b3.setText(sb3_value);
                price.setText(sprice_value);

                dialogPlus.show();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (b1.getText().toString().trim().equals("") ||
                                b2.getText().toString().trim().equals("") ||
                                b3.getText().toString().trim().equals("") ||
                                price.getText().toString().trim().equals("")) {
                            Toast.makeText(getContext(), "Fields Cannot be Empty.", Toast.LENGTH_SHORT).show();
                        } else {
                            Map<String, Object> map = new HashMap<>();
                            map.put("Benifit_1", b1.getText().toString().trim());
                            map.put("Benifit_2", b2.getText().toString().trim());
                            map.put("Benifit_3", b3.getText().toString().trim());
                            map.put("Price", price.getText().toString().trim());

                            refrence.child("Silver").updateChildren(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            dialogPlus.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
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

        refrence.child("Silver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sb1_value = String.valueOf(dataSnapshot.child("Benifit_1").getValue());
                sb2_value = String.valueOf(dataSnapshot.child("Benifit_2").getValue());
                sb3_value = String.valueOf(dataSnapshot.child("Benifit_3").getValue());
                sprice_value = String.valueOf(dataSnapshot.child("Price").getValue());

                sb1.setText(sb1_value);
                sb2.setText(sb2_value);
                sb3.setText(sb3_value);
                sprice.setText("Rs. " + sprice_value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}