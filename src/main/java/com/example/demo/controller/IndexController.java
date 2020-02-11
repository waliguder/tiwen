package com.example.demo.controller;

import com.example.demo.dto.PageDTO;
import com.example.demo.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {

   @Autowired(required = false)
   private QuestionService questionService;

    @GetMapping("/")
    public String  index(Model model,
                         @RequestParam(name = "page",defaultValue = "1") Integer page,
                         @RequestParam(name = "size",defaultValue = "5") Integer size){
        PageDTO pageDTO = questionService.list(page,size);
        model.addAttribute("pagination",pageDTO);
        return "index";
    }
}
