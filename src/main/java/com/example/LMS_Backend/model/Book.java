package com.example.LMS_Backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private Long id;

    @NotNull(message = "*Please enter book title")
    @NotBlank(message = "*Please enter book title")
    private String title;

    @NotNull(message = "*Please enter book tag")
    @NotBlank(message = "*Please enter book tag")
    private String tag;

    @NotNull(message = "*Please enter book authors")
    @NotBlank(message = "*Please enter book authors")
    private String authors;

    private String publisher;

    private String isbn;

    private Integer status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "*Please select category")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "books"})
    private Category category;
}
