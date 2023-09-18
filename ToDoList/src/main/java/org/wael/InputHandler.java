package org.wael;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.text.ParseException;


/*
*
* This class is made only to facilitate the input interaction with the user
* where all inputs are handled in this class and if error occurs these methods will do the
* job instead of handling issues in the ,ain method
*
 */


public class InputHandler {
    private static Scanner a = new Scanner(System.in);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public String getUserName() {
        System.out.print("\nEnter User Name: ");
        return a.next().trim();
    }

    public int getUserID() {
        System.out.print("\nEnter User ID: ");
        try {
            int id = a.nextInt();
            return id;      // Input was successfully read and stored in 'id'
        } catch (java.util.InputMismatchException e) {
            System.err.println("Invalid input. Please enter an integer.");
            return -1;
        }
    }

    private String setHashPass(String pass){
        String hashed = BCrypt.hashpw(pass, BCrypt.gensalt());      //make the string password hashed
        System.out.println(pass);
        System.out.println(hashed);
        return new String(hashed);
    }

    public String getUserPassWord_Hashed() {
        //directly return the hashed password to save in the database
        //because the string should not be saved for security reasons.
        //usually we save hashed passwords and not password strings themselves
        System.out.print("Enter your Password (at least 8 characters): ");
        String passTest = a.next().trim();
        if (passTest.length() >= 8){
            return setHashPass(passTest);
        }
        System.err.println("Password must be at least 8 characters long. Please try again.");
        return "";
    }

    public String getTaskTitle(){
        System.out.print("\nEnter Your Task's Title: ");
        return a.next().trim();
    }

    public String getTaskDescription(){
        System.out.print("Enter Your Task's description: ");
        return a.nextLine().trim();
    }

    public Date getTaskDate() {         //handle the date format
        System.out.print("Enter task date (dd-MM-yyyy): ");
        try {
            String dateStr = a.next();
            Date parsedDate = dateFormat.parse(dateStr);
            if (parsedDate.before(new Date())) {
                System.out.println("Date is in the past. Please enter a future date.");
                return getTaskDate();       // Recursive call to retry input
            } else {
                return parsedDate;
            }
        } catch (ParseException e) {
            System.err.println("Invalid date format. Please enter the date in dd-MM-yyyy format.");
            return getTaskDate();       // Recursive call to retry input
        }
    }

    public int getTaskID() {
        //Task Id input should be of type int, if not return -1 as an error code
        System.out.print("\nEnter Task ID: ");
        Scanner b = new Scanner(System.in);
        try {
            int id = b.nextInt();
            return id;      // Input was successfully read and stored in 'id'
        } catch (java.util.InputMismatchException e) {
            System.err.println("Invalid input. Please enter an integer.");
            return -1;
        }
    }
}
