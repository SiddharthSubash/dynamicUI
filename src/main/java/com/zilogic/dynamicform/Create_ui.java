/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;

import static com.zilogic.dynamicform.Main.statusLabel;
import java.lang.reflect.Field;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author user
 */

public class Create_ui {

    public static FormUtil formUtil = new FormUtil();
    public static UiFunctions uiFunctions = new UiFunctions();
    public static UIUtil uiUtil = new UIUtil();
    public static VBox createUiVboxLayout;
    public static Button createButton = new Button("Create");
    public static Button cancelButton = new Button("Cancel");
    public static Button clearAllBtn = new Button("Clear All");
    public static ObservableList<Node> childrens = FXCollections.observableArrayList();
    

    public static GridPane initializeGridPane() {
        GridPane gridPane = uiUtil.createGridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(0, 10, 10, 10));
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
        

        for (Field f: fields) {

            final TextField txt;
            JsonSerializable.JsonElement javaAnnotation = formUtil.checkAnnotationExist(f);
            if (javaAnnotation != null) {
                data_name = javaAnnotation.name();
            } else {
                data_name = f.getName();
            }
            
            Label lbl = uiUtil.createLabel(data_name);
            
            txt = uiUtil.createTextField(val);
            childrens.add(txt);
            Button clearBtn = uiUtil.createButton("Clear");

            inputValue = f.getType().getSimpleName();
            int checkType = uiFunctions.checkFieldDataType(inputValue);

            switch (checkType) {
                case 1:
                    uiFunctions.numberValidate(txt, lbl);
                    break;
                case 2:
                    uiFunctions.stringValidate(txt, lbl);
                    break;
                case 3:
                    uiFunctions.floatValidate(txt, lbl);
                    break;
                case 4:
                    uiFunctions.doubleValidate(txt, lbl);
                    break;
                default:
                    break;
            }

            uiUtil.addToGridPane(gridPane, lbl, column, row);
            if (formUtil.check_calendar_exist(f) == true) {
                DatePicker datePicker = new DatePicker();
                datePicker.getEditor().setDisable(true);
                datePicker.getEditor().setOpacity(1);
                clearBtn.setOnAction(event -> uiFunctions.clearDatePickerValue(datePicker));
                datePicker.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                    System.out.println("lbl" + lbl.getStyle());
                    if (lbl.getStyle().equalsIgnoreCase("-fx-text-fill: red")) {
                        lbl.setStyle("-fx-text-fill: green");
                    } else if (lbl.getStyle().equalsIgnoreCase("-fx-text-fill: green")) {
                        lbl.setStyle("-fx-text-fill: red");
                    }
                });
                childrens.add(datePicker);
                
                //fieldValueHbox.getChildren().addAll(datePicker, clearBtn);
                uiUtil.addToGridPane(gridPane, datePicker, column + 1, row);
                uiUtil.addToGridPane(gridPane, clearBtn, column + 2, row);

            } else {

                //fieldValueHbox.getChildren().addAll(txt, clearBtn);
                clearBtn.setOnAction(event -> uiFunctions.clearDataValue(txt));
                uiUtil.addToGridPane(gridPane, txt, column + 1, row);
                uiUtil.addToGridPane(gridPane, clearBtn, column + 2, row);
            }
            row = row + 1;
        }   
    }

    public static AnchorPane create_ui(Object obj) {
        try {

            AnchorPane anchorPane = new AnchorPane();
            GridPane gridPane = initializeGridPane();

            createUiVboxLayout = uiUtil.createVbox();

            HBox hboxFields = uiUtil.createHbox();
            hboxFields.setPadding(new Insets(0, 12, 15, 12));

            hboxFields.setSpacing(10);
            hboxFields.getChildren().add(gridPane);

            HBox hboxButtons = uiUtil.createHbox();
            hboxButtons.setPadding(new Insets(0, 12, 15, 12));
            hboxButtons.setSpacing(10);
            hboxButtons.setAlignment(Pos.CENTER);

            createButton.setOnAction(ev -> {
                uiFunctions.submitForm(anchorPane, obj, gridPane, statusLabel, "Fields Populated");
            });

            cancelButton.setOnAction(ev -> {
                statusLabel.setText("");
                anchorPane.getChildren().clear();
            });
            
            clearAllBtn.setOnAction(ev -> {
                uiFunctions.clearAllFields(childrens);

            });

            performOperation(obj, gridPane);



            hboxButtons.getChildren().addAll(cancelButton, createButton, clearAllBtn);
            createUiVboxLayout.getChildren().addAll(hboxFields, hboxButtons);
            anchorPane.getChildren().add(createUiVboxLayout);

            return anchorPane;

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}