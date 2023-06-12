/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;

import static com.zilogic.dynamicform.Main.mainStage;
import static com.zilogic.dynamicform.Main.statusLabel;
import java.lang.reflect.Field;
import java.util.GregorianCalendar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class Display_ui {

    public static FormUtil formUtil = new FormUtil();
    public static UiFunctions validate_ui_functions = new UiFunctions();
    public static UIUtil uiUtil = new UIUtil();


    public static Stage initializeStage(String titleText) {
        final Stage stage = uiUtil.createStage();
        stage.setTitle(titleText);
        stage.initOwner(mainStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setOnCloseRequest( ev -> {
            statusLabel.setText("");
            uiUtil.closeWindow(stage);
        });

        return stage;
    }

    public static GridPane initializeGridPane() {
        GridPane gridPane = uiUtil.createGridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 10, 10, 10));

        return gridPane;
    }

    public static void performOperation(Object obj, GridPane gridPane) {
        try {
            int row = 0;
            int column = 0;

            Class cls = obj.getClass();
            Field[] fields = cls.getDeclaredFields();
            String data_name;
            String object_type;
            TextField txt;
            Label lbl;

            for (Field f: fields) {

                JsonSerializable.JsonElement javaAnnotation = formUtil.checkAnnotationExist(f);
                if (javaAnnotation != null) {

                    data_name = javaAnnotation.name();
                    object_type = javaAnnotation.type().getTypeName();
                } else {
                    data_name = f.getName();
                    object_type = f.getType().getTypeName();
                }
                lbl = uiUtil.createLabel(data_name);
                Object val;

                if (formUtil.validate_data_values(f, obj) == false) {
                    lbl.setStyle("-fx-text-fill: red");
                    val = "";
                } else {
                    val = f.get(obj);
                }

                txt = uiUtil.createTextField(val);
                uiUtil.addToGridPane(gridPane, lbl, column, row);

                if (formUtil.check_calendar_exist(f) == true) {
                    DatePicker datePicker = new DatePicker();
                    datePicker.setDisable(true);
                    GregorianCalendar cal = (GregorianCalendar)f.get(obj);
                    if (validate_ui_functions.populate_calendar_values(cal, datePicker) != true) {

                        lbl.setStyle("-fx-text-fill: red");
                    }
                    gridPane = uiUtil.addToGridPane(gridPane, datePicker, column + 1, row);

                } else {
                    txt.setDisable(true);
                    gridPane = uiUtil.addToGridPane(gridPane, txt, column + 1, row);
                }
                row = row + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void display_ui(Object obj) {
        try {
            String stageTitleText = "Display Form";
            Stage stage = initializeStage(stageTitleText);
            GridPane gridPane = initializeGridPane();
            VBox vboxMainLayout = uiUtil.createVbox();

            HBox hboxFields = uiUtil.createHbox();
            hboxFields.setPadding(new Insets(15, 12, 15, 12));
            hboxFields.setSpacing(10);
            hboxFields.getChildren().add(gridPane);

            HBox hboxButtons = uiUtil.createHbox();
            hboxButtons.setPadding(new Insets(15, 12, 15, 12));
            hboxButtons.setSpacing(10);
            hboxButtons.setAlignment(Pos.CENTER);

            Button closeButton = new Button("Close");
            closeButton.setOnAction(ev -> {
                statusLabel.setText("");
                uiUtil.closeWindow(stage);
            });

            performOperation(obj, gridPane);
            hboxButtons.getChildren().addAll(closeButton);
            vboxMainLayout.getChildren().addAll(hboxFields, hboxButtons);

            Scene scene = uiUtil.createScene(vboxMainLayout, 385, 250);
            uiUtil.addSceneToStage(stage, scene);
            stage.show();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}