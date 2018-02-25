package com.android.paskahlis.anchat;

import android.app.Dialog;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.paskahlis.anchat.entity.EntityUser;
import com.android.paskahlis.anchat.sensor.ShakeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShakeFriendActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor acceleroMeter;
    private ShakeDetector shakeDetector;

    private FirebaseAuth auth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dbReference = database.getReference();

    private String selfId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_friend);

        auth = FirebaseAuth.getInstance();
        selfId = auth.getCurrentUser().getUid();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        acceleroMeter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeDetector = new ShakeDetector();
        final TextView iText = (TextView) findViewById(R.id.indicator_shake);
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                if (count > 0) {
                    iText.setText("Seeking...");
                    dbReference.child(ShakeDetector.SHAKE_ROOT).child(selfId).setValue("mantap");
                    dbReference.child(ShakeDetector.SHAKE_ROOT)
                            .addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    final String key = dataSnapshot.getKey();
                                    if (!key.equals(selfId)) {
                                        dataSnapshot.getRef().setValue(null);
                                        dbReference.child(EntityUser.USER_ROOT).child(key)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        showContactDialog(dataSnapshot, key);
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onChildRemoved(DataSnapshot dataSnapshot) {

                                }

                                @Override
                                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(shakeDetector, acceleroMeter, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(shakeDetector, acceleroMeter);
    }

    private void showContactDialog(DataSnapshot dataSnapshot, final String key) {
        final EntityUser user = dataSnapshot.getValue(EntityUser.class);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.contact_dialog);
        TextView username = (TextView)dialog.findViewById(R.id.username_dialog);
        username.setText(user.getDisplayName());
        dialog.findViewById(R.id.ok_button_dialog).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dbReference.child(EntityUser.USER_ROOT).child(selfId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                EntityUser selfUser = dataSnapshot.getValue(EntityUser.class);
                                selfUser.addContact(key);
                                dataSnapshot.getRef().setValue(selfUser);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        Intent intent = new Intent(ShakeFriendActivity.this, ChatActivity.class);
                        intent.putExtra(ChatActivity.EXTRA_ID, key);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }
        );
        dialog.findViewById(R.id.cancel_button_dialog).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                }
        );
        dialog.setCancelable(false);
        dialog.show();
    }
}
