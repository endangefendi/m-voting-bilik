package com.fend.mvotingbilik.Hasil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fend.mvotingbilik.R;


public class DetailHasil extends AppCompatActivity {
    TextView no_urut,nama,suara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_hasil);

        no_urut= findViewById(R.id.edit_text_no);

        nama= findViewById(R.id.edit_text_nama);
        suara= findViewById(R.id.suara);


        Intent in=getIntent();
        Bundle bun=this.getIntent().getExtras();
        this.setTitle("Candidate = "+bun.getString("no"));

        no_urut.setText(bun.getString("no"));
        nama.setText(bun.getString("nama"));
        suara.setText(bun.getString("suara"));

    }

}