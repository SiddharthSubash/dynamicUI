/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;



import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 *
 * @author user
 */

@JsonSerializable
public class Employee implements java.io.Serializable {

    @JsonSerializable.JsonElement(name = "Name", type = String.class)
    String name;
    @JsonSerializable.JsonElement(name = "Age", type = Integer.class)
    int age;
    @JsonSerializable.JsonElement(name = "DOB", type = GregorianCalendar.class)
    GregorianCalendar dob;
    @JsonSerializable.JsonElement(name = "EMP ID", type = Integer.class)
    int id;
//
//    public Employee(String name, int age, GregorianCalendar dob, int id) {
//        this.name = name;
//        this.age = age;
//        this.dob = dob;
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public GregorianCalendar getDob() {
        return dob;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setDob(GregorianCalendar dob) {
        System.out.println("dddd" + dob.get(Calendar.YEAR) + dob.get(Calendar.MONTH) + dob.get(Calendar.DATE));
        
        this.dob = dob;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public static void main(String[] args) {
//        //Application.launch(javafx.application.Application.class);
//        //GregorianCalendar cal =  new GregorianCalendar(1996, 10, 23);
//        //Employee e = new Employee("Orochimaru", 15, cal, 1);
//        
//      //Application.launch(JavaFxApplication.class);
//    }
}
