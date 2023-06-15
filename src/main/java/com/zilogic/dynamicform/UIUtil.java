/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zilogic.dynamicform;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author user
 */
public class UIUtil {
    
    public static Label createLabel(String text) {
        Label label = new Label(text);
        return label;
    }
    
    public static Button createButton(String text) {
        Button button = new Button(text);
        return button;
    }
    public static TextField createTextField(Object text) {
        try {
        TextField txtField = new TextField(text.toString());
        return txtField;
        } catch (Exception e) {
            return null;
        }
    }
    
    public static GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        return gridPane;
    }
    
    public static GridPane addToGridPane(GridPane gridPane , Node node, int column, int row) {
        gridPane.add(node, column, row, 1, 1);
        return gridPane;
    }
    
    public static Stage createStage() {
        Stage stage = new Stage();
        return stage;
    }
    
    public static Stage addSceneToStage(Stage stage, Scene scene) {
        stage.setScene(scene); 
        return stage;
    }
    
    public static Scene createScene(Pane node, int width, int height) {
        Scene scene = new Scene(node , width, height);     
        return scene;
    }
    
    public static void display_stage(Stage stage) {
        stage.show();
    }
    
    public static HBox createHbox() {
        HBox hbox = new HBox();

        return hbox;
    }
    
    public static VBox createVbox() {
        VBox vbox = new VBox();

        return vbox;
    }
    
    public static void closeWindow(Stage stage) {
        stage.close();
    }
}