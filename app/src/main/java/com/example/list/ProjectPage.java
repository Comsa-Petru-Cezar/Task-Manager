package com.example.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class ProjectPage extends AppCompatActivity {
    Display display;
    Project project;
    Cursor cursor;
    View curentOpenStage = null;

    PMDBHelper ldb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_page);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        this.project = (Project) bundle.getSerializable(getString(R.string.projectSer));
        setTitle(project.getName());
        this.ldb = new PMDBHelper(this);

        this.display = getWindowManager().getDefaultDisplay();
        display();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu.delete, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if ( item.getItemId() == R.id.delete){
            this.ldb.deleteProject(this.project);
        }

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 0);
        return true;


    }

    private void stages_list(){
        this.cursor =  this.ldb.getStages(this.project.getName());

        cursor.moveToFirst();
        //String[] fromColumn = {this.Stages.getColumnName(1)};
        //int[] toView = {R.id.stageItem};
        //SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this, R.layout.stageslist, this.Stages, fromColumn, toView,0);
        stageCursorAdapter cursorAdapter = new stageCursorAdapter(this, cursor);

        ListView listView = (ListView) findViewById(R.id.stages_list);
        listView.setAdapter(cursorAdapter);

    }

    public void stageClick(View v){
        int id = (int)v.getId();
        ImageButton show = ( ImageButton) findViewById(id+1);
        ImageButton up = ( ImageButton) findViewById(id+2);
        ImageButton down= ( ImageButton) findViewById(id+3);
        ImageButton delete = ( ImageButton) findViewById(id+4);
        ImageButton add = (ImageButton) findViewById(id+5);
        ListView list = (ListView) findViewById(id+6);
        ImageButton hide = (ImageButton) findViewById(id+7);
        TextInputEditText taskName = ( TextInputEditText)findViewById(id+8);
        ImageButton remove = (ImageButton) findViewById(id + 9);

        if(show.getVisibility() == View.GONE && add.getVisibility() == View.GONE){
            if(this.curentOpenStage != null)
                stageClick(this.curentOpenStage);
            this.curentOpenStage = v;
            show.setVisibility(View.VISIBLE);
            up.setVisibility(View.VISIBLE);
            down.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        }
        else {

            show.setVisibility(View.GONE);
            up.setVisibility(View.GONE);
            down.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            list.setVisibility(View.GONE);
            hide.setVisibility(View.GONE);
            add.setVisibility(View.GONE);
            taskName.setVisibility(View.GONE);
            remove.setVisibility(View.GONE);
            this.curentOpenStage = null;
        }
    }

    private void display(){
        int height = this.display.getHeight();

        ListView listView = (ListView) findViewById(R.id.stages_list);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = ((int)(height * 0.8));
        listView.setLayoutParams(params);

        Button add = (Button) findViewById(R.id.newStage);
        add.setHeight((int)(height * 0.05));
        add.setText(getString(R.string.new_stage));
        add.setVisibility(View.VISIBLE);

        TextView padding1 = (TextView) findViewById(R.id.padding_project1);
        padding1.setHeight((int)(height * 0.015));
        padding1.setVisibility(View.INVISIBLE);

        TextInputEditText textP = (TextInputEditText) findViewById(R.id.stageName);
        textP.setHeight((int)(height * 0.05));
        textP.setVisibility(View.GONE);

        TextView padding2 = (TextView) findViewById(R.id.padding_project2);
        padding2.setHeight((int)(height * 0.015));
        padding2.setVisibility(View.GONE);

        Button cancel = (Button) findViewById(R.id.cancel_stage);
        cancel.setHeight((int)(height * 0));
        cancel.setVisibility(View.GONE);

        TextView padding3 = (TextView) findViewById(R.id.padding_project3);
        padding3.setHeight((int)(height * 0.015));
        padding3.setVisibility(View.GONE);



        TextView padding4 = (TextView) findViewById(R.id.padding_project4);
        padding4.setHeight((int)(height * 0.5));

        stages_list();

    }

    public void newStage(View v){
       Button b = (Button)v;

       if(b.getText()==getString(R.string.new_stage)) {

           int height = this.display.getHeight();

           ListView listView = (ListView) findViewById(R.id.stages_list);
           ViewGroup.LayoutParams params = listView.getLayoutParams();
           params.height = ((int) (height * 0.65));
           listView.setLayoutParams(params);

           TextInputEditText textP = (TextInputEditText) findViewById(R.id.stageName);
           textP.setVisibility(View.VISIBLE);

           TextView padding2 = (TextView) findViewById(R.id.padding_project2);
           padding2.setVisibility(View.VISIBLE);

           b.setText(getString(R.string.create_stage));

           TextView padding3 = (TextView) findViewById(R.id.padding_project3);
           padding3.setVisibility(View.VISIBLE);

           Button cancel = (Button) findViewById(R.id.cancel_stage);
           cancel.setVisibility(View.VISIBLE);
       }
       else{


           TextInputEditText name = (TextInputEditText) findViewById(R.id.stageName);
           int id = this.ldb.getNewStageID();
           String namestr = name.getText().toString();
           if(namestr.length() == 0) { Toast.makeText(getApplicationContext(),"Empty name", Toast.LENGTH_SHORT).show();}
            else{
                if(chackSName(namestr)){
                    Stage stage = new Stage(id, namestr, this.project);
                    this.ldb.addStage(stage);


                    this.ldb.updateProject(this.project);

                    name.setText("");


                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
                else{ Toast.makeText(getApplicationContext(),"Duplicate name", Toast.LENGTH_LONG).show(); }
           }


       }

    }

    public void cancelStage(View v){ display();}

    public void show(View v){
        int id = (int)v.getId()-1;
        showSecundus(id);

    }

    public void showSecundus(int id){
        ImageButton show = (ImageButton) findViewById(id+1);
        ImageButton up = (ImageButton) findViewById(id+2);
        ImageButton down= (ImageButton) findViewById(id+3);
        ImageButton delete = (ImageButton) findViewById(id+4);
        ImageButton add = (ImageButton) findViewById(id+5);
        ListView list = (ListView) findViewById(id+6);
        ImageButton hide = (ImageButton) findViewById(id+7);

        show.setVisibility(View.GONE);
        up.setVisibility(View.GONE);
        down.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);

        Cursor tasksCursor =  ldb.getTasks(id);
        ViewGroup.LayoutParams params = list.getLayoutParams();
        params.height = ((int)(45 * tasksCursor.getCount()) );
        list.setLayoutParams(params);
        taskCursorAdapter cursorAdapter = new taskCursorAdapter(this, tasksCursor, id);
        list.setAdapter(cursorAdapter);

        hide.setVisibility(View.VISIBLE);
        add.setVisibility(View.VISIBLE);
        list.setVisibility(View.VISIBLE);


    }

    public void hide(View v){
        int id = ((int)v.getId() - 7);
        ImageButton hide = (ImageButton) v;
        ImageButton show = (ImageButton) findViewById(id+1);
        ImageButton up = (ImageButton) findViewById(id+2);
        ImageButton down= (ImageButton) findViewById(id+3);
        ImageButton delete = (ImageButton) findViewById(id+4);
        ImageButton add = (ImageButton) findViewById(id+5);
        ListView list = (ListView) findViewById(id+6);
        TextInputEditText taskName = ( TextInputEditText)findViewById(id+8);
        ImageButton remove = (ImageButton) findViewById(id + 9);



        show.setVisibility((View.VISIBLE));
        up.setVisibility(View.VISIBLE);
        down.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);

        hide.setVisibility(View.GONE);
        add.setVisibility(View.GONE);
        list.setVisibility(View.GONE);
        taskName.setVisibility(View.GONE);
        remove.setVisibility(View.GONE);
    }

    public void moveUp(View v){
        int id = ((v.getId())-2);
        this.cursor.moveToFirst();
        int id_secundus = 0;
        while(this.cursor.getInt(0) != id){
            id_secundus= this.cursor.getInt(0);
            this.cursor.moveToNext();
        }

        if(!this.cursor.isFirst()) {
            this.ldb.updateStageId(id, 0);
            this.ldb.updateStageId(id_secundus, id);
            this.ldb.updateStageId(0, id_secundus);

        }
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    public void moveDown(View v){
        int id = ((v.getId())-3);
        this.cursor.moveToFirst();

        while(this.cursor.getInt(0) != id){ this.cursor.moveToNext(); }

        if(!this.cursor.isLast()) {
            this.cursor.moveToNext();
            int id_secundus = this.cursor.getInt(0);
            this.ldb.updateStageId(id, 0);
            this.ldb.updateStageId(id+10, id);
            this.ldb.updateStageId(0, id+10);
        }
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void newTask(View v){
        int id = (int)v.getId()-5;
        TextInputEditText taskName = ( TextInputEditText)findViewById(id+8);
        if(taskName.getVisibility() == View.GONE) {
            taskName.setVisibility(View.VISIBLE);
            ImageButton remove = (ImageButton) findViewById(id + 9);
            remove.setVisibility(View.VISIBLE);
        }
        else{
            String name = taskName.getText().toString();
            if(name.length() == 0){
                Toast.makeText(getApplicationContext(),"Empty name", Toast.LENGTH_SHORT).show();
            }
            else {
                if(chackTName(name, id)) {
                    taskName.setText("");
                    Task task = new Task(this.ldb.getNewTaskID(), name, id);
                    this.ldb.addTask(task);
                    show(findViewById(id + 1));
                }
                else {Toast.makeText(getApplicationContext(),"Duplicate name", Toast.LENGTH_LONG).show();}
            }
        }

    }

    public void remove(View v){
        int id = ((v.getId())-4);

        this.ldb.deleteStage(id);
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void cancelTask(View v){
        int id = (int)v.getId()-9;
        TextInputEditText taskName = ( TextInputEditText)findViewById(id+8);
        taskName.setText("");
        taskName.setVisibility(View.GONE);
        ImageButton remove = (ImageButton)v;
        remove.setVisibility(View.GONE);

    }

    public void moveToNext(View v){
        this.cursor.moveToFirst();
        int string_id = (int)v.getTag(R.string.sec);
        while(this.cursor.getInt(0) != string_id){ this.cursor.moveToNext(); }
        this.cursor.moveToNext();
        this.ldb.moveToNext((int)v.getTag(R.string.prim), this.cursor.getInt(0));
        showSecundus((int)v.getTag(R.string.sec));
    }

    public void moveToPrevious(View v){
        this.cursor.moveToFirst();
        int string_id = (int)v.getTag(R.string.sec);
        while(this.cursor.getInt(0) != string_id){ this.cursor.moveToNext(); }
        this.cursor.moveToPrevious();
        this.ldb.moveToPrev((int)v.getTag(R.string.prim), this.cursor.getInt(0));
        showSecundus((int)v.getTag(R.string.sec));
    }

    public void deleteTask(View v){
        this.ldb.deleteTask((int)v.getTag(R.string.prim));
        showSecundus((int)v.getTag(R.string.sec));

    }

    public boolean chackSName(String name) {
        if (this.cursor.getCount() == 0) return  true;
        name = name.toLowerCase();
            this.cursor.moveToFirst();
            do {
                String aux = (this.cursor.getString(1)).toLowerCase();
                if (aux.equals(name)) return false;
            } while (this.cursor.moveToNext());
        return true;


    }

    public boolean chackTName(String name, int parent) {
        Cursor taskCursor = this.ldb.getTasks(parent);

        if (taskCursor.getCount() == 0) return  true;
        name = name.toLowerCase();
        taskCursor.moveToFirst();
        do {
            String aux=(taskCursor.getString(1)).toLowerCase();
            if( aux.equals( name))return false;
        } while(taskCursor.moveToNext());
        return true;


    }




}

