package org.wael;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Connection connection = startDatabase();        //connect to MySQL server on my Local machine
        Scanner a = new Scanner(System.in);
        ArrayList<User> users = new ArrayList<>();      //array of users only in the main function (for testing)
        int choice;                 //adding choice
        do {
            printChoice();          //function prints menu
            try {                   //input should be int if not dont take it
                choice = a.nextInt();
                switch (choice) {
                    case 1 -> addUser(users, connection);
                    case 2 -> loginUser(users, connection ,a);
                    case 3 -> closeSystem(connection, a);
                    default -> System.out.println("Your choice is invalid, try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer choice.");
                a.nextLine();       //consume the invalid input
                choice = 0;         //set choice to 0 to continue the loop
            }
        }while (choice != 3);       //exit on 3
    }

    private static void endDatabase(Connection c) {
        DatabaseConnector.disconnect(c);            //end database connection
    }

    private static Connection startDatabase() {
        Connection connection = DatabaseConnector.connect();    //start database connection
        return connection;
    }

    private static void addUser(ArrayList<User> users, Connection connection) {
        System.out.println("\n\n\n~~Welcome to Add User Page~~");
        System.out.println("----------------------------\n");
        InputHandler userHandler = new InputHandler();
        String name = userHandler.getUserName();
        String password = userHandler.getUserPassWord_Hashed();
        if (!name.isEmpty() && !name.isBlank()){        //if input naem is good
            User newUser = new User(name, password);
            users.add(newUser);         //to add optionally on the array list
            newUser.saveUserToDatabase(connection);     //add the new user to the database
            //System.out.println("\nUser Added Successfully!");
            System.out.println(newUser.toString());               //print the user's data
            System.out.println("Exiting Add User Page...\n\n\n");
        }
        else{
            System.err.println("\nFailed, try again (Bad name or password)!");
            System.out.println("Exiting Add User Page...\n");
        }
    }

    private static void loginUser(ArrayList<User> users, Connection connection, Scanner a) {
        System.out.println("~~Welcome to Login Page~~");
        System.out.println("-------------------------\n");
        InputHandler userHandler = new InputHandler();
        int id_test = userHandler.getUserID();          //get id
        if(id_test>=1){             //Ids start from 1
            User userChoice = new User(id_test, connection, users);             //create a user to save in case user found in database
            if(userChoice == null){               //if doesn't exist exit
                return;
            }
            else {
                String password_test = userHandler.getUserPassWord_Hashed();    //password should be hashed
                if(userChoice.testPassword(password_test)){                 //if password correct login
                    System.out.println("Login Successful!!!\n");
                    loginSuccessfulUser(userChoice, connection, a);         //jump to loginSuccess method
                }
                else{
                    System.out.println("Incorrect Password!!\n\n  Exiting...\n");
                    return;                                 //get back into main page
                }
            }
        }
    }

    private static void loginSuccessfulUser(User u, Connection connection, Scanner a) {
        int choice;
        do{
            System.out.println("\n~~~~ Welcome User " + u.getUserName() + "! ~~~~\n\n");
            printTaskChoice();              //second menu
            try {                   //input should be int if not dont take it
                choice = a.nextInt();
                switch (choice) {
                    case 1 -> addNewTask(u, connection);        //each choice has a method
                    case 2 -> viewTask(u);
                    case 3 -> viewAllTasks(u);
                    case 4 -> deleteTask(u);
                    case 5 -> markCompleted(u);
                    case 6 -> System.out.println("Exiting User's Page...");     //Exit on 6
                    default -> System.out.println("Your choice is invalid, try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer choice.");
                a.nextLine();       //consume the invalid input
                choice = 0;         //set choice to 0 to continue the loop
            }
        }while(choice!=6);          //quit to main page if exit
    }

    private static void addNewTask(User u, Connection connection) {      //each task belong to a user by foreign key (user_id)
        System.out.println("\n~~~~ Welcome to Add New Task Page! ~~~~\n");
        Task newTask = new Task();                  //all inputs in constructor Task
        newTask.saveTaskToDatabase(connection, u.getUserID());      //save new task in MySQL
        //u.addTask(newTask);                             //troubleshooting the logic using internal data structure (ArrayList)
        System.out.println("Your Task added successfully!!");
        System.out.println(newTask.toString());             //print the task
    }

    private static void viewTask(User u){       //user object should be passed because we need the ID
        System.out.println("\n~~~~ Welcome to View Task Page! ~~~~\n");
        InputHandler taskHandler = new InputHandler();
        int task_id = taskHandler.getTaskID();  //user should enter his task id to view it
        if(task_id>=1){                         //task id should be 1 or above
            Task taskChoice = searchForTask(task_id, u);        //seach for task_id between tasks for User's id
            if(taskChoice == null){
                System.err.println("Task not found!\nExiting...\n");
                return;
            }
            else{
                System.out.println(taskChoice.toString());      //Task not null, so found and displayed
            }
        }
        else    System.err.println("Invalid Task ID. Exiting...\n");
    }

    private static void viewAllTasks(User u) {      //view all tasks in  a for each loop
        System.out.println("\n~~~~ Welcome to View All Tasks Page! ~~~~\n");
        for (Task t : u.getUserTasks()) {
            System.out.println(t.toString() + "\n");
        }
    }

    private static void deleteTask(User u){         //Remove task from array list of tasks.(similarly for the database)
        System.out.println("\n~~~~ Welcome to Delete Task Page! ~~~~\n");
        InputHandler taskHandler = new InputHandler();
        int task_id = taskHandler.getTaskID();
        if(task_id>=1){
            Task taskChoice = searchForTask(task_id, u);        //search method called
            if(taskChoice == null){
                System.err.println("Task not found!\nExiting...\n");
                return;
            }
            else{
                u.deleteTask(taskChoice);           //method handled in User's class
                u.refreshTasksIds();
                System.out.println("Deleted Successfully!");
            }
        }
        else    System.out.println("Invalid Task ID. Exiting...\n");
    }

    private static void markCompleted(User u) {     //similar to above logic, but setting the Boolean attribute (completed)
        System.out.println("\n~~~~ Welcome to Delete Task Page! ~~~~\n");
        InputHandler taskHandler = new InputHandler();
        int task_id = taskHandler.getTaskID();
        if (task_id >= 1) {
            Task taskChoice = searchForTask(task_id, u);
            if (taskChoice == null) {
                System.out.println("Task not found!\nExiting...\n");
                return;
            }
            else if (taskChoice.markDone())         //method belonging to TAsk class
                System.out.println("Marked Successfully!");
            else
                System.out.println("Failed To Mark");
        }
        else    System.out.println("Invalid Task ID. Exiting...\n");
    }

    private static Task searchForTask(int id, User u){
        for(Task t : u.getUserTasks())
            if (id == t.getTaskID())
                return t;
        return null;
    }

    private static void printTaskChoice() {
        System.out.println("1- Add New Task.\n2- View Specific Task.\n3- View All Tasks.\n4- Mark Task as Complete.\n5- Exit.");
        System.out.print("Enter your choice: ");
    }

    public static void printChoice(){
        System.out.println("\n\n~~~~Welcome to the ToDo System Samurai bro~~~~\n----------------------------------------------\n\n");
        System.out.println("1- Add new User.\n2- Login.\n3- Exit.\n");
        System.out.print("Enter your choice: ");
    }

    private static void closeSystem(Connection connection, Scanner a) {
        a.close();          //make sure everything is closed before terminating the program
        endDatabase(connection);
        System.out.println("\n\nExiting System...\n_________________");
    }
}