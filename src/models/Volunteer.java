/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.PreparedStatement;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.sql.Date;

/**
 *
 * @author dashi
 */
public class Volunteer {
    
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate birthday;
    private File imageFile;
    private int volunteerID;

    public Volunteer(String firstName, String lastName, String phoneNumber, LocalDate birthday) {
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setBirthday(birthday);
        setImageFile(new File("./src/images/defaultPerson.png"));
    }

    public Volunteer(String firstName, String lastName, String phoneNumber, LocalDate birthday, File imageFile) throws IOException {
        this(firstName, lastName, phoneNumber, birthday);
        setImageFile(imageFile);
        copyImageFile();
    }

    
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getVolunteerID() {
        return volunteerID;
    }

    public void setVolunteerID(int volunteerID) {
        
        if (volunteerID >= 0) {
           this.volunteerID = volunteerID; 
        } else {
            throw new IllegalArgumentException("VolunteerID must be >= 0");
        }   
    }

    
    /**
     * area code   city      house 
     * NXX         -XXX      -XXXX
     * @param phoneNumber 
     */
    
    public void setPhoneNumber(String phoneNumber) {
        
        //Validate phone number 
        if (phoneNumber.matches("[2-9]\\d{2}[-.]?\\d{3}[-.]\\d{4}")) {
           this.phoneNumber = phoneNumber; 
        } else {
            throw new IllegalArgumentException("Phone number must be in the pattern NXX-XXX-XXXX");
        }

    }

    public LocalDate getBirthday() {
        return birthday;
    }

    /**
     * This will validate that the volunteer is between the ages of 10 and 100
     * @param birthday 
     */
    public void setBirthday(LocalDate birthday) {
        
        int age = Period.between(birthday, LocalDate.now()).getYears();
        
        if (age >= 10 && age <= 100) {
            this.birthday = birthday;
        } else {
            throw new IllegalArgumentException("Volunteer must be 10-100 years of age");
        }   
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    /**
     * This method will copy the file specified to the images directory on the server and give it a unique name
     */
    public void copyImageFile() throws IOException{
        
        //create a new Path to copy the image into a local directory
        Path sourcePAth = imageFile.toPath();
        
        String uniqueFileName = getUniqueFileName(imageFile.getName());
        
        Path targetPath = Paths.get("./src/images/" + uniqueFileName);
        
        //copy the file to new directory
        Files.copy(sourcePAth, targetPath, StandardCopyOption.REPLACE_EXISTING);
        
        //update the imageFile to point to the new File
        imageFile = new File(targetPath.toString());
    }
    
    /**
     * This method will receive a string the represents a file name and return a
     * String with random, unique set of letters prefixed to it
     * @return 
     */
    private String getUniqueFileName(String oldFileName) {
        
        String newName;
        
        //create a Random Number Genertor
        SecureRandom rng = new SecureRandom();
        
        //loop until we have a unique file name
        do {            
            newName = "";
            
            //generate 32 random characters
            for (int count = 1; count < 33; count++) {
                
                int nextChar;
                
                do {                    
                    
                    nextChar = rng.nextInt(123);
                } while (!validCharacterValue(nextChar));
                
                newName = String.format("%s%c", newName, nextChar);
            }
            
            newName += oldFileName;
        } while (!uniqueFileInDirectory(newName));
        
        return newName;
    }
    
    /**
     * This method will search the images directory and ensure that the file name is unique
     */
    
    public boolean uniqueFileInDirectory(String fileName) {
        
        File directory = new File("./src/images/");
        
        File[] dir_contents = directory.listFiles();
        
        for(File file: dir_contents) {
            
            if (file.getName().equals(fileName))
                return false;
        }
        return true;
    }
    /**
     * This method will validate if the integer given corresponds to a valid ASCII  
     * character that could be used in a file name 
     */
    public boolean  validCharacterValue(int asciiValue){
        
        //0-9 range 48-57
        if(asciiValue >= 48 && asciiValue <= 57)
            return true;
        //A-Z range 65-90.
        if(asciiValue >= 65 && asciiValue <= 90)
            return true;
        //a-z range 97 122
        if(asciiValue >= 97 && asciiValue <= 122)
            return true;
        
        return false;
    }
    
    /**
     * This method will return a formattedString with the persons' first name, last name and age
     */
    public String toString() {
        return String.format("%s %s is %d years old", firstName, lastName, Period.between(birthday, LocalDate.now()).getYears());
    }
    
    /**
     * This method will write the instance of the Volunteer into the database
     */
    public void insertIntoDB() throws SQLException {
        
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        
        try {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/volunteer", "root", "WGUcollegeIT2018");
            
            //2 Create a String that holds the query with ? as user inputs
            String sql = "INSERT INTO volunteer (firstName, lastName, phoneNumber, birthday, imageFile)" 
                    + "VALUES(?,?,?,?,?);";
            
            //3 prepare the query
            preparedStatement = conn.prepareCall(sql);
            
            //4 Convert the birthday into a SQL date
            Date db = Date.valueOf(birthday);
            
            //5 Bind the values to the parameters
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, phoneNumber);
            preparedStatement.setDate(4, db);
            preparedStatement.setString(5, imageFile.getName());
            
            preparedStatement.executeUpdate();
            
            
        } catch (Exception e) {
            
            System.err.println(e.getMessage());
        }
        finally {
            
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
