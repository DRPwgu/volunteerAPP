/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Volunteer;

/**
 * FXML Controller class
 *
 * @author dashi
 */
public class NewUserViewController implements Initializable {

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private DatePicker birthday;
    @FXML
    private Label errorMessageLabel;

    /*
    This method read from the scene and try to create a new instance of a volunteer
    If a volunteer was successfully created, its update in the databasa
    */
    public void saveVolunteerButtonClick() {
        
        try {
            
            Volunteer volunteer = new Volunteer(firstNameTextField.getText(), lastNameTextField.getText(), 
                                                phoneNumberTextField.getText(), birthday.getValue());
            errorMessageLabel.setText("");
            volunteer.insertIntoDB();
            
        } catch (Exception e) {
            
            errorMessageLabel.setText(e.getMessage());
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.errorMessageLabel.setText("");
    }    
    
}
