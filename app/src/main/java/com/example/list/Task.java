package com.example.list;

public class Task {
    int id;
    String name;
    int parent_id;

    public Task( int id, String name, int parent_id){
        this.id = id;
        this.name = name;
        this.parent_id = parent_id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent) {
        this.parent_id = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId(){return id;}
}
