/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;

import javafx.application.Application; 

import java.util.GregorianCalendar;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */

public class main extends Application{
    
    public static Button newBtn = new Button();
    public static Button updateBtn = new Button();
    public static Button displayBtn = new Button();
    public static Label statusLabel = new Label();
    public static Stage mainStage;

    @Override
    public void start(Stage stage) {
        try {
            mainStage = stage;

            Employee e = new Employee();

            GregorianCalendar cal =  new GregorianCalendar(1996, 10, 23);

            e.setId(78);
            e.setAge(5);
            //e.setName("Orochimaru");
            //e.setDob(cal);
            
            GridPane gridPane = UIUtil.createGridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            
            
            newBtn.setText("Create Fields");
            newBtn.setId("Create Button");
            
            newBtn.setOnAction(event -> {
                Create_ui.create_ui(e);
            });

            updateBtn.setText("Update Fields");
            updateBtn.setOnAction(event -> {
                Update_ui.update_ui(e);
            });

            displayBtn.setText("Display Fields");
            displayBtn.setOnAction(event -> {
                Display_ui.display_ui(e);
            });

            UIUtil.addToGridPane(gridPane, newBtn, 0, 0);
            UIUtil.addToGridPane(gridPane, updateBtn, 1, 0);
            UIUtil.addToGridPane(gridPane, displayBtn, 2, 0);
            UIUtil.addToGridPane(gridPane, statusLabel, 1, 4);

            Scene scene = UIUtil.createScene(gridPane, 400, 200);
            stage = UIUtil.addSceneToStage(stage, scene);
            stage.show();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static Object passObject(Object obj1) {
        Object obj = obj1;
        return obj;
    }

    public static void main(String[] args) {
        try {
            launch(args); 
            //App.main(args);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}