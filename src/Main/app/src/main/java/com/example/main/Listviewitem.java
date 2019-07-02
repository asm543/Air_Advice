package com.example.main;
public class Listviewitem {
    private String name,x,y;

    public String getName(){return name;}
    public String getlat(){return x;}
    public String getlon(){return y;}

    public Listviewitem(String name,String x,String y){
        this.name=name;
        this.x=x;
        this.y=y;
    }
}