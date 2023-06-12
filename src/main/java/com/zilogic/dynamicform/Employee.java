/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;

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
       
        this.dob = dob;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public static void main(String[] args) throws CloneNotSupportedException {
//        Employee e = new Employee();
//        e.setId(100);
//        e.setAge(5);
//        e.setName("Orochimaru");
//        GregorianCalendar cal =  new GregorianCalendar(1996, 10, 23);
//        e.setDob(cal);
//        System.out.println("asfasfasasfa");
//        //App.Main(args);
//        try {
//            Object obj = e;
//
//            System.out.println("objeeeeeectttt" + e);
//            Object[] ob = new Object[1];
//            ob[0] = obj;
//            System.out.println("objeeeeeect" + ob[0]);
//            //Object[] ag = ob;
//            //ag[0] = e;
//            //String[] str = ob(new String[ob.length()]);
//            System.out.println("ggggggggg");
//            App.launch(Main.class);
//            //App.launch(Main.class);
//            //Application.launch(javafx.application.Application.class);
//            //GregorianCalendar cal =  new GregorianCalendar(1996, 10, 23);
//            //Employee e = new Employee("Orochimaru", 15, cal, 1);
//
//          //Application.launch(JavaFxApplication.class);
//      } catch (Exception event) {
//            System.out.println("e" + event);
//        }
//    }
}