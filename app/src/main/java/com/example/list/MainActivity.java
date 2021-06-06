package com.example.list;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    Display display;
    PMDBHelper ldb;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Project Manager");
        this.ldb = new PMDBHelper(this);
        Display d = getWindowManager().getDefaultDisplay();
        this.display = d;
        int width = this.display.getWidth();

       // deleteDatabase("PMDB.db");

       display();
       Project_list();



    }

    public void projectClick(View v){
        Button b = (Button)v;
        Intent i = new Intent(this, ProjectPage.class);

        PMDBHelper ldb = new PMDBHelper(this);
        Cursor ProjectsCursor =  ldb.getProjects(b.getText().toString());
        ProjectsCursor.moveToFirst();
        Project np = new Project(ProjectsCursor.getString(0));

        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.projectSer), np);
        i.putExtras(bundle);
        startActivity(i);


    }

    private void Project_list(){
        this.cursor =  ldb.getProjects("");
        cursor.moveToFirst();
        String[] fromColumn = {cursor.getColumnName(1)};
        int[] toView = {R.id.projectItem};
        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.listview, cursor, fromColumn, toView,0);
        ListView listView = (ListView) findViewById(R.id.projects_list);
        listView.setAdapter(cursorAdapter);

    }

    private void display(){
        int height = this.display.getHeight();

        ListView listView = (ListView) findViewById(R.id.projects_list);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = ((int)(height * 0.80));
        listView.setLayoutParams(params);

        TextView padding1 = (TextView) findViewById(R.id.padding_main1);
        padding1.setHeight((int)(height * 0.015));
        padding1.setVisibility(View.INVISIBLE);

        TextInputEditText textP = (TextInputEditText) findViewById(R.id.projectName);
        textP.setHeight((int)(height * 0.05));
        textP.setVisibility(View.GONE);

        TextView padding2 = (TextView) findViewById(R.id.padding_main2);
        padding2.setHeight((int)(height * 0.015));
        padding2.setVisibility(View.GONE);

        Button cancel = (Button) findViewById(R.id.cancel_project);
        cancel.setHeight((int)(height * 0));
        cancel.setVisibility(View.GONE);

        TextView padding3 = (TextView) findViewById(R.id.padding_main3);
        padding3.setHeight((int)(height * 0.015));
        padding3.setVisibility(View.GONE);

        Button add = (Button) findViewById(R.id.new_project);
        add.setHeight((int)(height * 0.05));
        add.setText(getString(R.string.new_project));

        TextView padding4 = (TextView) findViewById(R.id.padding_main4);
        padding4.setHeight((int)(height * 0.5));


    }

    public void newProject(View v){
        Button b = (Button)v;

        if(b.getText()==getString(R.string.new_project)) {

            int height = this.display.getHeight();

            ListView listView = (ListView) findViewById(R.id.projects_list);
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = ((int) (height * 0.65));
            listView.setLayoutParams(params);

            TextInputEditText textP = (TextInputEditText) findViewById(R.id.projectName);
            textP.setVisibility(View.VISIBLE);

            TextView padding2 = (TextView) findViewById(R.id.padding_main2);
            padding2.setVisibility(View.VISIBLE);

            b.setText(getString(R.string.create_project));

            TextView padding3 = (TextView) findViewById(R.id.padding_main3);
            padding3.setVisibility(View.VISIBLE);

            Button cancel = (Button) findViewById(R.id.cancel_project);
            cancel.setVisibility(View.VISIBLE);
        }
        else{
            TextInputEditText nameText = (TextInputEditText) findViewById(R.id.projectName);
            String name = (nameText.getText().toString());
            if(name.length() == 0){Toast.makeText(getApplicationContext(),"Empty name", Toast.LENGTH_SHORT).show();}
            else {
                if(chackName(name)){
                    Project np = new Project(name);
                    nameText.setText("");

                    PMDBHelper ldb = new PMDBHelper(this);
                    ldb.addProject(np);
                    Project_list();
                    display();
                }
                else { Toast.makeText(getApplicationContext(),"Duplicate name", Toast.LENGTH_LONG).show(); }
            }

        }
    }

    public boolean chackName(String name) {
        if (this.cursor.getCount() == 0) return  true;
        name = name.toLowerCase();
        this.cursor.moveToFirst();
        do {
            String aux=(this.cursor.getString(1)).toLowerCase();
            if( aux.equals( name))return false;
        } while(this.cursor.moveToNext());
        return true;


    }

    public void cancelProject(View b){ display(); }

}