package com.revature.projectzerospring.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Employee {
    public enum Roles {STANDARD, MANAGER}

    @Id
    @GeneratedValue
    @NonNull
    @JsonIgnore
    private Long id;
    @NonNull
    @Column(unique = true)
    private String email;
    @JsonIgnore
    private int password;
    @NonNull
    private Roles role;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    @JsonBackReference
    private List<Ticket> tickets;
}
