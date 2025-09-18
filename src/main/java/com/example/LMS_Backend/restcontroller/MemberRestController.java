package com.example.LMS_Backend.restcontroller;

import com.example.LMS_Backend.model.Member;
import com.example.LMS_Backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value="/rest/member")
public class MemberRestController {

    @Autowired
    private MemberService memberService;

    @GetMapping(value={"/","/list"})
    public List<Member> all(){
        return  memberService.getAll();
    }
}
