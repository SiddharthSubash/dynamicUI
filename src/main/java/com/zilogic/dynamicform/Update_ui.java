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
import java.util.function.UnaryOperator;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class Update_ui {
    
    public static Validate_util validate_util= new Validate_util();
    public static Validate_ui_functions validate_ui_functions = new Validate_ui_functions();
    
public static void submit(Stage stage, Employee obj, GridPane gridPane) {
        Boolean checkStatus = validate_ui_functions.validate_ui(gridPane, obj);
        if (checkStatus == true) {
            statusLabel.setText("Fields Updated");
            statusLabel.setStyle("-fx-text-fill: green");
            stage.close();
        }
    }
    public static void cancelOperation(Stage stage) {
        statusLabel.setText("");
        stage.close();
    }
    public static void update_ui(Employee emp) {
        try {
            final Stage stage = UIUtil.createStage();
            stage.initOwner(mainStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setOnCloseRequest(ev -> {
                statusLabel.setText("");
                stage.close();
            });
            Class obj = emp.getClass();
            final GridPane gridPane = UIUtil.createGridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(10, 10, 10, 10));
            Field[] fields = obj.getDeclaredFields();

            Button updateButton = new Button("Update");
            updateButton.setOnAction(ev -> {
                submit(stage, emp, gridPane);
            });
            
            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(ev -> {
                cancelOperation(stage);
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
                UnaryOperator<TextFormatter.Change> numberValidationFormatter = change -> {
                    if(!change.getText().matches("[a-zA-Z$&+,:;=\\\\?@#|/'\\[\\]\\{\\}_+<>.^*()%!-]")){
                        if (change.isContentChange() == true) {
                            if (lbl.getStyle().equalsIgnoreCase("-fx-text-fill: red")) {
                                lbl.setStyle("-fx-text-fill: green");
                            } else if (change.getControlNewText().length() == 0) {
                                lbl.setStyle("-fx-text-fill: red");
                            }
                        }
                        return change;
                    } else {
                        return null;
                    }
                    
                };

                UnaryOperator<TextFormatter.Change> StringValidationFormatter = change -> {
                    if (!change.getText().matches("[$&+,:;=\\\\?@#|/'\\[\\]\\{\\}_+<>.^*()%!-]")){
                        if (change.isContentChange() == true) {
                            if (lbl.getStyle().equalsIgnoreCase("-fx-text-fill: red")) {
                                lbl.setStyle("-fx-text-fill: green");
                            } else if (change.getControlNewText().length() == 0) {
                                lbl.setStyle("-fx-text-fill: red");
                            }
                        }
                        return change;
                    } else {
                        return null;
                    }
                };
                
                String inputValue = f.getType().getSimpleName();
                    if (inputValue.equalsIgnoreCase("Int")) {
                        TextFormatter<Integer> formatter = new TextFormatter<>(numberValidationFormatter);
                        txt.setTextFormatter(formatter);
                    } else if (inputValue.equalsIgnoreCase("String")) {
                        TextFormatter<String> formatter = new TextFormatter<>(StringValidationFormatter);
                        txt.setTextFormatter(formatter);
                    }

                UIUtil.addToGridPane(gridPane, lbl, column, row);

                if (validate_util.check_calendar_exist(f) == true) {
                    DatePicker datePicker = new DatePicker();
                    datePicker.getEditor().setDisable(true);
                    datePicker.getEditor().setOpacity(1);
                    datePicker.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                        if (lbl.getStyle().equalsIgnoreCase("-fx-text-fill: red")) {
                            lbl.setStyle("-fx-text-fill: green");
                        }
                    });
                    GregorianCalendar cal = (GregorianCalendar)f.get(emp);
                    if (validate_ui_functions.populate_calendar_values(cal, datePicker) != true) {

                        lbl.setStyle("-fx-text-fill: red");
                    }
                    UIUtil.addToGridPane(gridPane, datePicker, column + 1, row);
                    
                } else {
                    UIUtil.addToGridPane(gridPane, txt, column + 1, row);
                }
                row = row + 1;
            }

            UIUtil.addToGridPane(gridPane, updateButton, 3, row);
            UIUtil.addToGridPane(gridPane, cancelButton, 2, row);
            Scene scene = UIUtil.createScene(gridPane, 450, 200);
            UIUtil.addSceneToStage(stage, scene);
            stage.show();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}