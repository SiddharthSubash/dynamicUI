/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;

import static com.zilogic.dynamicform.Main.statusLabel;
import java.lang.reflect.Field;
import java.util.GregorianCalendar;
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

public class Update_ui {

    public static FormUtil formUtil= new FormUtil();
    public static UiFunctions uiFunctions = new UiFunctions();
    public static UIUtil uiUtil = new UIUtil();
    public static Button updateButton = new Button("Update");
    public static Button cancelButton = new Button("Cancel");
    public static Button clearAllBtn = new Button("Clear All");
    public static VBox UpdateUiVboxLayout;
    public static ObservableList<Node> childrens = FXCollections.observableArrayList();;

    public static GridPane initializeGridPane() {
        GridPane gridPane = uiUtil.createGridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(0, 10, 10, 10));
        return gridPane;
    }

    public static void performOperation(Object obj, GridPane gridPane) {
        try {
            Class cls = obj.getClass();
            Field[] fields = cls.getDeclaredFields();
            int row = 0;
            int column = 0;

            String data_name;
            Object val;
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
                Button clearBtn = uiUtil.createButton("Clear");

                if (formUtil.validate_data_values(f, obj) == false) {
                    lbl.setStyle("-fx-text-fill: red");
                    val = "";
                } else {
                    val = f.get(obj);
                }

                txt = uiUtil.createTextField(val);
                childrens.add(txt);
                txt.setPromptText("Enter " + lbl.getText());

                inputValue = f.getType().getSimpleName();
                int checkType = uiFunctions.checkFieldDataType(inputValue);

                if (checkType == 1) {
                    uiFunctions.numberValidate(txt, lbl);
                } else if (checkType == 2) {
                    uiFunctions.stringValidate(txt, lbl);
                } else if (checkType == 3) {
                    uiFunctions.floatValidate(txt, lbl);
                } else if (checkType == 4) {
                    uiFunctions.doubleValidate(txt, lbl);
                }

                uiUtil.addToGridPane(gridPane, lbl, column, row);

                if (formUtil.check_calendar_exist(f) == true) {
                    DatePicker datePicker = new DatePicker();
                    datePicker.getEditor().setDisable(true);
                    datePicker.getEditor().setOpacity(1);
                    childrens.add(datePicker);
                    
                    GregorianCalendar cal = (GregorianCalendar)f.get(obj);
                    if (uiFunctions.populate_calendar_values(cal, datePicker) != true) {

                        lbl.setStyle("-fx-text-fill: red");
                        datePicker.setPromptText("Enter " + lbl.getText());
                        //datePicker.getParent().requestFocus();
                    }

                    clearBtn.setOnAction(event -> uiFunctions.clearDatePickerValue(datePicker));
                    
                    uiUtil.addToGridPane(gridPane, datePicker, column + 1, row);
                    uiUtil.addToGridPane(gridPane, clearBtn, column + 2, row);
                    datePicker.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {

                        if (datePicker.getValue() == null) {
                            if (lbl.getStyle().equalsIgnoreCase("-fx-text-fill: green")) {
                                lbl.setStyle("-fx-text-fill: red");
                            }
                        } else if (lbl.getStyle().equalsIgnoreCase("-fx-text-fill: red")) {
                            lbl.setStyle("-fx-text-fill: green");
                        }
                    });
                } else {

                    clearBtn.setOnAction(event -> uiFunctions.clearDataValue(txt));
                    
                    uiUtil.addToGridPane(gridPane, txt, column + 1, row);
                    uiUtil.addToGridPane(gridPane, clearBtn, column + 2, row);
                }
                
                row = row + 1;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static AnchorPane update_ui(Object obj) {
        try {

            GridPane gridPane = initializeGridPane();
            UpdateUiVboxLayout = uiUtil.createVbox();
            AnchorPane anchorPane = new AnchorPane();
            HBox hboxFields = uiUtil.createHbox();
            hboxFields.setPadding(new Insets(0, 12, 15, 12));
            hboxFields.setSpacing(10);
            hboxFields.getChildren().add(gridPane);

            HBox hboxButtons = uiUtil.createHbox();
            hboxButtons.setPadding(new Insets(0, 12, 15, 12));
            hboxButtons.setSpacing(10);
            hboxButtons.setAlignment(Pos.CENTER);

            
            updateButton.setOnAction(ev -> {
                uiFunctions.submitForm(anchorPane, obj, gridPane, statusLabel, "Fields Updated");
            });

            
            cancelButton.setOnAction(ev -> {
                statusLabel.setText("");
                anchorPane.getChildren().clear();
            });

            clearAllBtn.setOnAction(ev -> {
                uiFunctions.clearAllFields(childrens);

            });

            performOperation(obj, gridPane);
            hboxButtons.getChildren().addAll(cancelButton, updateButton, clearAllBtn);
            UpdateUiVboxLayout.getChildren().addAll(hboxFields, hboxButtons);
            anchorPane.getChildren().add(UpdateUiVboxLayout);
            

            return anchorPane;

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}