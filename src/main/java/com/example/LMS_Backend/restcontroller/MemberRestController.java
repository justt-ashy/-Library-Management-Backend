package com.example.LMS_Backend.restcontroller;

import com.example.LMS_Backend.model.Member;
import com.example.LMS_Backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

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

    @GetMapping(value="/{id}")
    public ResponseEntity<Member> getMember(@PathVariable(name="id") Long id){
        Member member = memberService.get(id);
        if(member != null){
            return ResponseEntity.ok(member);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(value="/save")
    public ResponseEntity<String> saveMember(@RequestBody Member member){
        try {
            memberService.addNew(member);
            return ResponseEntity.ok("Member saved successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error saving member: " + e.getMessage());
        }
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<String> updateMember(@PathVariable(name="id") Long id, @RequestBody Member member){
        try {
            Member existingMember = memberService.get(id);
            if(existingMember != null){
                member.setId(id);
                memberService.save(member);
                return ResponseEntity.ok("Member updated successfully");
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating member: " + e.getMessage());
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<String> deleteMember(@PathVariable(name="id") Long id){
        try {
            Member member = memberService.get(id);
            if(member != null){
                if(!memberService.hasUsage(member)){
                    memberService.delete(id);
                    return ResponseEntity.ok("Member deleted successfully");
                } else {
                    return ResponseEntity.badRequest().body("Cannot delete member: they have active book issues");
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting member: " + e.getMessage());
        }
    }
}
