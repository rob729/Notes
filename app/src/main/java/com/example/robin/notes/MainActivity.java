package com.example.robin.notes;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ArrayList<task> al = new ArrayList<task>();
    RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();
        if(appSharedPrefs.contains("MyObject")) {
            String json = appSharedPrefs.getString("MyObject", null);
            Type type = new TypeToken<ArrayList<task>>() {
            }.getType();
            al = gson.fromJson(json, type);
        }


        final TaskAdapter taskAdapter= new TaskAdapter(this,al);
        rv.setAdapter(taskAdapter);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.layout_view, null, true);

        final AlertDialog customDialog = new AlertDialog.Builder(this)
                .setTitle("Add a new Task")
                .setView(dialogView)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        EditText t = dialogView.findViewById(R.id.edt1);
                        String tk = t.getText().toString();
                        String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                        al.add(new task(tk,mydate));
                        t.setText("");
                        taskAdapter.notifyDataSetChanged();


                    }
                })
                .create();

        FloatingActionButton mfab = findViewById(R.id.fab);
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.show();
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String data = gson.toJson(al);
        prefsEditor.putString("MyObject",data );
        prefsEditor.apply();


    }
}
