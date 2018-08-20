package com.example.helloworld.api;


public class Customer {
    private String name;
    private int id;

    public Customer(String name, int id){
        this.name = name;
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public String getName() {return this.name;}
}
