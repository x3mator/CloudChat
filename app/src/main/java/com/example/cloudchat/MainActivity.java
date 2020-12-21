package com.example.cloudchat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.cloudchat.R.layout.activity_main;

public class MainActivity extends AppCompatActivity {
    private static final int MAX_MESSAGE_LENGTH = 100;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("messages");
    RecyclerView mMessagesRecycle;

    EditText mEditTextMessage;
    FloatingActionButton mSendButton;

    ArrayList<String> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        mEditTextMessage = findViewById(R.id.message_input);
        mSendButton = findViewById(R.id.send_message_b);
        mMessagesRecycle = findViewById(R.id.messages_recycler);

        mMessagesRecycle.setLayoutManager(new LinearLayoutManager(this));

        DataAdapter dataAdapter = new DataAdapter(this, messages);
        mMessagesRecycle.setAdapter(dataAdapter);

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = mEditTextMessage.getText().toString();
                if (msg.equals("")) {
                    Toast.makeText(getApplicationContext(), "Введите сообщение!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (msg.length() > MAX_MESSAGE_LENGTH) {
                    Toast.makeText(getApplicationContext(), "Слишком длинное сообщение!", Toast.LENGTH_SHORT).show();
                    return;
                }
                myRef.push().setValue(msg);
                mEditTextMessage.setText("");
            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String msg = snapshot.getValue(String.class);
                messages.add(msg);
                dataAdapter.notifyDataSetChanged();
                mMessagesRecycle.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}