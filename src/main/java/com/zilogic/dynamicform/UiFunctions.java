/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.function.UnaryOperator;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class UiFunctions {
    public static Boolean populate_calendar_values(GregorianCalendar gc, DatePicker datePicker) {
        try {
            int year = gc.get(GregorianCalendar.YEAR);
            int month = gc.get(GregorianCalendar.MONTH);
            int date = gc.get(GregorianCalendar.DATE);

            datePicker.setValue(LocalDate.of(year, month, date));

            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static void submitForm(Stage stage, Object obj, GridPane gridPane, Label lbl, String statusText) {
        if (UiFunctions.validate_ui(gridPane, obj) == true) {
            lbl.setText(statusText);
            lbl.setStyle("-fx-text-fill: green");
            stage.close();
        }
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
                            txtField.setPromptText("Enter " + lbl.getText());
                            txtField.getParent().requestFocus();
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
                            txtField.setPromptText("Enter " + lbl.getText());
                            txtField.getParent().requestFocus();
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

    public static Boolean validate_ui(GridPane gridPane, Object emp) {
        try {
            Label lblNode = new Label();
            ObservableList<Node> childrens = gridPane.getChildren();

            Boolean validateFlag = true;
            int column = 1;
            Class obj = emp.getClass();
            Field[] fields = obj.getDeclaredFields();
            int i = 0;

            for (Node node : childrens) {
                if (GridPane.getColumnIndex(node) != column) {
                    if (node.getClass().getSimpleName().equalsIgnoreCase("Label")) {
                        lblNode = (Label)node;
                    }
                } else if (GridPane.getColumnIndex(node) == column) {

                    if (node.getClass().getSimpleName().equalsIgnoreCase("TextField")) {
                        TextField txt = (TextField) node;
                        if (txt.getText().length() == 0) {
                            validateFlag = false;
                            lblNode.setStyle("-fx-text-fill: red");
                            txt.setPromptText("Enter " + lblNode.getText());
                            txt.getParent().requestFocus();
                            i = i + 1;
                        } else if (txt.getText().isEmpty()) {
                            validateFlag = false;
                            lblNode.setStyle("-fx-text-fill: red");
                            txt.setPromptText("Enter " + lblNode.getText());
                            txt.getParent().requestFocus();
                            i = i + 1;
                        } else {
                            TextField txtObj = txt;
                            txtObj.setText(txt.getText());

                            if (fields[i].getType().getSimpleName().equalsIgnoreCase("String")) {

                                if (validateFlag == true) {
                                    fields[i].set(emp, txt.getText());
                                }
                                i = i + 1;
                            } else if (fields[i].getType().getSimpleName().equalsIgnoreCase("Int")) {
                                int val = Integer.parseInt(txt.getText());  
                                if (validateFlag == true) {
                                    fields[i].setInt(emp, val);
                                }
                                i = i + 1;
                            }

                        }
                    } else if (node.getClass().getSimpleName().equalsIgnoreCase("DatePicker")) {

                        DatePicker datePicker = (DatePicker) node;
                        GregorianCalendar gc = new GregorianCalendar();
                        if (datePicker.getValue() == null) {
                            validateFlag = false;
                            lblNode.setStyle("-fx-text-fill: red");
                            datePicker.setPromptText("Enter " + lblNode.getText());
                            datePicker.getParent().requestFocus();
                            i = i + 1;
                        } else {
                            LocalDate lv = datePicker.getValue();
                            gc.set(lv.getYear(), lv.getMonthValue(), lv.getDayOfMonth());
                            if (validateFlag == true) {
                                fields[i].set(emp, gc);
                            }
                            i = i + 1;
                        }
                    }
                }
            }
        
            return validateFlag;
        } catch (Exception e) {
            return null;
        }
    }   
}