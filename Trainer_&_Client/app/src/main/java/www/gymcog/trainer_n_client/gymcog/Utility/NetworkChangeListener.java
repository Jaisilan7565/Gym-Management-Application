package www.gymcog.trainer_n_client.gymcog.Utility;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import www.gymcog.trainer_n_client.gymcog.R;

public class NetworkChangeListener extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        if (!Common.isConnectedToInternet(context)) { //Internet is Not Connected
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.dialogue);
            View layout_dialogue = LayoutInflater.from(context).inflate(R.layout.network_dialogue_box, null);
            builder.setView(layout_dialogue);

            AppCompatButton retry_btn = layout_dialogue.findViewById(R.id.retry_btn);

            //show dialogue
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

            retry_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Common.isConnectedToInternet(context)){
                        Toast.makeText(context, "Please Connect to the Internet", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        dialog.dismiss();
                        onReceive(context, intent);
                    }
                }
            });
        }
    }
}
