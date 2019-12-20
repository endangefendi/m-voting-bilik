package com.fend.mvotingbilik.Vote;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.fend.mvotingbilik.R;
import com.google.android.gms.common.internal.SignInButtonImpl;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;


public class Vote extends AppCompatActivity {
    TextView no_urut,nama,angkatan,visi, misi, des;
    Button but ;

    EditText n,p;
    String nim,pin; String sta,pp;
    Integer i =1;
    DatabaseReference databaseVoter;
    DatabaseReference databaseCandidate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final String b,c;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote);
        Intent inn = getIntent();
        Bundle bunn = getIntent().getExtras();
        if (bunn != null)
            nim = bunn.getString("nim");

        no_urut = findViewById(R.id.edit_text_no);
        angkatan = findViewById(R.id.edit_text_angkatan);
        nama = findViewById(R.id.edit_text_nama);
        visi = findViewById(R.id.edit_text_visi);
        misi = findViewById(R.id.edit_text_misi);
        des = findViewById(R.id.description);


        Intent in = getIntent();
        Bundle bun = this.getIntent().getExtras();
        this.setTitle("Candidate = " + bun.getString("no"));

        no_urut.setText(bun.getString("no"));
        nama.setText(bun.getString("nama"));
        angkatan.setText(bun.getString("angkatan"));
        visi.setText(bun.getString("visi"));
        misi.setText(bun.getString("misi"));
        des.setText(bun.getString("des"));
        b =(bun.getString("suara"));
        c = (bun.getString("no"));


        but = findViewById(R.id.button);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bb = Integer.parseInt(b);

                int hasil = (bb + 1);
                final String tambah = String.valueOf(hasil);
                databaseCandidate = FirebaseDatabase.getInstance().getReference("Candidate")
                        .child(c).child("suara");
                databaseVoter = FirebaseDatabase.getInstance().getReference("Voter")
                        .child(nim);
                databaseVoter.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String nama = (String) dataSnapshot.child("nama").getValue();
                        sta = (String) dataSnapshot.child("status").getValue();
                        pp = (String) dataSnapshot.child("pin").getValue();
                        if (sta.equals("Not Vote")) {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Vote.this);
                            builder.setCancelable(false);
                            builder.setTitle("Thank you " + nama);
                            builder.setMessage("Vote is successfully");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //if user pressed "yes", then he is allowed to exit from application
                                    databaseVoter.child("status").setValue("Vote");
                                    databaseCandidate.setValue(tambah);
                                    nim="";
                                    finish();
                                    Intent intent = new Intent(Vote.this, Scan.class);
                                    startActivity(intent);

                                }
                            });
                            android.app.AlertDialog alert = builder.create();
                            alert.show();

                            alert.getButton(BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.black_overlay));
                            alert.getButton(BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black_overlay));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
