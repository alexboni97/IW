package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import es.ucm.fdi.iw.model.Parking;
import es.ucm.fdi.iw.model.Parking.Transfer;
import es.ucm.fdi.iw.model.Parker;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Admin;
import es.ucm.fdi.iw.model.Enterprise;
import es.ucm.fdi.iw.model.Lorem;

import java.util.ArrayList;
import java.util.List;

/**
 *  Non-authenticated requests only.
 */
@Controller
public class RootController {

    private static final Logger log = LogManager.getLogger(RootController.class);

    @Autowired
 	private PasswordEncoder passwordEncoder;

    @Autowired
	private EntityManager entityManager;

    @ModelAttribute
    public void populateModel(HttpSession session, Model model) {        
        for (String name : new String[] {"u", "url", "ws"}) {
            model.addAttribute(name, session.getAttribute(name));
        }
    }

	@GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        boolean error = request.getQueryString() != null && request.getQueryString().indexOf("error") != -1;
        model.addAttribute("loginError", error);
        return "login";
    }

	@GetMapping("/")
    public String index(Model model) {
        List<Parking> parkings = entityManager.createNamedQuery("Parking.findAll", Parking.class).getResultList();
        List<Transfer> transferParkings = new ArrayList<>();
        for (Parking p : parkings) {
            transferParkings.add(p.toTransfer());
        }
        model.addAttribute("parkings", transferParkings);

        System.out.println("Found " + parkings.size() + " parkings");
        for (Parking p : parkings) {
            System.out.println("Parking: " + p.getName());
        }

        return "index";
    }

    @PostMapping("/")
    @Transactional
    public String poblarUsuarios() {
        
        Admin a = new Admin();
        a.setId(1);
        a.setEnabled(true);
        a.setRole(User.Role.ADMIN);
        a.setUsername("a");
        a.setPassword(passwordEncoder.encode("aa"));
        a.setWallet(10000);
        a.setEmail("1234@park-it.com");
        a.setTelephone(000000000);

        a.setCodigo("1234");

        entityManager.persist(a);


        Parker u = new Parker();
        u.setId(2);
        u.setEnabled(true);
        u.setRole(User.Role.USER);
        u.setUsername("b");
        u.setPassword(passwordEncoder.encode("aa"));
        u.setWallet(10000);
        u.setEmail("pepe@gmail.com");
        u.setTelephone(000000001);

        u.setDNI("aaa");
        u.setFirstName("Pepe");
        u.setSecondName("Perez");

        entityManager.persist(u);
        

        Enterprise e = new Enterprise();
        e.setId(3);
        e.setEnabled(true);
        e.setRole(User.Role.ENTERPRISE);
        e.setUsername("e");
        e.setPassword(passwordEncoder.encode("aa"));
        e.setWallet(0);
        e.setEmail("e@empresa1.com");
        e.setTelephone(000000002);

        e.setName("Empresa1");
        e.setCIF("ES1234");

        entityManager.persist(e);        

        for (int i=0; i<10; i++) {
            Parker p = new Parker();
            p.setUsername("user" + i);
            p.setPassword(passwordEncoder.encode(UserController.generateRandomBase64Token(9)));
            p.setDNI(i + "A");
            p.setEnabled(true);
            p.setRole(User.Role.USER);
            p.setFirstName(Lorem.nombreAlAzar());
            p.setSecondName(Lorem.apellidoAlAzar());
            p.setWallet(100);
            entityManager.persist(p);
         }

        return "index";
    }

    @GetMapping("/info")
    public String info(Model model) {
        return "info";
    }

    @GetMapping("/help")
    public String ayuda(Model model) {
        return "help";
    }

}
