package com.example.list;

public class Stage {
    int id;
    String name;
    Project parent;
    int prev_id;
    int next_id;

    public Stage(int id, String name, Project parent){
        this.id = id;
        this.name = name;
        this.parent = parent;
        //this.prev_id = prev_id;
        //this.next_id = next_id;

    }



    //public void setNext_id(int next_id){ this.next_id = next_id; }

    //public void setPrev_id(int prev_id){this.prev_id = prev_id;}


    //public int getNext_id(){ return this.next_id; }
    //public int getPrev_id(){ return this.prev_id; }

    public Project getParent() { return this.parent; }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }



}
