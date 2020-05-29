package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;


public class AddNote extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String noteString;
    ArrayList<String> notes = new ArrayList<String>();
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        noteString = sharedPreferences.getString("myNotes", "");
        String[] notesArr  = noteString.split(",");
        for(String not : notesArr) {
            if (not != ""){
                notes.add(not);
            }
        }

        TextView noteText  = (TextView) findViewById(R.id.addNotetext);
        intent = getIntent();
        noteText.setText(intent.getStringExtra("note"));

    }

    @SuppressLint("CommitPrefEdits")
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        TextView noteText  = (TextView) findViewById(R.id.addNotetext);
        String newNote = noteText.getText().toString();

        if (!newNote.equals("")) {
            //Add it to storage
            int position = intent.getIntExtra("position", 0);
            Log.i("Position entered", position + "");
            notes.set(position, newNote);
            String serializedNotes = notes.toString();
            serializedNotes = serializedNotes.substring(1, serializedNotes.length()-1);
            sharedPreferences.edit().putString("myNotes", serializedNotes).apply();
        }
    }
}
