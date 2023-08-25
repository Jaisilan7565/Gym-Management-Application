package www.gymcog.trainer_n_client.gymcog.Trainer.Membership_Plans;

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

import www.gymcog.trainer_n_client.gymcog.R;


public class Monthly_Plans extends Fragment {
    TextView pb1, pb2, pb3, pprice, gb1, gb2, gb3, gprice, sb1, sb2, sb3, sprice;

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

        pb1 = view.findViewById(R.id.pb1);
        pb2 = view.findViewById(R.id.pb2);
        pb3 = view.findViewById(R.id.pb3);
        pprice = view.findViewById(R.id.pprice);

        gb1 = view.findViewById(R.id.gb1);
        gb2 = view.findViewById(R.id.gb2);
        gb3 = view.findViewById(R.id.gb3);
        gprice = view.findViewById(R.id.gprice);

        sb1 = view.findViewById(R.id.sb1);
        sb2 = view.findViewById(R.id.sb2);
        sb3 = view.findViewById(R.id.sb3);
        sprice = view.findViewById(R.id.sprice);

//Platinum
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