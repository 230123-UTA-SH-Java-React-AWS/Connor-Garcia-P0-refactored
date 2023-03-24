package com.revature.projectzerospring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revature.projectzerospring.model.Employee;
import com.revature.projectzerospring.model.Employee.Roles;
import com.revature.projectzerospring.repository.EmployeeRepository;

@Service
public class EmployeeService {
    EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public boolean verifyEmployeeIsManager(String email, String password) {
        Employee e = getEmployee(email);
        if(e == null) return false;
        if(e.getPassword() != password.hashCode()) return false;
        return e.getRole() == Employee.Roles.MANAGER;
    }

    public boolean verifyEmployeeLogin(String email, String password) {
        Employee e = getEmployee(email);
        if(e == null) return false;
        return e.getPassword() == password.hashCode();
    }
    
    public Employee getEmployee(String email){
        Optional<Employee> eOptional = employeeRepository.findByEmail(email);
        if(eOptional.isPresent()){
            return eOptional.get();
        } else {
            return null;
        }
    }

    //Update the employee's information to match the new information provided.
    //Used to promote or demote an employee.
    public void updateEmployee(Employee e){
        employeeRepository.save(e);
    }

    //Register a new employee with the provided email address and password.
    //This returns a boolean indicating whether the operation was successful.
    public boolean registerEmployee(String email, String password){
        if(getEmployee(email) != null) return false;
        Employee e = new Employee();
        e.setEmail(email);
        e.setPassword(password.hashCode());
        e.setRole(Roles.STANDARD);
        employeeRepository.save(e);
        return true;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
