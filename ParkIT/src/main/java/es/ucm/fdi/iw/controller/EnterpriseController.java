package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
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
import es.ucm.fdi.iw.model.Enterprise;
import es.ucm.fdi.iw.model.Parker;
import es.ucm.fdi.iw.model.Request;
import es.ucm.fdi.iw.model.Reserve;
import es.ucm.fdi.iw.model.Parking;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
        for (String name : new String[] { "u", "url", "ws" }) {
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
        User user = (User) model.getAttribute("u");

        List<Parking> parkings = entityManager
                .createNamedQuery("Parking.findByEnterprise", Parking.class)
                .setParameter("enterprise", user)
                .getResultList();

        model.addAttribute("parkings", parkings);

        return "enterprise-parkings";
    }

    @GetMapping("/add-parking")
    public String addParkings(Model model) {
        return "add-parking";
    }

    @GetMapping("/enterprise-plazas")
    public String enterprisePlazas(Model model) {
        return "enterprise-plazas";
    }

    @GetMapping("/add-plaza")
    public String addPlaza(Model model) {
        return "add-plaza";
    }

    // Función para solicitar añadir un parking al administrador.
    // Ruta con la que llega a este método desde la vista
    @PostMapping("/request-parking")
    public String requestParking(@RequestParam String name, @RequestParam String address, @RequestParam int cp,
            @RequestParam String city, @RequestParam String country, @RequestParam int telephone,
            @RequestParam String email, @RequestParam double feePerHour, @RequestParam String openingTime,
            @RequestParam String closingTime, @RequestParam Integer totalSpots, HttpSession session) {

        // //Creamos un objeto de tipo request
        // Request request = new Request();

        // //Metemos los datos al request
        // request.setName(name);
        // request.setAddress(address);
        // request.setEnabled(true);
        // request.setCp(cp);
        // request.setCity(city);
        // request.setCountry(country);
        // request.setTelephone(telephone);
        // request.setEmail(email);
        // request.setFeePerHour(feePerHour);
        // request.setOpeningTime(LocalTime.parse(openingTime));
        // request.setClosingTime(LocalTime.parse(closingTime));
        // request.setState("AÑADIR");
        // request.setTotalSpots(totalSpots);
        // request.setLatitude(null);
        // request.setLongitude(null);
        // request.setEnterprise((Enterprise) session.getAttribute("u"));

        // try {
        // entityManager.persist(request);
        // entityManager.flush();
        // entityManager.clear();
        // model.addAttribute("success", "Solcitud realizada con éxito. Esperando
        // respuesta del administrador.");
        // } catch (Exception e) {
        // model.addAttribute("error", "Hubo un error al guardar la solicitud: " +
        // e.getMessage());
        // return "redirect:/error";
        // }

        // //Nombre de la vista a la que quiero redirigir.
        return "enterprise-parkings";
    }
}
