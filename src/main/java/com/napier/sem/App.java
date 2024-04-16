package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class App {


    public static void main(String[] args) {
        // Create new Application
        App a = new App();
        // Connect to database
        a.connect();
        // Get Employees with the title "Engineer"
        List<Employee> engineers = a.getEmployee("Engineer");
        // Display results for each engineer
        for (Employee emp : engineers) {
            a.displayEmployee(emp);
        }
        // Disconnect from database
        a.disconnect();
    }

    /**
     * Connection to MySQL database.
     */
    private Connection con = null;

    /**
     * Connect to the MySQL database.
     */
    public void connect() {
        try {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Wait a bit for db to start
                Thread.sleep(30000);
                // Connect to database
                con = DriverManager.getConnection("jdbc:mysql://db:3306/employees?useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + Integer.toString(i));
                System.out.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted? Should not happen.");
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect() {
        if (con != null) {
            try {
                // Close connection
                con.close();
            } catch (Exception e) {
                System.out.println("Error closing connection to database");
            }
        }
    }
    public List<Employee> getEmployee(String title) {
        List<Employee> employees = new ArrayList<>();
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary " +
                            "FROM employees " +
                            "JOIN salaries ON employees.emp_no = salaries.emp_no AND salaries.to_date = '9999-01-01' " +
                            "JOIN titles ON employees.emp_no = titles.emp_no AND titles.to_date = '9999-01-01' " +
                            "WHERE titles.title = '" + title + "' " +
                            "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Loop through the result set
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.salary = (int) rset.getDouble("salary");
                employees.add(emp);
            }
        } catch (SQLException e) {
            System.out.println("Failed to get employees by title: " + e.getMessage());
        }
        return employees;
    }

    public void displayEmployee(Employee emp)
    {
        if (emp != null)
        {
            System.out.println(
                    emp.emp_no + " " +
                            emp.first_name + " " +
                            emp.last_name + " " +
                            emp.title + " " +
                            "Salary:" + emp.salary + " " +
                            emp.dept_name + " " +
                            "Manager: " + emp.manager + " ");
        }
    }
}