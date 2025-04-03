package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.web.bind.annotation.ResponseBody;
import com.fasterxml.jackson.core.JsonProcessingException;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Enterprise;
import es.ucm.fdi.iw.model.Message;
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

    @GetMapping("/enterprise-requests")
    public String enterpriseRequests(Model model) {
        User user = (User) model.getAttribute("u");

        List<Request> requests = entityManager
                .createNamedQuery("Request.findByEnterprise", Request.class)
                .setParameter("enterprise", user)
                .getResultList();

        // El nombre que le pongamos el de entre comillas es el que usamos luego para
        // recorrer en la vista con thimeleaf
        model.addAttribute("requests", requests);

        return "enterprise-requests";
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
    /*
     * PARÁMETROS ELIMINADOS PARA QUE SE ENVIEN EN FORMATO DE JSON:
     * 
     * @RequestParam String name, @RequestParam String address, @RequestParam int
     * cp,
     * 
     * @RequestParam String city, @RequestParam String country, @RequestParam String
     * telephone,
     * 
     * @RequestParam String email, @RequestParam double feePerHour, @RequestParam
     * String openingTime,
     * 
     * @RequestParam String closingTime, @RequestParam Integer totalSpots
     */
    /**
     * Posts a message to a user.
     * 
     * @param id of target user (source user is from ID)
     * @param o  JSON-ized message, similar to {"message": "text goes here"}
     * @throws JsonProcessingException
     */
    @PostMapping("/request-parking")
    @Transactional
    @ResponseBody
    public String requestParking(@RequestBody JsonNode requestData, HttpSession session, Model model)
            throws JsonProcessingException {

        try {

            String name = requestData.get("name").asText();
            String address = requestData.get("address").asText();
            int cp = requestData.get("cp").asInt();
            String city = requestData.get("city").asText();
            String country = requestData.get("country").asText();
            String telephone = requestData.get("telephone").asText();
            String email = requestData.get("email").asText();
            double feePerHour = requestData.get("feePerHour").asDouble();
            String openingTime = requestData.get("openingTime").asText();
            String closingTime = requestData.get("closingTime").asText();
            Integer totalSpots = requestData.get("totalSpots").asInt();

            LocalTime parsedOpeningTime = LocalTime.parse(openingTime);
            LocalTime parsedClosingTime = LocalTime.parse(closingTime);

            Request request = new Request();
            request.setName(name);
            request.setAddress(address);
            request.setEnabled(true);
            request.setCp(cp);
            request.setCity(city);
            request.setCountry(country);
            request.setTelephone(Integer.parseInt(telephone));
            request.setEmail(email);
            request.setFeePerHour(feePerHour);
            request.setLatitude(0.0);
            request.setLongitude(0.0);
            request.setOpeningTime(parsedOpeningTime);
            request.setClosingTime(parsedClosingTime);
            request.setState("Pendiente");
            request.setType("Añadir");
            request.setTotalSpots(totalSpots);
            request.setEnterprise((Enterprise) session.getAttribute("u"));

            entityManager.persist(request);
            entityManager.flush();
            entityManager.clear();

            // Creamos y enviamos la notificación
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode messageNode = mapper.createObjectNode();
            messageNode.put("text", "Nueva solicitud de parking de " + request.getEnterprise().getName() + " con id: "
                    + request.getId() + " en la dirección: " + request.getAddress());
            messageNode.put("dateSent", LocalDateTime.now().toString());
            messageNode.put("senderId", ((Enterprise) session.getAttribute("u")).getId());

            String messageJson = mapper.writeValueAsString(messageNode);
            messagingTemplate.convertAndSend("/topic/admin", messageJson);

            model.addAttribute("success", "Solicitud realizada con éxito. Esperando respuesta del administrador.");
            return "{\"result\": \"Solicitud realizada con éxito . Esperando respuesta del administrador.\"}";
        } catch (Exception e) {
            model.addAttribute("error", "Hubo un error al guardar la solicitud: " +
                    e.getMessage());
            return "redirect:/error";
        }

        /* A FALTA DE MÁS INFORMACIÓN */
        // return enterpriseRequests(model);
    }

}
