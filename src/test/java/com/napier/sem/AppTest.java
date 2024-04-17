package com.napier.sem;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AppTest
{
    static App app;

    @BeforeAll
    static void init()
    {
        app = new App();
    }

    @Test
    public void testDisplayEmployeeWithValidEmployee() {
        ArrayList<Employee> employees = new ArrayList<Employee>();
        Employee emp = new Employee();
        emp.emp_no = 1;
        emp.first_name = "Kevin";
        emp.last_name = "Chalmers";
        emp.title = "Engineer";
        emp.salary = 55000;
        // Create instance of App class and call displayEmployee method
        App app = new App();
        app.displayEmployee(emp);

        // Add assertions here to verify that the method output matches the expected output
    }

    @Test
    public void testDisplayEmployeeWithNullEmployee() {
        Employee emp = null;

        // Create instance of App class and call displayEmployee method
        App app = new App();
        app.displayEmployee(emp);

        // Add assertions here to verify that the method behaves correctly with a null input
    }
}