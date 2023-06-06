/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;

import static com.zilogic.dynamicform.main.mainStage;
import static com.zilogic.dynamicform.main.statusLabel;
import java.lang.reflect.Field;
import java.util.GregorianCalendar;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class Display_ui {

    public static Validate_util validate_util= new Validate_util();
    public static Validate_ui_functions validate_ui_functions = new Validate_ui_functions();

    public static void closeWindow(Stage stage) {
        statusLabel.setText("");
        stage.close();
    }
    public static int getNumberOfFields(Object obj) {
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();

        return fields.length;
    }
    
    public static Stage initializeStage() {
        final Stage stage = UIUtil.createStage();
        stage.initOwner(mainStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setOnCloseRequest( ev -> {
            statusLabel.setText("");
            closeWindow(stage);
        });
        return stage;
    }

    public static GridPane initializeGridPane() {
        GridPane gridPane = UIUtil.createGridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        return gridPane;
    }
    public static void display_ui(Employee emp) {
        try {
            final Stage stage = UIUtil.createStage();
            stage.initOwner(mainStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setOnCloseRequest(ev -> {
                statusLabel.setText("");
                stage.close();
            });
            Class obj = emp.getClass();
            GridPane gridPane = UIUtil.createGridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            Field[] fields = obj.getDeclaredFields();
            
            Button closeButton = new Button("Close");
            closeButton.setOnAction(ev -> {
                closeWindow(stage);
            });

            int row = 0;
            int column = 0;

            String data_name;
            Class<?> cls;

            for (Field f: fields) {

                JsonSerializable.JsonElement javaAnnotation = validate_util.checkAnnotationExist(f);
                if (javaAnnotation != null) {

                    data_name = javaAnnotation.name();
                    cls = javaAnnotation.type();
                } else {
                    data_name = f.getName();
                    cls = f.getType();
                }
                Label lbl = UIUtil.createLabel(data_name);
                Object val;

                if (validate_util.validate_data_values(f, emp) == false) {
                    lbl.setStyle("-fx-text-fill: red");
                    val = "";
                } else {
                    val = f.get(emp);
                }

                if (validate_util.validateDataValueType(val, cls) == false) {
                    lbl.setStyle("-fx-text-fill: red");
                }
                TextField txt = UIUtil.createTextField(val);
                gridPane = UIUtil.addToGridPane(gridPane, lbl, column, row);

                if (validate_util.check_calendar_exist(f) == true) {
                    DatePicker datePicker = new DatePicker();
                    datePicker.setDisable(true);
                    GregorianCalendar cal = (GregorianCalendar)f.get(emp);
                    if (validate_ui_functions.populate_calendar_values(cal, datePicker) != true) {

                        lbl.setStyle("-fx-text-fill: red");
                    }
                    gridPane = UIUtil.addToGridPane(gridPane, datePicker, column + 1, row);

                } else {
                    txt.setDisable(true);
                    gridPane = UIUtil.addToGridPane(gridPane, txt, column + 1, row);
                }
                row = row + 1;
            }
            gridPane = UIUtil.addToGridPane(gridPane, closeButton, 1, row);

            Scene scene = UIUtil.createScene(gridPane, 450, 200);
            UIUtil.addSceneToStage(stage, scene);
            stage.show();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}