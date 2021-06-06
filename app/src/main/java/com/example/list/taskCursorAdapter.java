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
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class taskCursorAdapter extends CursorAdapter {
    int stage_id;

    public taskCursorAdapter(Context context, Cursor cursor, int stage_id) {
        super(context, cursor, 0);
        this.stage_id = stage_id;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.taskslist, parent, false);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor){
        TextView task = (TextView) view.findViewById(R.id.taskItem);

        if(task != null){
            int id = cursor.getInt(0);
            task.setText(cursor.getString(1));

            ImageButton prev = (ImageButton) view.findViewById(R.id.Previous);
            ImageButton next = (ImageButton) view.findViewById(R.id.Next);
            ImageButton delete = (ImageButton) view.findViewById(R.id.delete);
            task.setTag(R.string.prim,id);
            prev.setTag(R.string.prim,id);
            next.setTag(R.string.prim,id);
            delete.setTag(R.string.prim, id);
            task.setTag(R.string.sec,stage_id);
            prev.setTag(R.string.sec,stage_id);
            next.setTag(R.string.sec,stage_id);
            delete.setTag(R.string.sec, stage_id);
        }


    }



}
