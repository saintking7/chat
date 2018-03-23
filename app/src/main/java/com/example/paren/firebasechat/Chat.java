package com.example.paren.firebasechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chat extends AppCompatActivity{

    private Button btn_send_msg;
    private EditText input_msg;
    private TextView chat_msg;
    private String user_name;
    private DatabaseReference root;
    private String temp_key;
    private ImageButton btn_logout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        btn_send_msg = (Button)findViewById(R.id.send_button);
        btn_logout = (ImageButton)findViewById(R.id.logout_button);
        input_msg = (EditText)findViewById(R.id.message_field);
        chat_msg = (TextView)findViewById(R.id.textView);
        user_name = getIntent().getExtras().get("user_name").toString();
        root = FirebaseDatabase.getInstance().getReference().getRoot();
        chat_msg.setText("");
        btn_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name", user_name);
                map2.put("message", input_msg.getText().toString());
                message_root.updateChildren(map2);
                input_msg.setText("");
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat.this,MainActivity.class);
                startActivity(intent);
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                append_chat_msg(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                append_chat_msg(dataSnapshot);
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

    private String chat_m, chat_username;
    private void append_chat_msg(DataSnapshot dataSnapshot){
        Iterator i = dataSnapshot.getChildren().iterator();
        chat_m = (String) ((DataSnapshot)i.next()).getValue();
        chat_username = (String) ((DataSnapshot)i.next()).getValue();
        chat_msg.append(chat_username + " : " + chat_m + "\n");

    }
}
