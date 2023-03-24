package com.revature.projectzerospring.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revature.projectzerospring.model.Employee;
import com.revature.projectzerospring.model.Ticket;
import com.revature.projectzerospring.model.Ticket.ReimbursementType;
import com.revature.projectzerospring.model.Ticket.StatusValues;
import com.revature.projectzerospring.service.EmployeeService;
import com.revature.projectzerospring.service.TicketService;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    EmployeeService employeeService;
    TicketService ticketService;

    public TicketController(EmployeeService employeeService, TicketService ticketService) {
        this.employeeService = employeeService;
        this.ticketService = ticketService;
    }

    @PostMapping("/")
    public ResponseEntity<String> submitTicket(@RequestHeader String email,
    @RequestHeader String password,
    @RequestHeader BigDecimal amount,
    @RequestHeader ReimbursementType type,
    @RequestHeader(required = false) String description){
        if(!employeeService.verifyEmployeeLogin(email, password)) return ResponseEntity.status(403).body("Failed to verify credentials.");
        ticketService.submitTicket(email, amount, type, description);
        return ResponseEntity.status(200).body("Successfully submitted a ticket.");
    }

    @GetMapping("/")
    public ResponseEntity<List<Ticket>> getAllTcikets(
        @RequestHeader String username, //The username of the person making this request (must be a manager)
        @RequestHeader String password, //The password of the manager in question (stored as as hash in the DB)
        @RequestHeader(required = false) Ticket.StatusValues status,    //(optional) Filters tickets that match the provided status
        @RequestHeader(required = false) Ticket.ReimbursementType type, //(optional) filters tickets that match the provided type
        @RequestHeader(required = false) String employeeEmail //(Optional) get all of the tickets from a specific employee.
        ){
        
        if(!employeeService.verifyEmployeeIsManager(username, password)) return ResponseEntity.status(403).body(null);
        return ResponseEntity.status(200).body(ticketService.getAllTickets(status, type, employeeEmail));
    }

    @GetMapping("/my")
    public ResponseEntity<List<Ticket>> getMyTickets(
        @RequestHeader String email, //The username of the person making this request (can be any employee)
        @RequestHeader String password, //The password of the manager in question (stored as as hash in the DB)
        @RequestHeader(required = false) Ticket.StatusValues status,    //(optional) Filters tickets that match the provided status
        @RequestHeader(required = false) Ticket.ReimbursementType type  //(optional) filters tickets that match the provided type
        ){
        Employee e = employeeService.getEmployee(email);
        if(e == null) return ResponseEntity.status(403).body(null);
        if(e.getPassword() != password.hashCode()) return ResponseEntity.status(403).body(null);
        return ResponseEntity.status(200).body(ticketService.getMyTickets(e, status, type));
    }

    @PutMapping("/finalize")
    public ResponseEntity<String> finalizeTicket(
        @RequestHeader String email,
        @RequestHeader String password,
        @RequestHeader Long ticketId,
        @RequestHeader StatusValues newStatus){
            if(newStatus == StatusValues.PENDING){
                return ResponseEntity.status(400).body("Cannot make a ticket pending!");
            }
            if(!employeeService.verifyEmployeeIsManager(email, password)) {
                return ResponseEntity.status(403).body("Failed to verify credentials.");
            }
            if(ticketService.updateTicket(ticketId, newStatus)){
                return ResponseEntity.status(200).body("Successfully updated ticket status.");
            }
            return ResponseEntity.status(400).body("Failed to update ticket status. Check that the ticket exists and has not already been finalized.");
        }
}
