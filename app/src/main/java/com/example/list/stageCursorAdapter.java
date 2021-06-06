package com.example.list;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.material.textfield.TextInputEditText;

public class stageCursorAdapter extends CursorAdapter {

    public stageCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.stageslist, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){

        Button stageButton = (Button) view.findViewById(R.id.stageItem);
        if (stageButton != null) {

            ImageButton showButton = ( ImageButton) view.findViewById(R.id.showTasks);
            ImageButton upButton = ( ImageButton) view.findViewById(R.id.moveUp);
            ImageButton downButton = ( ImageButton) view.findViewById(R.id.moveDown);
            ImageButton deleteButton = ( ImageButton) view.findViewById(R.id.deleteStage);
            ImageButton addTask = (ImageButton) view.findViewById(R.id.addTask);
            ImageButton hideTasks = (ImageButton) view.findViewById(R.id.hideTasks);
            TextInputEditText taskName = (TextInputEditText) view.findViewById(R.id.taskName);
            ListView listView = (ListView) view.findViewById(R.id.tasks_list);
            ImageButton cancelTask = (ImageButton) view.findViewById(R.id.cancelTask);




            stageButton.setText(cursor.getString(1));
            int id = cursor.getInt(0);

            stageButton.setId(id);
            showButton.setId(id+1);
            upButton.setId(id+2);
            downButton.setId(id+3);
            deleteButton.setId(id+4);
            addTask.setId(id+5);
            listView.setId(id+6);
            hideTasks.setId(id+7);
            taskName.setId(id+8);
            cancelTask.setId(id+9);
        }

    }



}
