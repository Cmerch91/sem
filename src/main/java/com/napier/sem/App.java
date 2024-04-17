package com.napier.sem;

import java.sql.*;
import java.util.ArrayList;

public class App {

    public static void main(String[] args) {
        // Create new Application
        App a = new App();


        // Connect to database
        a.connect();


        Department dept = new Department();
        dept.dept_name = "Development";
        ArrayList<Employee> employees = a.getSalariesByDepartment(dept);

        // Display Engineer details with salaries
        a.printSalaries(employees);


        a.displayEmployees(employees);

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

    /**
     * Retrieve employees who belong to a specified department.
     *
     * @param dept The Department object representing the department.
     * @return An ArrayList of Employee objects representing employees in the specified department.
     */
    public ArrayList<Employee> getSalariesByDepartment(Department dept) {

        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT employees.emp_no, employees.first_name, employees.last_name, salaries.salary "
                            + "FROM employees "
                            + "JOIN salaries ON employees.emp_no = salaries.emp_no "
                            + "JOIN dept_emp ON employees.emp_no = dept_emp.emp_no "
                            + "JOIN departments ON dept_emp.dept_no = departments.dept_no "
                            + "WHERE salaries.to_date = '9999-01-01' "
                            + "AND departments.dept_name = '" + dept + "' "
                            + "ORDER BY employees.emp_no ASC";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Populate the ArrayList with employees
            ArrayList<Employee> employees = new ArrayList<>();
            while (rset.next()) {
                Employee emp = new Employee();
                emp.emp_no = rset.getInt("employees.emp_no");
                emp.first_name = rset.getString("first_name");
                emp.last_name = rset.getString("last_name");
                emp.salary = rset.getInt("salary");
                employees.add(emp);
            }
            return employees;
        }
        catch (Exception e)
        {
        System.out.println(e.getMessage());
        System.out.println("Failed to get salary details");
        return null;
        }
    }

    public Department getDepartment(String deptName) {
        try {
            // Create an SQL statement
            Statement stmt = con.createStatement();
            // Create string for SQL statement
            String strSelect =
                    "SELECT * FROM departments WHERE dept_name = '" + deptName + "'";
            // Execute SQL statement
            ResultSet rset = stmt.executeQuery(strSelect);
            // Check if department exists
            if (rset.next()) {
                Department department = new Department();
                department.dept_name = rset.getString("dept_name");
                return department;
            } else {
                System.out.println("Department not found: " + deptName);
            }
        } catch (SQLException e) {
            System.out.println("Failed to get department details: " + e.getMessage());
        }
        return null;
    }



    /**
     * Print the details of employees along with their salaries.
     *
     * @param employees An ArrayList of Employee objects.
     */
    public void printSalaries(ArrayList<Employee> employees) {
        if (employees == null) {
            System.out.println("No employees");
            return;
        }

        System.out.println(String.format("%-10s %15s %20s %8s", "Emp No", "First Name", "Last Name", "Salary"));
        for (Employee emp : employees)

        {
            if (emp == null)
                continue;
            String emp_string =
                    String.format("%10s %15s %20s &8s",
                            emp.emp_no, emp.first_name, emp.last_name, emp.salary);
            System.out.println(emp_string);
        }
    }

    public void displayEmployees(ArrayList<Employee> emp) {
        if (emp != null) {
            for (Employee e : emp) {
                System.out.println(
                        e.emp_no + " " +
                                e.first_name + " " +
                                e.last_name + "\n" +
                                e.title + "\n" +
                                "Salary: " + e.salary + "\n" +
                                e.dept_name + "\n" +
                                "Manager: " + e.manager + "\n"
                );
            }
        }
    }
}


