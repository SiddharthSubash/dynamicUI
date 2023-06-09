/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;

import java.lang.reflect.Field;

/**
 *
 * @author user
 */
public class FormUtil {

    public static Boolean check_calendar_exist(Field f) {
        try {
            return f.getType().getSimpleName().equalsIgnoreCase("GregorianCalendar");
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean validate_data_values(Field f, Object obj) {
        try {
            if (f.get(obj) == "") {
                return false;
            } else if (f.get(obj).equals(0)) {
                return false;
            } else if (f.get(obj) != null || f.get(obj) == "") {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    
    public static JsonSerializable.JsonElement checkAnnotationExist(Field f) {
        JsonSerializable.JsonElement javaAnnotation = f.getAnnotation(JsonSerializable.JsonElement.class);
        return javaAnnotation;
    }

    public static void display_type_fields(Employee emp) {
        Class obj = emp.getClass();
        Field[] fields = obj.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("value and type" + field.getName() + field.getType());
        }
    }
}