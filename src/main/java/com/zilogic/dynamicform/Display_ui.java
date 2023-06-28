/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;

import static com.zilogic.dynamicform.Main.statusLabel;
import java.lang.reflect.Field;
import java.util.GregorianCalendar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author user
 */
public class Display_ui {

    public static FormUtil formUtil = new FormUtil();
    public static UiFunctions uiFunctions = new UiFunctions();
    public static UIUtil uiUtil = new UIUtil();
    public static Button closeButton = new Button("Close");
    public static VBox displayUiVboxLayout;

    public static GridPane initializeGridPane() {
        GridPane gridPane = uiUtil.createGridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(0, 10, 10, 10));

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
            Label fieldNameLbl;
            Label fieldValueLbl;

            for (Field f: fields) {

                JsonSerializable.JsonElement javaAnnotation = formUtil.checkAnnotationExist(f);
                if (javaAnnotation != null) {

                    data_name = javaAnnotation.name();
                    object_type = javaAnnotation.type().getTypeName();
                } else {
                    data_name = f.getName();
                    object_type = f.getType().getTypeName();
                }
                fieldNameLbl = uiUtil.createLabel(data_name);
                Object val;

                if (formUtil.validate_data_values(f, obj) == false) {
                    fieldNameLbl.setStyle("-fx-text-fill: red");
                    
                    val = "";
                    fieldValueLbl = uiUtil.createLabel("N/A");
                    fieldValueLbl.setStyle("-fx-text-fill: red");

                } else {
                    val = f.get(obj);
                    fieldValueLbl = uiUtil.createLabel(f.get(obj).toString());
                }

                uiUtil.addToGridPane(gridPane, fieldNameLbl, column, row);

                if (formUtil.check_calendar_exist(f) == true) {
                    Label dateValueLbl = uiUtil.createLabel("");
                    DatePicker datePicker = new DatePicker();
                    datePicker.setDisable(true);
                    GregorianCalendar cal = (GregorianCalendar)f.get(obj);
                    if (uiFunctions.populate_calendar_values(cal, datePicker) != true) {

                        fieldNameLbl.setStyle("-fx-text-fill: red");
                        dateValueLbl.setText("N/A");
                        dateValueLbl.setStyle("-fx-text-fill: red");
                    } else {
                        dateValueLbl.setText(datePicker.getValue().toString());
                    }
                    gridPane = uiUtil.addToGridPane(gridPane, dateValueLbl, column + 1, row);

                } else {
                    gridPane = uiUtil.addToGridPane(gridPane, fieldValueLbl, column + 1, row);
                }
                row = row + 1;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static AnchorPane display_ui(Object obj) {
        try {

            GridPane gridPane = initializeGridPane();
            displayUiVboxLayout = uiUtil.createVbox();
            AnchorPane anchorPane = new AnchorPane();

            HBox hboxFields = uiUtil.createHbox();
            hboxFields.setPadding(new Insets(0, 12, 15, 12));
            hboxFields.setSpacing(10);
            hboxFields.getChildren().add(gridPane);

            HBox hboxButtons = uiUtil.createHbox();
            hboxButtons.setPadding(new Insets(0, 12, 15, 12));
            hboxButtons.setSpacing(10);
            hboxButtons.setAlignment(Pos.CENTER);

            
            closeButton.setOnAction(ev -> {
                statusLabel.setText("");
                anchorPane.getChildren().clear();
            });

            performOperation(obj, gridPane);
            hboxButtons.getChildren().addAll(closeButton);
            displayUiVboxLayout.getChildren().addAll(hboxFields, hboxButtons);
            anchorPane.getChildren().add(displayUiVboxLayout);

            return anchorPane;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}