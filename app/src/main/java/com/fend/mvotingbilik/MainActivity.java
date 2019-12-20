package com.fend.mvotingbilik;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.fend.mvotingbilik.Hasil.HasilVote;
import com.fend.mvotingbilik.Vote.Scan;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.idscan).setOnClickListener(this);
        findViewById(R.id.idhasil).setOnClickListener(this);
        findViewById(R.id.idabout).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idabout:
                about();
                break;
            case R.id.idscan:
                Intent qq = new Intent(getApplicationContext(), Scan.class);
                startActivity(qq);
                break;

            case R.id.idhasil:
                Intent q4q = new Intent(getApplicationContext(), HasilVote.class);
                startActivity(q4q);
                break;}
        }

    public void about() {
        Button back;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        dialogBuilder.setTitle("About App").setIcon(R.drawable.hasil);

        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.about, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog b = dialogBuilder.create();
        b.show();
        back = dialogView.findViewById(R.id.button_cancel);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.dismiss();
            }
        });

    }

}
