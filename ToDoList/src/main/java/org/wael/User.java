package org.wael;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

public class User {         //we may have more than one user
    private static int id_counter = 0;                    //static counter for Ids starting from 1
    private int USER_ID = ++id_counter;                   //automatically assign a permanent Id for the user
    //these two above in case I am adding the User to the ArrayList and not the database
    //in the database the Id is automatically assigned
    private String userName;
    private String hashPassWord;                                //security reasons
    private ArrayList<Task> userTasks = new ArrayList<>();      //each user has his own list of tasks


    public User(String userName, String pass){          //Default constructor
        this.userName = userName;
        this.hashPassWord = pass;
    }

    public User(int user_id, Connection connection, ArrayList<User> users) {
        /*
        *
        *   In this constructor, the purpose is to search in the database for a user
        *   Using his ID
        *
         */

        try {
            String sqlQuery = "SELECT user_name, password FROM users WHERE user_id = ?";    //create a SQL query to retrieve user data based on user_id

            //create a PreparedStatement for the query
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setInt(1, user_id);

            ResultSet resultSet = preparedStatement.executeQuery();     //execute the query and retrieve the result set
            //check if a user with the provided user_id exists
            if (resultSet.next()) {
                //retrieve user data from the result set and initialize object properties
                this.USER_ID = user_id;     //assuming USER_ID is a field in the User class
                this.userName = resultSet.getString("user_name");
                this.hashPassWord = resultSet.getString("password");
                users.add(new User(this.userName, this.hashPassWord));
            } else {            //User doesnt exist
                System.out.println("Your user Id is not in my Database! ");
                System.out.println("Exiting Login ...");
                return;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUserToDatabase(Connection connection) {
        /*
         *
         * define an SQL INSERT query to insert the user's data into the 'users' table
         * tasks table and users table are created using SQL commands on MySQL WorkBench
         * The tables'  columns (attributes) are set as the attributes of the classes here (User and Task)
         * But I added the user_id as an attribute for the tasks table to make it a foreign key to belong
         * to a specific user
         *
         */
        //define an SQL INSERT query to insert the user's data into the (users) table
        String query = "INSERT INTO users (user_name, password) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, this.userName);
            preparedStatement.setString(2, this.hashPassWord);
            //execute the INSERT query to add the user to the database
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println("User added to the database.");
            } else {
                System.err.println("Failed to add the user to the database.");
            }
        } catch (SQLException e) {
            System.err.println("Error inserting user data into the database: " + e.getMessage());
        }
    }

    //method to test the hashed version of the password entered with the actual hashed password
    public Boolean testPassword(String password_test){
        try{
        return BCrypt.checkpw(password_test, this.hashPassWord);
        }catch (Exception e){
            return false;
        }

    }

    public int getUserID(){
        return this.USER_ID;
    }

    public String getUserName() {
        return userName;
    }

    public ArrayList<Task> getUserTasks() {
        return userTasks;
    }

    public void addTask(Task newTask) {
        this.userTasks.add(newTask);
        newTask.setTaskID(this.userTasks.indexOf(newTask));
    }

    public void refreshTasksIds(){
        for(Task t : this.userTasks){
            t.setTaskID(this.userTasks.indexOf(t));
        }
    }

    public void deleteTask(Task task){
        this.userTasks.remove(task);
    }

    public String toString(){
        String s ="\nUser ID: " + this.USER_ID+
                ".\nUser Name: " + this.userName +
                ".\nUser Tasks Count: " + this.userTasks.size();
        return s;
    }
}
