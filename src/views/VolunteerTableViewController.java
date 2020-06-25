/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.Volunteer;

/**
 * FXML Controller class
 *
 * @author dashi
 */
public class VolunteerTableViewController implements Initializable {

    @FXML
    private TableView<Volunteer> volunteerTableView;
    @FXML
    private TableColumn<Volunteer, Integer> volunteerIDCol;
    @FXML
    private TableColumn<Volunteer, String> firstNameCol;
    @FXML
    private TableColumn<Volunteer, String> lastNameCol;
    @FXML
    private TableColumn<Volunteer, String> phoneCol;
    @FXML
    private TableColumn<Volunteer, LocalDate> birthdayCol;
    @FXML
    private Button editvolunteerButtton;
    
    //Change Scenes
    @FXML
    private void createNewVolunteerButton(MouseEvent event) throws IOException {
        
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "NewUserView.fxml", "Create New Volunteer");
    }
    
    // Edit volunteer button
    @FXML
    private void editVolunteer(MouseEvent event) throws IOException {
        SceneChanger sc = new SceneChanger();
        Volunteer volunteer = this.volunteerTableView.getSelectionModel().getSelectedItem();
        NewUserViewController nuvc = new  NewUserViewController();
        sc.changeScenes(event, "NewUserViewController", "Edit Volunteer", volunteer, nuvc);
        
    }
    
    //If user select volunteer to edit enable edit button
    public void volunteerSelectec(){
        
        editvolunteerButtton.setDisable(false);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //
        editvolunteerButtton.setDisable(true);
        
        // configure the table columns
        volunteerIDCol.setCellValueFactory(new PropertyValueFactory<>("volunteerID"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        birthdayCol.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        
        try {
            loadVolunteers();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
           
    }    
    
    /**
     * This method will lod the volunteer from the data base and load them into the tableView object 
     */
    
    public void loadVolunteers() throws SQLException {
        
        ObservableList<Volunteer> volunteers = FXCollections.observableArrayList();
        
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try {
            //1. connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer", "root", "WGUcollegeIT2018");
            
            //2. create a statement object
            statement = conn.createStatement();
            
            //3. create the SQL query
            resultSet = statement.executeQuery("SELECT * FROM volunteer");
            
            //4. create volunteer objects from each record
            while (resultSet.next()) {
                
                Volunteer newVolunteer = new Volunteer(resultSet.getString("firstName"), resultSet.getString("lastName"), resultSet.getString("phoneNumber"), 
                        resultSet.getDate("birthday").toLocalDate() );
                
                newVolunteer.setVolunteerID(resultSet.getInt("VolunteerID"));
                newVolunteer.setImageFile(new File (resultSet.getString("imageFile")));
                volunteers.add(newVolunteer);
            } 
            volunteerTableView.getItems().addAll(volunteers);
            
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        finally {
            
            if (conn != null) 
                conn.close();
            if (statement != null)
                statement.close();
            if (resultSet != null)
                resultSet.close();
        }
    }

    

    
}
