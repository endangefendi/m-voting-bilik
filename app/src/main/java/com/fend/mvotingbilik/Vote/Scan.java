package com.fend.mvotingbilik.Vote;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.fend.mvotingbilik.Hasil.HasilVote;
import com.fend.mvotingbilik.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

import java.util.Objects;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;
import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_NEUTRAL;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class Scan extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;
    private static int camId = Camera.CameraInfo.CAMERA_FACING_BACK;
    String nim = null, nama=null;
    String status = null;
    String hasilScan;
    AlertDialog.Builder builder;
    DatabaseReference databaseVoter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        builder = new AlertDialog.Builder(this);
        //setContentView(R.layout.scan);

        setContentView(scannerView);
        int currentApiVersion = Build.VERSION.SDK_INT;

        if (currentApiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(getApplicationContext(), "Scanning QR Code!", Toast.LENGTH_SHORT).show();
            } else {
                requestPermission();
            }
        }
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    @Override
    public void onResume() {
        super.onResume();

        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(getApplicationContext(), "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                showMessageOKCancel("You need to allow access to both the permissions!!!",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA},
                                                            REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Scan.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(final Result result) {
        hasilScan = result.getText();
        nim = result.getText();
        databaseVoter = FirebaseDatabase.getInstance().getReference();
        databaseVoter.child("Voter").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(nim).exists()) {
                    nama = dataSnapshot.child(nim).child("name").getValue().toString();
                    status = dataSnapshot.child(nim).child("status").getValue().toString();
                    if (status.equals("Not Vote")) {
                        builder.setTitle("Pesan").setIcon(R.drawable.hasil);
                        builder.setMessage("Hallo " + nama + "\nSilahkan pilih calon ketua HUMANIKA.");
                        AlertDialog alert3 = builder.create();
                        alert3.show();
                        alert3.getButton(BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black_overlay));
                        alert3.getButton(BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black_overlay));
                    } else {
                        builder.setTitle("Scan Result").setIcon(R.drawable.hasil);
                        builder.setMessage("Halloo " + nama + "\n" + "Maaf Anda Sudah Vote");
                        builder.setPositiveButton("Ulangi", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                scannerView.resumeCameraPreview(Scan.this);
                            }
                        });
                        builder.setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();//scannerView.resumeCameraPreview(Scan.this);
                            }
                        });
                        AlertDialog alert2 = builder.create();
                        alert2.show();
                        alert2.getButton(BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black_overlay));
                        alert2.getButton(BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black_overlay));
                    }
                } else {
                    builder.setTitle("Scan Result").setIcon(R.drawable.hasil);
                    builder.setMessage("QR Code Salah");
                    builder.setNeutralButton("Tutup", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();//scannerView.resumeCameraPreview(Scan.this);
                        }
                    });
                    builder.setPositiveButton("Ulangi", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            scannerView.resumeCameraPreview(Scan.this);
                        }
                    });
                    AlertDialog alert4 = builder.create();
                    alert4.show();
                    alert4.getButton(BUTTON_NEUTRAL).setTextColor(getResources().getColor(R.color.black_overlay));
                    alert4.getButton(BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.black_overlay));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());
        builder.setTitle("Scan Result");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nim = result.getText();
                String re = result.getText();
                Bundle bun = new Bundle();
                Intent in = new Intent(getBaseContext(), ShowCandidate.class);
                bun.putString("nim", nim);
                in.putExtras(bun);
                startActivity(in);
            }
        });
        builder.setNeutralButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scannerView.resumeCameraPreview(Scan.this);
                    }
                }
        );

    }

}

