package id.ac.ui.cs.advprog.subsmanagementservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class SubscriptionBoxController {
    @GetMapping("/")
    @ResponseBody
    public String addSubsBox() {
        return "<h1>Hello World</h1>";
    }

}
