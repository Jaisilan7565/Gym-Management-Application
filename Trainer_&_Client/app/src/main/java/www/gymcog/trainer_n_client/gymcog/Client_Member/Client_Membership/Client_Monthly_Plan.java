package www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Membership;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import www.gymcog.trainer_n_client.gymcog.Client_Member.Client_Payments.Payment;
import www.gymcog.trainer_n_client.gymcog.R;

public class Client_Monthly_Plan extends Fragment {

    TextView cpb1, cpb2, cpb3, cpprice, cgb1, cgb2, cgb3, cgprice, csb1, csb2, csb3, csprice;

    String cpb1_value, cpb2_value, cpb3_value, cpprice_value,
            cgb1_value, cgb2_value, cgb3_value, cgprice_value,
            csb1_value, csb2_value, csb3_value, csprice_value;

    TextView buy_mp, buy_mg, buy_ms;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client__monthly__plan, container, false);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference refrence = FirebaseDatabase.getInstance().getReference()
                .child("memberships").child("monthly");

        cpb1 = view.findViewById(R.id.cpb1);
        cpb2 = view.findViewById(R.id.cpb2);
        cpb3 = view.findViewById(R.id.cpb3);
        cpprice = view.findViewById(R.id.cpprice);
        buy_mp=view.findViewById(R.id.buy_mp);

        cgb1 = view.findViewById(R.id.cgb1);
        cgb2 = view.findViewById(R.id.cgb2);
        cgb3 = view.findViewById(R.id.cgb3);
        cgprice = view.findViewById(R.id.cgprice);
        buy_mg=view.findViewById(R.id.buy_mg);

        csb1 = view.findViewById(R.id.csb1);
        csb2 = view.findViewById(R.id.csb2);
        csb3 = view.findViewById(R.id.csb3);
        csprice = view.findViewById(R.id.csprice);
        buy_ms=view.findViewById(R.id.buy_ms);

//Platinum
        refrence.child("Platinum").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cpb1_value = String.valueOf(dataSnapshot.child("Benifit_1").getValue());
                cpb2_value = String.valueOf(dataSnapshot.child("Benifit_2").getValue());
                cpb3_value = String.valueOf(dataSnapshot.child("Benifit_3").getValue());
                cpprice_value = String.valueOf(dataSnapshot.child("Price").getValue());

                cpb1.setText(cpb1_value);
                cpb2.setText(cpb2_value);
                cpb3.setText(cpb3_value);
                cpprice.setText("Rs. " + cpprice_value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        buy_mp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String member_type="1 Month Platinum";
                String amount=cpprice_value;

                Intent intent=new Intent(v.getContext(), Payment.class);
                intent.putExtra("member_type",member_type);
                intent.putExtra("amount",amount);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });


//GOLD
        refrence.child("Gold").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cgb1_value = String.valueOf(dataSnapshot.child("Benifit_1").getValue());
                cgb2_value = String.valueOf(dataSnapshot.child("Benifit_2").getValue());
                cgb3_value = String.valueOf(dataSnapshot.child("Benifit_3").getValue());
                cgprice_value = String.valueOf(dataSnapshot.child("Price").getValue());

                cgb1.setText(cgb1_value);
                cgb2.setText(cgb2_value);
                cgb3.setText(cgb3_value);
                cgprice.setText("Rs. " + cgprice_value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        buy_mg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String member_type="1 Month Gold";
                String amount=cgprice_value;

                Intent intent=new Intent(v.getContext(),Payment.class);
                intent.putExtra("member_type",member_type);
                intent.putExtra("amount",amount);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });

//SILVER
        refrence.child("Silver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                csb1_value = String.valueOf(dataSnapshot.child("Benifit_1").getValue());
                csb2_value = String.valueOf(dataSnapshot.child("Benifit_2").getValue());
                csb3_value = String.valueOf(dataSnapshot.child("Benifit_3").getValue());
                csprice_value = String.valueOf(dataSnapshot.child("Price").getValue());

                csb1.setText(csb1_value);
                csb2.setText(csb2_value);
                csb3.setText(csb3_value);
                csprice.setText("Rs. " + csprice_value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        buy_ms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String member_type="1 Month Silver";
                String amount=csprice_value;

                Intent intent=new Intent(v.getContext(),Payment.class);
                intent.putExtra("member_type",member_type);
                intent.putExtra("amount",amount);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);
            }
        });

        return view;
    }
}