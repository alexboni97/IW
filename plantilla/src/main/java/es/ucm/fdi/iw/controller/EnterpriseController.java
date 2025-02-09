package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;

@Controller
public class EnterpriseController {

    // @ModelAttribute
    // public void populateModel(HttpSession session, Model model) {        
    //     for (String name : new String[] {"u", "url", "ws"}) {
    //         model.addAttribute(name, session.getAttribute(name));
    //     }
    // }

    // private static final Logger log = LogManager.getLogger(EnterpriseController.class);

    // @GetMapping("/enterprise")
    // public String enterprise(Model model) {
    //     return "enterprise"; 
    // }
}
