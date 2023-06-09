/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;

import static com.zilogic.dynamicform.main.mainStage;
import static com.zilogic.dynamicform.main.statusLabel;
import java.lang.reflect.Field;
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

public class Create_ui {

    public static FormUtil formUtil = new FormUtil();
    public static UiFunctions uiFunctions = new UiFunctions();
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
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        int row = 0;
        int column = 0;
        Object val = "";
        String data_name;
        String inputValue = "";

        TextField txt;

        for (Field f: fields) {
            JsonSerializable.JsonElement javaAnnotation = formUtil.checkAnnotationExist(f);
            if (javaAnnotation != null) {
                data_name = javaAnnotation.name();
            } else {
                data_name = f.getName();
            }
            Label lbl = uiUtil.createLabel(data_name);

            txt = uiUtil.createTextField(val);

            inputValue = f.getType().getSimpleName();

            if (inputValue.equalsIgnoreCase("Int")) {
                txt = uiFunctions.numberValidate(txt, lbl);
            } else if (inputValue.equalsIgnoreCase("String")) {
                txt = uiFunctions.stringValidate(txt, lbl);
            }

            uiUtil.addToGridPane(gridPane, lbl, column, row);
            if (formUtil.check_calendar_exist(f) == true) {
                DatePicker datePicker = new DatePicker();
                datePicker.getEditor().setDisable(true);
                datePicker.getEditor().setOpacity(1);
                datePicker.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                    if (lbl.getStyle().equalsIgnoreCase("-fx-text-fill: red")) {
                        lbl.setStyle("-fx-text-fill: green");
                    }
                });

                uiUtil.addToGridPane(gridPane, datePicker, column + 1, row);

            } else {
                uiUtil.addToGridPane(gridPane, txt, column + 1, row);
            }
            row = row + 1;
        }   
    }

    public static void create_ui(Object obj) {
        try {
            String stageTitleText = "Create Form";
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

            Button createButton = new Button("Create");
            createButton.setOnAction(ev -> {
                uiFunctions.submitForm(stage, obj, gridPane, statusLabel, "Fields Populated");
            });

            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(ev -> {
                statusLabel.setText("");
                uiUtil.closeWindow(stage);
            });

            performOperation(obj, gridPane);

            hboxButtons.getChildren().addAll(cancelButton, createButton);
            vboxMainLayout.getChildren().addAll(hboxFields, hboxButtons);

            Scene scene = uiUtil.createScene(vboxMainLayout, 385, 250);
            uiUtil.addSceneToStage(stage, scene);
            stage.show();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}