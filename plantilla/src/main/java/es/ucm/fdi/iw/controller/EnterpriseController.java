package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.User;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("enterprise")
public class EnterpriseController {

	@Autowired
	private EntityManager entityManager;

	@Autowired
    private LocalData localData;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;
    @ModelAttribute
    public void populateModel(HttpSession session, Model model) {        
        for (String name : new String[] {"u", "url", "ws"}) {
            model.addAttribute(name, session.getAttribute(name));
        }
    }

    private static final Logger log = LogManager.getLogger(EnterpriseController.class);

	@GetMapping("/")
    public String index(Model model) {
        log.info("Empresa acaba de entrar");
        return "enterprise";
    }
        /**
     * Landing page for a user profile
     */

	@GetMapping("{id}")
    public String index(@PathVariable long id, Model model, HttpSession session) {
        User target = entityManager.find(User.class, id);
        model.addAttribute("user", target);
        return "enterprise-info";
    }

    @GetMapping("/enterprise-parkings")
    public String enterpriseParkings(Model model) {
        return "enterprise-parkings";
    }

    @GetMapping("/enterprise/add-parking")
    public String addParkings(Model model) {
        return "add-parking";
    }
    
     @GetMapping("/enterprise/enterprise-plazas")
    public String enterprisePlazas(Model model) {
        return "enterprise-plazas";
    }

    @GetMapping("/enterprise/add-plaza")
    public String addPlaza(Model model) {
        return "add-plaza";
    }
}
