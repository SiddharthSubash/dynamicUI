/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;

import java.awt.event.WindowListener;
import javafx.application.Application; 
import java.util.GregorianCalendar;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author user
 */

public class Main extends Application {

    public static Button newBtn = new Button();
    public static Button updateBtn = new Button();
    public static Button displayBtn = new Button();
    public static Label statusLabel = new Label();
    public static Stage mainStage;
    public static UIUtil uiUtil = new UIUtil();
    public AnchorPane createAnchorPane;
    public AnchorPane updateAnchorPane;
    public AnchorPane displayAnchorPane;

    @Override
    public void start(Stage stage) {
        try {
            mainStage = stage;
//            mainStage.setMinWidth(Double.MIN_VALUE);
//            mainStage.setHeight(Double.MIN_VALUE);
//            mainStage.setMaxHeight(Double.MAX_VALUE);
//            mainStage.setMaxWidth(Double.MAX_VALUE);
            mainStage.sizeToScene();
            String stageTitleText = "Form";
            mainStage.setTitle(stageTitleText);
            VBox mainVbox = uiUtil.createVbox();
            mainVbox.setMaxHeight(Double.MAX_VALUE);

            mainVbox.setPadding(new Insets(15, 12, 15, 12));
            mainVbox.setSpacing(10);

            Employee e = new Employee();

            GregorianCalendar cal =  new GregorianCalendar(1996, 10, 23);

            //=e.setId(78);
            //e.setAge(5);
            //e.setSalary(235.23523);
            //e.setName("Orochimaru");
            //e.setDob(cal);

            GridPane gridPane = uiUtil.createGridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            //gridPane.setPadding(new Insets(10, 10, 10, 10));
            gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            gridPane.setMinSize(Double.MIN_VALUE, Double.MIN_VALUE);
            newBtn.setText("Create Fields");
            newBtn.setId("Create Button");

            newBtn.setOnAction(event -> {
                gridPane.getChildren().remove(createAnchorPane);
                statusLabel.setText("");
                createAnchorPane = Create_ui.create_ui(e);
                //createAnchorPane.setPadding(new Insets(50, 50, 50, 50));

                gridPane.getChildren().remove(updateAnchorPane);
                gridPane.getChildren().remove(displayAnchorPane);
                gridPane.add(createAnchorPane, 1, 0, 1, 6);
            });

            updateBtn.setText("Update Fields");
            updateBtn.setOnAction(event -> {
                gridPane.getChildren().remove(updateAnchorPane);
                statusLabel.setText("");
                updateAnchorPane = Update_ui.update_ui(e);
                gridPane.getChildren().remove(createAnchorPane);
                gridPane.getChildren().remove(displayAnchorPane);
                gridPane.add(updateAnchorPane, 1, 0, 1, 6);
                
            });

            displayBtn.setText("Display Fields");
            displayBtn.setOnAction(event -> {
                gridPane.getChildren().remove(displayAnchorPane);
                statusLabel.setText("");
                displayAnchorPane = Display_ui.display_ui(e);
                gridPane.getChildren().remove(createAnchorPane);
                gridPane.getChildren().remove(updateAnchorPane);
                gridPane.add(displayAnchorPane, 1, 0, 1, 6);
            });

            uiUtil.addToGridPane(gridPane, newBtn, 0, 0);
            uiUtil.addToGridPane(gridPane, updateBtn, 0, 1);
            uiUtil.addToGridPane(gridPane, displayBtn, 0, 2);
            uiUtil.addToGridPane(gridPane, statusLabel, 0, 3);

            mainVbox.getChildren().addAll(gridPane);
            
            Scene scene = uiUtil.createScene(mainVbox, 520, 300);

            stage = uiUtil.addSceneToStage(stage, scene);
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
            //App.Main(args);
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}