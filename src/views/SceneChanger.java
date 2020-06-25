/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.IOException;
import java.security.KeyStore;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Volunteer;

/**
 *
 * @author dashi
 */
public class SceneChanger {
    
    
    /**
     * This method will accept the title of the new scene and the .fxml file name 
     * for the view and the actionEvent that trigger the change 
     * @param event
     * @param viewName
     * @param title
     */ 
    public void changeScenes(MouseEvent event, String viewName, String title) throws IOException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        //get the stage from event 
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * This method will change scenes and preload the next scene with a volunteer object
     */
    public void changeScenes(MouseEvent event, String viewName, String title, Volunteer volunteer, ControllerClass controllerClass) throws IOException {
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(viewName));
        Parent parent = loader.load();
        
        Scene scene = new Scene(parent);
        
        //Access the controller class and preload the volunteer
        controllerClass = loader.getController();
        controllerClass.preloadData(volunteer);
        
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        
    }
    
    
}
