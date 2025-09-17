package com.example.LMS_Backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public User(){};

    public User(@NotNull String displayName,
                @NotNull String username,
                @NotNull String password,
                @NotNull String role){

        super();
        this.displayName = displayName;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @NotNull
    @Column(name="display_name")
    private String displayName;

    // ✅ Getters
    @Getter
    @NotNull
    @Column(name="username")
    private String username;

    @Setter
    @NotNull
    @Column(name="password")
    private String password;

    @NotNull
    @Column(name="role")
    private String role;

    @NotNull
    @Column(name="active")
    private Integer active;

    @NotNull
    @Column(name="created_date")
    private Date createdDate;

    @Column(name="last_modified_date")
    private Date lastModifiedDate;
}
