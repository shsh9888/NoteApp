package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ArrayList<String> notes= new ArrayList<String>();
    boolean shouldOnResumeBeCalled = false;
    ArrayAdapter<String> notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shouldOnResumeBeCalled=false;
        setContentView(R.layout.activity_main);
        sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
//        sharedPreferences.edit().remove("myNotes").apply();
//        sharedPreferences.edit().clear().apply();


        String noteString = sharedPreferences.getString("myNotes", "");
        String[] notesArr  = noteString.split(",");
        for(String not : notesArr) {
            if (not != ""){
                notes.add(not);
            }
        }


        if (notes.size() == 0) {
            notes.add("Empty Note");
            String serializedNotes = notes.toString();
            serializedNotes = serializedNotes.substring(1, serializedNotes.length()-1);
            sharedPreferences.edit().putString("myNotes", serializedNotes).apply();
        }
        Log.i("stored", noteString.toString());


        ListView listView = findViewById(R.id.noteList);
        notesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(notesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getBaseContext(), AddNote.class);
                intent.putExtra("note", notes.get(position));
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                notes.remove(position);
                notesAdapter.notifyDataSetChanged();
                return true;
            };
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shouldOnResumeBeCalled) {
            sharedPreferences = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
            String noteString = sharedPreferences.getString("myNotes", "");
            String[] notesArr  = noteString.split(",");
            notes.clear();
            for(String not : notesArr) {
                if (not != ""){
                    notes.add(not);
                }
            }
            Log.i("stored2", noteString.toString());
            notesAdapter.notifyDataSetChanged();
        } else {
            shouldOnResumeBeCalled = true;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.add_note) {
            Intent intent= new Intent(getBaseContext(), AddNote.class);
            intent.putExtra("position", -1);
            startActivity(intent);
        }
        return true;
    }
}
