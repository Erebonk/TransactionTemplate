package com.erebo.transaction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
@Slf4j
public class TransactionApplication implements CommandLineRunner {

    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/trans_test";

    private static final String USER = "root";
    private static final String PASSWORD = "root";


    public static void main(String[] args) {
        SpringApplication.run(TransactionApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

            Connection connection;
            Statement statement;

            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            connection.setAutoCommit(false);

            statement = connection.createStatement();

            String SQL;

            SQL = "SELECT * FROM developers";

            ResultSet resultSet = statement.executeQuery(SQL);

            System.out.println("Retrieving data from database...");
            System.out.println("\nDevelopers:");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialty = resultSet.getString("specialty");
                int salary = resultSet.getInt("salary");

                System.out.println("\n================\n");
                System.out.println("id: " + id);
                System.out.println("Name: " + name);
                System.out.println("Specialty: " + specialty);
                System.out.println("Salary: $" + salary);
            }

            System.out.println("\n===========================\n");
            System.out.println("Creating savepoint...");
            Savepoint savepointOne = connection.setSavepoint("SavepointOne");

            try {
                SQL = "INSERT INTO developer VALUES (6, 'John','C#', 2200)";
                statement.executeUpdate(SQL);

                SQL = "INSE INTHE developers VALUES (7, 'Ron', 'Ruby', 1900)";
                statement.executeUpdate(SQL);

                connection.commit();
            } catch (SQLException e) {
                System.out.println("SQLException. Executing rollback to savepoint...");
                connection.rollback(savepointOne);
            }
            SQL = "SELECT * FROM developers";
            resultSet = statement.executeQuery(SQL);

            System.out.println("Retrieving data from database...");
            System.out.println("\nDevelopers:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialty = resultSet.getString("specialty");
                int salary = resultSet.getInt("salary");

                System.out.println("id: " + id);
                System.out.println("Name: " + name);
                System.out.println("Specialty: " + specialty);
                System.out.println("Salary: $" + salary);
                System.out.println("\n================\n");
            }

            System.out.println("Closing connection and releasing resources...");
            resultSet.close();
            statement.close();
            connection.close();
    }
}

