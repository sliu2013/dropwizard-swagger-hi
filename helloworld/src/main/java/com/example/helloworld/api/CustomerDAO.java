package com.example.helloworld.api;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {
    private List<Customer> allCustomers = new ArrayList<>();

    public List<Customer> getCustomers(String searchString, int limit){
        limit = limit > this.allCustomers.size() ? this.allCustomers.size() : limit;

        return this.allCustomers.subList(0, limit);
    }


    public Customer createCustomer(String customerName){
        int newId = this.allCustomers.size();
        Customer newCustomer = new Customer(customerName, newId);
        this.allCustomers.add(newCustomer);

        return newCustomer;
    }
}
