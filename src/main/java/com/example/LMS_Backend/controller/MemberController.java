package com.example.LMS_Backend.controller;

import com.example.LMS_Backend.common.Constants;
import com.example.LMS_Backend.model.Member;
import com.example.LMS_Backend.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ModelAttribute(name = "memberTypes")
    public List<String> memberTypes(){
        return Constants.MEMBER_TYPES;
    }

    @RequestMapping(value={"/", "/list"}, method = RequestMethod.GET)
    public String showMemberPage(Model model){
        model.addAttribute("members", memberService.getAll());
        return "/member/list";
    }

    @RequestMapping(value="/add", method = RequestMethod.GET)
    public String addMemberpage(Model model){
        model.addAttribute("member", new Member());
        return "/member/form";
    }

    @RequestMapping(value="/edit/{id}", method = RequestMethod.GET)
    public String editMemberPage(@PathVariable(name="id") Long id, Model model){
        Member member = memberService.get(id);
        if( member != null){
            model.addAttribute("member", member);
            return "/member/form";
        }
        else{
            return "redirect:/member/add";
        }
    }


    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String saveMember(@Valid Member member, BindingResult bindingResult, final RedirectAttributes redirectAttributes){
        if( bindingResult.hasErrors()){
            return "/member/form";
        }

        if( member.getId() == null){
            memberService.addNew(member);
            redirectAttributes.addFlashAttribute("SuccessMsg", "'" + member.getFirstName() + " " + member.getMiddleName() +  "' is added as new member.");
            return "redirect:/member/add";
        }
        else{
            Member updatedMember = memberService.save(member);
            redirectAttributes.addFlashAttribute("successMsg", "Changes for '" + member.getFirstName() + " " + member.getMiddleName() + "' is saved successfully.");
            return "redirect:/member/edit" + updatedMember.getId();
        }
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
    public String removeMember(@PathVariable(name="id") Long id, Model model){
        Member member = memberService.get(id);
        if( member != null ){
            if( memberService.hasUsage(member)){
                model.addAttribute("memberInUse", true);
                return showMemberPage(model);
            }
            else{
                memberService.delete(id);
            }
        }

        return "redirect:/member/list";
    }
}
