/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package views;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import models.Volunteer;

/**
 * FXML Controller class
 *
 * @author dashi
 */
public class NewUserViewController implements Initializable, ControllerClass {

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
    @FXML
    private ImageView imageView;

    private File imageFile;
    private boolean imageFileChanged;
    private Volunteer volunteer;
    
    //File Chooser allow user to change images
    @FXML
    private void changeVolunteerImage(MouseEvent event) {
        
        //Get the Stage to open a new window
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
        //Instance a File Chooser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        
        //fileter for jpeg and png 
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");
        
        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);
        
        
        //Set to the user's picture diectory or user directory if not available
        String userDirectoryString = System.getProperty("user.home") + "\\Pictures";
        File userDirectory = new File(userDirectoryString);
        
        //if you cannot navigate to the picture directory, go to the home 
        if (!userDirectory.canRead()) {
            userDirectory = new File(System.getProperty("user.home"));
        }
        fileChooser.setInitialDirectory(userDirectory);
        
        //open file dialog
        File tmpImageFile = fileChooser.showOpenDialog(stage);
        
        if (tmpImageFile != null) {
            
            imageFile = tmpImageFile;
        
            //update the imageview with the new name
            if (imageFile.isFile()) {
                try {

                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    Image img = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageView.setImage(img);
                    imageFileChanged = true;
                } catch (IOException e) {

                    System.err.println(e.getMessage());
                }
            }
        }
        
    }
    
    
    /*
    This method read from the scene and try to create a new instance of a volunteer
    If a volunteer was successfully created, its update in the databasa
    */
    @FXML
    public void saveVolunteerButtonClick(MouseEvent event) {
        
        try {
            
            Volunteer volunteer;
            if (imageFileChanged) {
                volunteer = new Volunteer(firstNameTextField.getText(), lastNameTextField.getText(), 
                                                phoneNumberTextField.getText(), birthday.getValue(), imageFile);
            } else {
                volunteer = new Volunteer(firstNameTextField.getText(), lastNameTextField.getText(), 
                                                phoneNumberTextField.getText(), birthday.getValue());
            }
            
            errorMessageLabel.setText("");
            volunteer.insertIntoDB();
            
            SceneChanger sceneChanger = new SceneChanger();
            sceneChanger.changeScenes(event, "VolunteerTableView.fxml", "All Volunteers");
 
            
        } catch (Exception e) {
            
            errorMessageLabel.setText(e.getMessage());
        }
    }
    
    @FXML
    private void cancelButton(MouseEvent event) throws IOException {
        
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "VolunteerTableView.fxml", "All Volunteers");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        imageFileChanged = false; //initially the imag has not change, use default 
        //Set a static date value minus 20 years
        //birthday.setValue(LocalDate.now().minusYears(20));
        
        this.errorMessageLabel.setText(""); //
        
        //load default image
        try {
            
            imageFile = new File("./src/images/defaultPerson.png");
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);
            
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }       

    @Override
    public void preloadData(Volunteer volunteer) {
        this.volunteer = volunteer;
    }
    
}
