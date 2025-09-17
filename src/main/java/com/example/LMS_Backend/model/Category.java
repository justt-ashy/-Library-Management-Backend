package com.example.LMS_Backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Setter;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "category")
@Getter
@Setter
public class Category implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;


    @NotNull(message = "*Please enter category name")
    @NotBlank(message = "*Please enter category name")
    @Column(name = "name")
    private String name;

    @NotNull(message = "*Please enter category short name")
    @NotBlank(message = "*Please enter category short name")
    @Length(max = 4, message = "*Must not exceed 4 characters")
    @Column(name = "short_name")
    private String shortName;

    @Column(name = "notes")
    @Length(max = 1000, message = "*Must not exceed 1000 characters.")
    private String notes;

    @Column(name = "create_date")
    private Date createDate;

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Book> books;


}