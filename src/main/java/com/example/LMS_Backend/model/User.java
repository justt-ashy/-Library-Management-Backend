package com.example.LMS_Backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Setter;
import lombok.Getter;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;


    private String role;

    @Column(name = "display_name", nullable = false)
    @JsonProperty("display_name")
    private String displayName;

    @Column(nullable = false)
    private Boolean active = true;

}
