package com.example.LMS_Backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "book")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Better for MySQL/Postgres
    @Column(name="id")
    private Long id;

    @NotNull(message = "*Please enter book title")
    @NotBlank(message = "*Please enter book title")
    @Column(name="title")
    private String title;

    @NotNull(message = "*Please enter book tag")
    @NotBlank(message = "*Please enter book tag")
    @Column(name="tag")
    private String tag;

    @NotNull(message = "*Please enter book authors")
    @NotBlank(message = "*Please enter book authors")
    @Column(name = "authors")
    private String authors;

    @Column(name = "publisher")
    private String publisher;

    @Column(name="isbn")
    private String isbn;

    @Column(name="status")
    private Integer status;

    @Column(name="create_date")
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "*Please select category")
    @JsonIgnore
    private Category category;


}
