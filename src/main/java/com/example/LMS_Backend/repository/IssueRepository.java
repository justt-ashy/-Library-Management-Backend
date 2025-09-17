package com.example.LMS_Backend.repository;

import com.example.LMS_Backend.model.Issue;
import com.example.LMS_Backend.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    public List<Issue> findByReturned(Integer returned);
    public Long countByMemberAndReturned(Member member, Integer returned);
}
