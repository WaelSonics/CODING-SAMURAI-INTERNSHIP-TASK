package org.wael;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class Task {
    private int taskID;
    private String title;
    private String description;
    private Date dateCreated;
    private Date dueDate;
    private Boolean completed;

    public Task(){                  //auto Constructor which calls input handler
        this.dateCreated = new Date();
        this.completed = false;
        InputHandler taskHandler = new InputHandler();
        this.title = taskHandler.getTaskTitle();
        this.description = taskHandler.getTaskDescription();
        this.dueDate = taskHandler.getTaskDate();
    }

    public void saveTaskToDatabase(Connection connection, int userId){
        /*
        *
        * define an SQL INSERT query to insert the task's data into the 'tasks' table
        * tasks table and users table are created using SQL commands on MySQL WorkBench
        * The tables'  columns (attributes) are set as the attributes of the classes here (User and Task)
        * But I added the user_id as an attribute for the tasks table to make it a foreign key to belong
        * to a specific user
        *
        */
        String query = "INSERT INTO tasks (user_id, title, description, date_created, due_date, completed) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            //preparing the java.util dates into sql subformat
            java.sql.Date sqlDateCreated = new java.sql.Date(this.dateCreated.getTime());
            java.sql.Date sqlDueDate = new java.sql.Date(this.dueDate.getTime());
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, this.title);
            preparedStatement.setString(3, this.description);
            preparedStatement.setDate(4, sqlDateCreated);
            preparedStatement.setDate(5, sqlDueDate);
            preparedStatement.setBoolean(6, this.completed);


            int rowsAffected = preparedStatement.executeUpdate();   //execute the INSERT query to add the task to the database
            //if rowsAffected = 0 so no change has happened
            if (rowsAffected == 1) {
                System.out.println("Task added to the database Successfully.");
            } else {
                System.err.println("Failed to add the task to the database.");
            }
        } catch (SQLException e) {          //SQL exception
            System.err.println("Error inserting task data into the database: " + e.getMessage());
        }
    }


    public String toString(){
        String s = "\nTask ID: " + this.taskID+
                ".\nTask Title: " + this.title +
                ".\nTask Description: " + this.description +
                ".\nDate Created: " + this.dateCreated.toString() +
                ".\nDue Date: " + this.dueDate +
                ".\nCompleted: " + this.completed;
        return s;
    }

    //getters and setters

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int id){
        this.taskID = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean isComplete() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Boolean markDone() {
        this.completed = true;      //method called to mark a task as completed
        return true;
    }
}
