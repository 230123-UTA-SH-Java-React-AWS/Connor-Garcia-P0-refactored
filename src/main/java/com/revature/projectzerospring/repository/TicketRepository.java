package com.revature.projectzerospring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.projectzerospring.model.Employee;
import com.revature.projectzerospring.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findAllByOwner(Employee employee);
    
}
