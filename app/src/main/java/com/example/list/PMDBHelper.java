package com.example.list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

public class PMDBHelper extends SQLiteOpenHelper {

    public static final String DB = "PMDB.db";

    public static final String PT = "projecs";
    public static final String PC_name = "name";

    //public static final String PC_first_stage = "first_stage";
    //public static final String PC_last_stage = "last_stage";

    public static final String ST = "stages";
    public static final String SC_id = "_id";
    public static final String SC_name = "name";
    public static final String SC_parent = "parent";
    //public static final String SC_prev_id = "prev_id";
    //public static final String SC_next_id = "next_id";

    public static final String TT = "tasks";
    public static final String TC_id = "_id";
    public static final String TC_name = "name";
    public static final String TC_parent_id = "parent_id";

    public PMDBHelper(Context context) {
        super(context, DB , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }

        //project table
        db.execSQL("CREATE TABLE " + PT + "(" + PC_name + " text primary key" +  ")");

        //stage table
        db.execSQL("CREATE TABLE " + ST + "(" + SC_id + " integer primary key," +
                SC_name + " text, " + SC_parent + " text, " +
                "FOREIGN KEY(" + SC_parent + ") REFERENCES " + PT + "(" + PC_name + ") ON DELETE CASCADE" +
                ")");

        //task table
        db.execSQL("CREATE TABLE " + TT + "(" + TC_id + " integer primary key, " +
                TC_name + " text, " + TC_parent_id + " integer, " +
                "FOREIGN KEY(" + TC_parent_id + ") REFERENCES " + ST + "(" + SC_id + ") ON DELETE CASCADE" +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        onCreate(db);
    }

    public void addProject(Project project){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PC_name,project.getName());
        //contentValues.putNull(PC_first_stage);

        db.insert(PT, null, contentValues);
        db.close();

    }

    public Cursor getProjects(String name){
        if(name == ""){
            SQLiteDatabase ldb = this.getReadableDatabase();
            Cursor p = ldb.rawQuery("select rowid _id,* from " + PT + "", null);
            //ldb.close();
            return p;
        }
        else {
            SQLiteDatabase ldb = this.getReadableDatabase();
            Cursor p = ldb.rawQuery("select * from " + PT +" where " + PC_name + "=" + "'" + name + "'", null);

            return p;
        }
    }

    public void deleteProject(Project project){
        SQLiteDatabase ldb = this.getReadableDatabase();
        Cursor cursor = ldb.rawQuery("select * from " + PT +" where " + PC_name + "=" + "'" + project.getName() + "'", null);
        Cursor cursor1 = ldb.rawQuery("select * from " + ST +" where " + SC_parent + "=" + "'" + project.getName() + "'", null);
        ldb.delete(PT, PC_name + "=?", new String[] {String.valueOf(project.getName())});
        while(cursor1.moveToNext()){ this.deleteStage(cursor1.getInt(0)); }
        cursor.close();
        cursor1.close();

    }

    public void updateProject(Project project){}

    public void addStage(Stage stage){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(SC_id, stage.getId());
        contentValues.put(SC_name, stage.getName());
        contentValues.put(SC_parent, stage.getParent().getName());

        db.insert(ST, null, contentValues);

            db.close();

    }

    public void deleteStage(int id){
        SQLiteDatabase ldb = this.getReadableDatabase();
        Cursor cursor = ldb.rawQuery("select * from " + TT +" where " + TC_parent_id + "=" + "'" + id + "'", null);
        while(cursor.moveToNext()){ this.deleteTask(cursor.getInt(0)); }
        ldb.delete(ST, SC_id + "=?", new String[] {String.valueOf(id)});
    }

    public Cursor getStages(String parent){
        SQLiteDatabase ldb = this.getReadableDatabase();
        Cursor p = ldb.rawQuery("select * from " + ST +" where " + SC_parent + "=" + "'" + parent + "'" + " order by " + SC_id, null);
        return p;
    }

    public void updateStageId(int stage_id, int newid){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(SC_id, newid);
        db.update(ST, args, SC_id + "=" + stage_id, null);
    }

    public void addTask(Task task){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TC_id, task.getId());
        contentValues.put(TC_name, task.getName());
        contentValues.put(TC_parent_id, task.getParent_id());

        db.insert(TT, null, contentValues);

        db.close();

    }

    public Cursor getTasks(int parent_id){
        SQLiteDatabase ldb = this.getReadableDatabase();
        Cursor p = ldb.rawQuery("select * from " + TT +" where " +  TC_parent_id + "=" + "" + parent_id + "" + " order by " + TC_id
                , null);
        return p;
    }

    public void deleteTask(int id){
        SQLiteDatabase ldb = this.getReadableDatabase();
        ldb.delete(TT, TC_id + "=?", new String[] {String.valueOf(id)});
    }

    public void moveToNext(int task_id, int stage_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TC_parent_id, stage_id);
        db.update(TT, args, TC_id + "=" + task_id, null);
    }

    public void moveToPrev(int task_id, int stage_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(TC_parent_id, stage_id);
        db.update(TT, args, TC_id + "=" + task_id, null);

    }

    public int getNewStageID(){
        SQLiteDatabase ldb = this.getReadableDatabase();
        Cursor cursor = ldb.rawQuery("select max(" + SC_id + ") from " + ST + "", null);
        cursor.moveToLast();
        return cursor.getInt(0) + 10;
    }

    public int getNewTaskID(){
        SQLiteDatabase ldb = this.getReadableDatabase();
        Cursor cursor = ldb.rawQuery("select rowid _id from " + TT + "", null);
        int id = 1;
        while (cursor.moveToNext()){
            if (id != cursor.getInt(0)) break;
            else id = id + 1;
        }
        return id;
    }

}
