/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;

import static com.zilogic.dynamicform.Main.mainVbox;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.animation.FadeTransition;

import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

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

    public static Boolean submitForm(AnchorPane anchorPane, Object obj, GridPane gridPane, Label lbl, String statusText) {
        Boolean validateFlag = UiFunctions.validate_ui(gridPane, obj);
        
        if (validateFlag == true) {
            lbl.setText(statusText);
            lbl.setStyle("-fx-text-fill: green");

            anchorPane.getChildren().clear();
        }
        ChangeBackGroundColor(validateFlag);
        return validateFlag;
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
            printNodes(gridPane);
            printFields(emp);
            for (Node node : childrens) {
                
                if (GridPane.getColumnIndex(node) == 0) {
                    if (node.getClass().getSimpleName().equalsIgnoreCase("Label")) {
                        
                        lblNode = (Label)node;
                    }
                } else if (GridPane.getColumnIndex(node) == column) {
                    if (node.getClass().getSimpleName().equalsIgnoreCase("TextField")) {
                        TextField txt = (TextField) node;
                        TextField txtObj = txt;
                        txtObj.setText(txt.getText());
                        
                        if (fields[i].getType().getSimpleName().equalsIgnoreCase("String")) {
                            if (txt.getText().equals("")) {
                                validateFlag = false;
                                lblNode.setStyle("-fx-text-fill: red");
                            } else {
                                if (validateFlag == true) {
                                    fields[i].set(emp, txt.getText());
                                }
                            }
                            i = i + 1;
                        } else if (fields[i].getType().getSimpleName().equalsIgnoreCase("Int")) {

                            if (txt.getText().equals("")) {
                                validateFlag = false;
                                lblNode.setStyle("-fx-text-fill: red");
                            } else {
                                int val = Integer.parseInt(txt.getText());  
                                if (validateFlag == true) {
                                    fields[i].setInt(emp, val);
                                }
                            }
                            i = i + 1;
                        } else if (fields[i].getType().getSimpleName().equalsIgnoreCase("float")) {

                            if (txt.getText().equals("")) {
                                validateFlag = false;
                                lblNode.setStyle("-fx-text-fill: red");
                            } else {
                                float val = Float.parseFloat(txt.getText());
                                String valConversion = Float.toString(val);
                                if (validateFlag == true) {
                                    fields[i].setFloat(emp, val);
                                    txt.setText(valConversion);
                                    //txt.getParent().requestFocus();
                                }
                            }
                            i = i + 1;
                        }  else if (fields[i].getType().getSimpleName().equalsIgnoreCase("Double")) {
                            if (txt.getText().equals("")) {
                                validateFlag = false;
                                lblNode.setStyle("-fx-text-fill: red");
                            } else {
                                double doubleVal = Double.parseDouble(txt.getText());
                                String doubleValConversion = Double.toString(doubleVal);
                                if (validateFlag == true) {
                                    fields[i].setDouble(emp, doubleVal);
                                    txt.setText(doubleValConversion);
                                }
                            }
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
        
    public static void ChangeBackGroundColor(Boolean validateFlag) {
        String color;
        
        if (validateFlag) {
            color =  "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #6ae2aa, #c9c9c9);";
        } else {
            color = "-fx-background-color: linear-gradient(from 25% 25% to 100% 100%, #ff5d48, #c9c9c9);";
        }
        mainVbox.setStyle(color);
        FadeTransition ft = new FadeTransition(Duration.millis(400), mainVbox);
        ft.setFromValue(1.0);
        ft.setToValue(0.6);
        ft.setCycleCount(4);
        ft.setAutoReverse(true);
        ft.play();

        ft.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                mainVbox.setStyle("-fx-background-color: white;");
            }
        });
    }
    
    public static void printFields(Object emp) {
        Class obj = emp.getClass();
        Field[] fields = obj.getDeclaredFields();
        
        for (Field field: fields) {
            System.out.println("fields" + field.getType().getSimpleName());
        }       
    }
    
    public static void printNodes(GridPane gridPane) {
        ObservableList<Node> childrens = gridPane.getChildren();
        
        for (Node node: childrens) {
            System.out.println("nodes" + node.getClass().getSimpleName());
        }
    }
}