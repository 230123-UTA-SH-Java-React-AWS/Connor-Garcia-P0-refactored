package com.revature.projectzerospring.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.projectzerospring.model.Employee;
import com.revature.projectzerospring.model.Employee.Roles;
import com.revature.projectzerospring.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/")
    public ResponseEntity<String> registerEmployee(@RequestHeader String email, @RequestHeader String password){
        if(employeeService.getEmployee(email) != null) return ResponseEntity.status(403).body("Someone already has that email address");
        employeeService.registerEmployee(email, password);
        return ResponseEntity.status(200).body("Successfully registered an account");
    }

    //If I remember right, getting a list of all employees was outside of the scope of the original project. Oh well.
    //It was in my implementation, and this project is a recreation of the features that the old one had.
    @GetMapping("/")
    public ResponseEntity<List<Employee>> getAllEmployees(@RequestHeader String email, @RequestHeader String password){
        if(employeeService.verifyEmployeeIsManager(email, password)){
            return ResponseEntity.status(200).body(employeeService.getAllEmployees());
        }
        return ResponseEntity.status(403).body(null);
    }

    //Because of the jank way I'm handling logins (no actual persistent session), this function essentially only tells you
    // if your credentials are valid and could be used to make future requests.
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestHeader String email, @RequestHeader String password){
        return ResponseEntity.status(200).body(employeeService.verifyEmployeeLogin(email, password));
    }

    //Allows a manager employee (and only a manager) to change the role of another employee. In this way, an employee
    // can be promoted or demoted. One thing of note: I do not check to see if you are attempting to make a meaningless
    // change like setting an employee's role to one that already exists.
    @PutMapping("/changeRole")
    public ResponseEntity<String> promoteEmployee(
        @RequestHeader String email,
        @RequestHeader String password,
        @RequestHeader String otherEmplEmail,
        @RequestHeader Roles newRole){
        if(!employeeService.verifyEmployeeIsManager(email, password)){
            return ResponseEntity.status(403).body("Either you did not provide the correct credentials or you are not a manager.");
        }
        Employee otherEmployee = employeeService.getEmployee(otherEmplEmail);
        if(otherEmployee == null) return ResponseEntity.status(404).body("There is no employee with the email address " + otherEmplEmail);
        otherEmployee.setRole(newRole);
        employeeService.updateEmployee(otherEmployee);
        return ResponseEntity.status(200).body("Successfully changed the status of the other employee.");
    }
}
