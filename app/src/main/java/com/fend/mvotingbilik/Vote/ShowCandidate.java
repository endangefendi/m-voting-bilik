package com.fend.mvotingbilik.Vote;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.fend.mvotingbilik.Calon.Calon;
import com.fend.mvotingbilik.Calon.CalonList;
import com.fend.mvotingbilik.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowCandidate extends AppCompatActivity {
    List<Calon> artists;
    ListView lvc;
    EditText n;
    String nim;
    String cek;
    Integer i =1;
    DatabaseReference databaseVoter;
    DatabaseReference databaseCandidate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_candidate);

        Intent ini=getIntent();
        Bundle bunn=this.getIntent().getExtras();
        this.setTitle("Vote");

        nim = bunn.getString("nim");

        if (nim.equals("")){
            finish();
        }
        lvc =  findViewById(R.id.lv_candidate);

        artists = new ArrayList<>();
        tampil(this, lvc);
        lvc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Calon ad = artists.get(position);
                Bundle bun= new Bundle();
                Intent in= new Intent(getBaseContext(),Vote.class);
                bun.putString("no",ad.getNo_urut());
                bun.putString("nama",ad.getNama());
                bun.putString("angkatan",ad.getAngkatan());
                bun.putString("visi",ad.getVisi());
                bun.putString("misi",ad.getMisi());
                bun.putString("des",ad.getDes());
                bun.putString("suara", ad.getSuara());

                bun.putString("nim",nim);

                in.putExtras(bun);
                startActivity(in);

            }
        });
        tampil(this, lvc);
    }

    public void tampil(final Activity context, final ListView listView) {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Candidate");
        artists = new ArrayList<>();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                artists.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting artist
                    Calon sepatu = postSnapshot.getValue(Calon.class);
                    //adding artist to the list
                    artists.add(sepatu);
                }
                //creating adapter
                CalonList sepatuAdapter = new CalonList(context, artists);
                //attaching adapter to the listview
                listView.setAdapter(sepatuAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}