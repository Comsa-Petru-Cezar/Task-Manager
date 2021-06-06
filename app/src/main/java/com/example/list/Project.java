package com.example.list;

import android.content.Context;

import java.io.Serializable;

public class Project implements Serializable {
    int id;
    String name;

   // int first_stage_id;
   // int last_stage_id;

    public Project(String name){

        //this.id = id;
        this.name = name;
       // this.first_stage_id = 0;
        //this.last_stage_id = 0;

    }

    public String getName(){ return this.name; }

    public void setName(String name){ this.name = name;}

    public int getId(){ return this.id; }

    public void setId(int id){ this.id = id;}

   // public int getFirst(){return  this.first_stage_id;}

   // public int getLast(){return this.last_stage_id;}

   // public void setFirst(int first){ this.first_stage_id = first; }

    //public void setLast(int last){this.last_stage_id = last;}


}
