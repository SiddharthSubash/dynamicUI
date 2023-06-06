/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;


import static com.zilogic.dynamicform.main.mainStage;
import static com.zilogic.dynamicform.main.statusLabel;
import java.lang.reflect.Field;
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

public class Create_ui {
    
    public static Validate_util validate_util= new Validate_util();
    public static Validate_ui_functions validate_ui_functions = new Validate_ui_functions();

    public static void create_completion(Stage stage, Object obj, GridPane gridPane) {
        if (validate_ui_functions.validate_ui(gridPane, obj) == true) {
            statusLabel.setText("Fields Populated");
            statusLabel.setStyle("-fx-text-fill: green");

            stage.close();
        }
    }
    public static void cancel_create_operation(Stage stage) {
        statusLabel.setText("");
        stage.close();
    }

    public static TextField numberValidate(TextField txtField, Label lbl) {
        UnaryOperator<TextFormatter.Change> numberValidationFormatter = change -> {
            if(!change.getText().matches("[a-zA-Z$&+,:;=\\\\?@#|/'\\[\\]<>.^*()%!-]")) {
                if (change.isContentChange() == true) {
                    if (lbl.getStyle().equalsIgnoreCase("-fx-text-fill: red")) {
                        lbl.setStyle("-fx-text-fill: green");
                    } else if (change.getControlNewText().length() == 0) {
                        if (lbl.getStyle().length() != 0) {
                            lbl.setStyle("-fx-text-fill: red");
                        }
                    }
                }
                return change;
            } else {
                return null;
            }
        };
        
        TextFormatter<Integer> formatter = new TextFormatter<>(numberValidationFormatter);
        txtField.setTextFormatter(formatter);

        return txtField;
    }
    
    public static TextField stringValidate(TextField txtField, Label lbl) {
        UnaryOperator<TextFormatter.Change> StringValidationFormatter = change -> {
            if (!change.getText().matches("[$&+,:;=\\\\?@#|/'\\[\\]<>.^*()%!-]")){
                if (change.isContentChange() == true) {
                    if (lbl.getStyle().equalsIgnoreCase("-fx-text-fill: red")) {
                        lbl.setStyle("-fx-text-fill: green");
                    } else if (change.getControlNewText().length() == 0) {
                        if (lbl.getStyle().length() != 0) {
                            lbl.setStyle("-fx-text-fill: red");
                        }
                    }
                }
                return change;
            } else {
                return null;
            }
        };
        
        TextFormatter<String> formatter = new TextFormatter<>(StringValidationFormatter);
        txtField.setTextFormatter(formatter);

        return txtField;
    }

    public static void closeWindow(Stage stage) {
        stage.close();
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
            JsonSerializable.JsonElement javaAnnotation = validate_util.checkAnnotationExist(f);
            if (javaAnnotation != null) {
                data_name = javaAnnotation.name();
            } else {
                data_name = f.getName();
            }
            Label lbl = UIUtil.createLabel(data_name);

            txt = UIUtil.createTextField(val);

            inputValue = f.getType().getSimpleName();

            if (inputValue.equalsIgnoreCase("Int")) {
                txt = numberValidate(txt, lbl);
            } else if (inputValue.equalsIgnoreCase("String")) {
                txt = stringValidate(txt, lbl);
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

                UIUtil.addToGridPane(gridPane, datePicker, column + 1, row);

            } else {
                UIUtil.addToGridPane(gridPane, txt, column + 1, row);
            }
            row = row + 1;
        }   
    }
    
    public static int getNumberOfFields(Object obj) {
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();

        return fields.length;
    }

    public static void create_ui(Object obj) {
        try {

            Stage stage = initializeStage();
            GridPane gridPane = initializeGridPane();
            int numOfFields = getNumberOfFields(obj);

            Button createButton = new Button("Create");
            createButton.setOnAction(ev -> {
                create_completion(stage, obj, gridPane);
            });

            Button cancelButton = new Button("Cancel");
            cancelButton.setOnAction(ev -> {
                cancel_create_operation(stage);
            });

            performOperation(obj, gridPane);

            UIUtil.addToGridPane(gridPane, createButton, 3, numOfFields);
            UIUtil.addToGridPane(gridPane, cancelButton, 2, numOfFields);

            Scene scene = UIUtil.createScene(gridPane, 450, 200);
            UIUtil.addSceneToStage(stage, scene);
            stage.show();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}