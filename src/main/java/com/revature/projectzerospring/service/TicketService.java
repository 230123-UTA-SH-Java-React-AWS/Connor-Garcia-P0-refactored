package com.revature.projectzerospring.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.revature.projectzerospring.model.Employee;
import com.revature.projectzerospring.model.Ticket;
import com.revature.projectzerospring.model.Ticket.ReimbursementType;
import com.revature.projectzerospring.model.Ticket.StatusValues;
import com.revature.projectzerospring.repository.EmployeeRepository;
import com.revature.projectzerospring.repository.TicketRepository;

@Service
public class TicketService {
    TicketRepository ticketRepository;
    EmployeeRepository employeeRepository;

    public TicketService(TicketRepository ticketRepository, EmployeeRepository employeeRepository){
        this.ticketRepository = ticketRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Ticket> getAllTickets(StatusValues status, ReimbursementType type, String email) {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream()
            .filter(ticket -> {
                if(status != null && ticket.getStatus() != status) return false;
                if(email != null && !Objects.equals(ticket.getOwner().getEmail(), email)) return false;
                if(type != null && ticket.getReimbursementType() != type) return false;
                return true;
            })
            .collect(Collectors.toList());
    }
    
    public List<Ticket> getMyTickets(Employee employee, StatusValues status, ReimbursementType type) {
        List<Ticket> tickets = ticketRepository.findAllByOwner(employee);
        return tickets.stream()
            .filter(ticket -> {
                if(status != null && ticket.getStatus() != status) return false;
                if(type != null && ticket.getReimbursementType() != type) return false;
                return true;
            })
            .collect(Collectors.toList());
    }

    public void submitTicket(String email, BigDecimal amount, ReimbursementType type, String description) {
        Ticket t = new Ticket();
        t.setAmount(amount);
        t.setDescription(description);
        t.setReimbursementType(type);
        t.setStatus(StatusValues.PENDING);
        Optional<Employee> eOptional = employeeRepository.findByEmail(email);
        if(eOptional.isPresent()){ //This check is largely useless because by the time this function call happens we are verified logged-in.
            Employee e = eOptional.get();
            t.setOwner(e);
            ticketRepository.save(t);
        }
    }

    public boolean updateTicket(Long ticketId, StatusValues status){
        Optional<Ticket> tOptional = ticketRepository.findById(ticketId);
        if(tOptional.isPresent()){
            Ticket t = tOptional.get();
            if(t.getStatus() != StatusValues.PENDING) return false;
            t.setStatus(status);
            ticketRepository.save(t);
            return true;
        } else return false;
    }
}
