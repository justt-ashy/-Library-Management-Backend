package com.example.LMS_Backend.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;
import lombok.Getter;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="member")
@Getter
@Setter
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @NotEmpty(message = "*Please select member type")
    @NotNull(message="*Please select member type")
    @Column(name="type")
    private String type;

    @NotEmpty(message="*Please enter first name")
    @NotNull(message="*Please enter first name")
    @Column(name="first_name")
    private String firstName;

    @NotEmpty(message="*Please enter middle name")
    @NotNull(message="*Please enter middle name")
    @Column(name="middle_name")
    private String middleName;

    @Column(name="last_name")
    private String lastName;

    @NotEmpty(message="*Please select gender")
    @NotNull(message="*Please select gender")
    @Column(name="gender")
    private String gender;

    @NotNull(message="*Please enter birth date")
    @Column(name="date_of_birth")
    private Date dateOfBirth;

    @Column(name="joining_date")
    private Date joiningDate;

    @Column(name="contact")
    private Date contact;

    @Column(name="email")
    private String email;

    public Member(@NotNull String type,
                  @NotNull String firstName,
                  @NotNull String middleName,
                  @NotNull String lastName,
                  @NotNull String gender,
                  @NotNull Date dateOfBirth,
                  @NotNull Date joiningDate){

        super();
        this.type = type;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.joiningDate = joiningDate;
    }

    public Member(){}
}
