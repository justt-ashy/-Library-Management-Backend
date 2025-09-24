package com.example.LMS_Backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "issued_book")
public class IssuedBook implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    @NotNull
    private Book book;

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "issue_id")
//    @NotNull
//    private Issue issue;

    @Column(name = "returned")
    private Integer returned;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Book getBook(){
        return book;
    }

    public void setBook(Book book){
        this.book = book;
    }

//    public Issue getIssue(){
//        return issue;
//    }
//
//    public void setIssue(Issue issue){
//        this.issue = issue;
//    }

    public Integer getReturned(){
        return returned;
    }

    public void setReturned(Integer returned){
        this.returned = returned;
    }
}