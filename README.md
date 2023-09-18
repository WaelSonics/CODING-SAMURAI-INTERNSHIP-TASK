# CODING-SAMURAI-INTERNSHIP-TASK


                                  *Java Development Task 1*
                                  //Task Management System\\  
                                  
This is a simple task management system written in Java. It allows users to create tasks, mark them as completed, and manage their to-do
list. Users can also log in with their credentials and have their tasks associated with their accounts.

1- Prerequisites
    Before you can run this project, you need to have the following installed:
      -Java Development Kit (JDK)
      -MySQL Database
      -Your MySQL database should have a schema named todo_list_db.

2- Installation
    Clone the repository to your local machine using the following command:
      git clone https://github.com/yourusername/your-repo.git

3- Open the project in your preferred Java IDE.

4- Update the database connection configuration:
    In the DatabaseConnector.java file, modify the following constants with your MySQL database credentials:
      -->private static final String JDBC_URL = "jdbc:mysql://localhost:3306/todo_list_db";
      -->private static final String JDBC_USER = "your_database_username";
      -->private static final String JDBC_PASSWORD = "your_database_password";

5- Build and run the project.

6- Usage:
    Run the application, and you will see a menu with the following options:

    -Add a new user
    -Log in as a user
    -Exit
  Select an option by entering the corresponding number.

-To add a new user, choose option 1 and follow the prompts.
-To log in as a user, choose option 2 and enter your user ID and password.
-To exit the application, choose option 3.

Once logged in, you can perform various tasks, including adding tasks, viewing specific tasks, viewing all tasks, marking tasks as
completed, and exiting the user interface.
