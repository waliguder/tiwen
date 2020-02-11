package com.example.demo.controller;

import com.example.demo.dto.PageDTO;
import com.example.demo.model.User;
import com.example.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileCroller {

    @Autowired(required = false)
    private QuestionService questionService;
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action",value = "") String action,
                          Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "5") Integer size){
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            model.addAttribute("error","用户未登陆");
            return "redirect:/";
        }
        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的问题");
        }else if("repies".equals(action)){
            model.addAttribute("section","repies");
            model.addAttribute("sectionName","最新回复");
        }
        PageDTO pageDTO = questionService.myQuestionList(user.getId(),page,size);
        model.addAttribute("pagination",pageDTO);
        return "profile";
    }
}
