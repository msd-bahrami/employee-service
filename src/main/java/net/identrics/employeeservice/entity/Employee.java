package net.identrics.employeeservice.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
@Data
public class Employee {

    @Id
    @GenericGenerator(name = "increment", strategy = "increment")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "increment")
    private long id;

    @NotBlank
    private String name;

    @NotBlank
    private String company;

    @NotBlank
    private String education;

    @Min(0)
    private long salary;

}
