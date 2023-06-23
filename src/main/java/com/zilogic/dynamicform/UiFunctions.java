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
import java.util.regex.Pattern;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class UiFunctions {
    public static boolean patternMatchFound = false;
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

    public static void submitForm(AnchorPane anchorPane, Object obj, GridPane gridPane, Label lbl, String statusText) {
        Boolean validateFlag = UiFunctions.validate_ui(gridPane, obj);
        if (validateFlag == true) {
            lbl.setText(statusText);
            lbl.setStyle("-fx-text-fill: green");
            anchorPane.getChildren().clear();
        }
    }

    public static int checkFieldDataType(String FieldMember) {
        if (FieldMember.equalsIgnoreCase("Int")) {
            return 1;
        } else if (FieldMember.equalsIgnoreCase("String")) {
            return 2;
        } else if (FieldMember.equalsIgnoreCase("Float")) {
            return 3;
        }  else if (FieldMember.equalsIgnoreCase("Double")) {
            return 4;
        }
        return 0;
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
    
    public static TextField floatValidate(TextField txtField, Label lbl) {
        UnaryOperator<TextFormatter.Change> floatValidationFormatter = change -> {

            String regex = "^\\d+\\.\\d\\d$";
            Pattern pattern = Pattern.compile(regex);

            //System.out.println(patternMatchFound + change.getControlNewText() + "chahe" + change.getControlText() + "asfsa" + txtField.getText() + "ttt" + change.getText());
            if (pattern.matcher(change.getControlNewText()).matches()) {

                if (patternMatchFound) {
                    patternMatchFound = false;
                } else {
                    patternMatchFound = true;
                }
                return change;
            }
            // For allowing backspace
            else if(!change.getText().matches("[0-9 a-zA-Z$&+,:;=\\\\?@#|/'\\[\\]<>^*()%!-]") && patternMatchFound == true) {
                patternMatchFound = false;
                return change;
            }

            // For entering characters only if pattern not found and prevents multiple decimal point
            else if(!change.getText().matches("[a-z A-Z$&+,:;=\\\\?@#|/'\\[\\]<>^*()%!-]")) {
                if (patternMatchFound == true) {
                    return null;
                } else if (change.isContentChange() == true) {
                    if (lbl.getStyle().equalsIgnoreCase("-fx-text-fill: red")) {
                        lbl.setStyle("-fx-text-fill: green");
                    } else if (change.getControlNewText().length() == 0) {
                        if (lbl.getStyle().length() != 0) {
                            lbl.setStyle("-fx-text-fill: red");
                            txtField.setPromptText("Enter " + lbl.getText());
                            txtField.getParent().requestFocus();
                        }
                    } else if (change.getControlNewText().charAt(0) == '.') {
                        return null;
                    } else {

                        int index = -1;
                        int count = 0;
                        while ((index = change.getControlNewText().indexOf(".", index + 1)) != -1) {
                            count = count + 1;
                        }
                        if (count > 1) {
                            return null;
                        } else {
                            return change;
                        }
                    }
                }
                return change;
            } 
            else {
                return null;
            }
        };
        
        TextFormatter<Integer> formatter = new TextFormatter<>(floatValidationFormatter);
        txtField.setTextFormatter(formatter);

        return txtField;
    }
    
    public static TextField doubleValidate(TextField txtField, Label lbl) {
        UnaryOperator<TextFormatter.Change> doubleValidationFormatter = change -> {

            String regex = "^\\d+\\.\\d\\d\\d\\d$";
            Pattern pattern = Pattern.compile(regex);

            if (pattern.matcher(change.getControlNewText()).matches()) {

                if (patternMatchFound) {
                    patternMatchFound = false;
                } else {
                    patternMatchFound = true;
                }
                return change;
            }
            // For allowing backspace
            else if(!change.getText().matches("[0-9 a-zA-Z$&+,.:;=\\\\?@#|/'\\[\\]<>^*()%!-]") && patternMatchFound == true) {
                patternMatchFound = false;
                return change;
            }

            // For entering characters only if pattern not found and prevents multiple decimal point
            else if(!change.getText().matches("[a-z A-Z$&+,:;=\\\\?@#|/'\\[\\]<>^*()%!-]")) {
                if (patternMatchFound == true) {
                    return null;
                }
                else if (change.isContentChange() == true) {
                    if (lbl.getStyle().equalsIgnoreCase("-fx-text-fill: red")) {
                        lbl.setStyle("-fx-text-fill: green");
                    } else if (change.getControlNewText().length() == 0) {
                        if (lbl.getStyle().length() != 0) {
                            lbl.setStyle("-fx-text-fill: red");
                            txtField.setPromptText("Enter " + lbl.getText());
                            txtField.getParent().requestFocus();
                        }
                    } else if (change.getControlNewText().charAt(0) == '.') {

                        return null;
                    } else {

                        int index = -1;
                        int count = 0;
                        while ((index = change.getControlNewText().indexOf(".", index + 1)) != -1) {
                            count = count + 1;
                        }
                        if (count > 1) {
                            return null;
                        } else {
                            return change;
                        }
                    }
                }
                return change;
            } 
            else {
                return null;
            }
        };
        
        TextFormatter<Integer> formatter = new TextFormatter<>(doubleValidationFormatter);
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
                        } else if (fields[i].getType().getSimpleName().equalsIgnoreCase("float")) {
                            float val = Float.parseFloat(txt.getText());
                            String valConversion = Float.toString(val);

                            fields[i].setFloat(emp, val);
                            txt.setText(valConversion);
                            txt.getParent().requestFocus();

                            i = i + 1;
                        }  else if (fields[i].getType().getSimpleName().equalsIgnoreCase("Double")) {
                            double doubleVal = Double.parseDouble(txt.getText());

                            String doubleValConversion = Double.toString(doubleVal);

                            fields[i].setDouble(emp, doubleVal);

                            txt.setText(doubleValConversion);

                            i = i + 1;
                        }
                    } else if (node.getClass().getSimpleName().equalsIgnoreCase("DatePicker")) {

                        DatePicker datePicker = (DatePicker) node;
                        GregorianCalendar gc = new GregorianCalendar();
                        if (datePicker.getValue() == null) {
                            validateFlag = false;
                            lblNode.setStyle("-fx-text-fill: red");

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
    
    public static void clearDataValue(TextField txtField) {
        txtField.setText("");
    }

    public static void clearDatePickerValue(Label dateLbl, DatePicker datePicker) {

        datePicker.setValue(null);
    }
}