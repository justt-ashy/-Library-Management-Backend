package com.example.LMS_Backend.service;

import com.example.LMS_Backend.model.Member;
import com.example.LMS_Backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.Setter;
import lombok.Getter;

import java.util.Date;


@Service
@Setter
@Getter
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private IssueService issueService;

    public Member addNew(Member member){
        member.setJoiningDate(new Date());
        return memberRepository.save(member);
    }

    public Member save(Member member){
        return memberRepository.save(member);
    }

    public void delete(Member member){
        memberRepository.delete(member);
    }

    public void delete(Long id){
        memberRepository.deleteById(id);
    }

    public boolean hasUsage(Member member){
        return issueService.getCountByMember(member) > 0;
    }
}
