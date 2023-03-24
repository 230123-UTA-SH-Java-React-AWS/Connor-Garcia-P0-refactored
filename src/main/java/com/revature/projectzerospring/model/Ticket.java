package com.revature.projectzerospring.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Ticket {
    public enum StatusValues {PENDING, APPROVED, DENIED}
    public enum ReimbursementType {TRAVEL, LODGING, FOOD, OTHER}

    @Id
    @GeneratedValue
    @NonNull
    private Long id;
    @NonNull
    private StatusValues status;
    @NonNull
    private ReimbursementType reimbursementType;
    @NonNull
    private BigDecimal amount;
    private String description;
    @NonNull
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    @JsonManagedReference
    private Employee owner;
}
