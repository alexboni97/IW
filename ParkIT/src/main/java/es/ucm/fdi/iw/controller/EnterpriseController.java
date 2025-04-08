package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

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
import es.ucm.fdi.iw.model.Transferable;
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
        User target = (Enterprise) entityManager.find(Enterprise.class, id);

        model.addAttribute("user", target.toTransfer());
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
 {

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

               // Enviar notificación a los administradores
    // Obtener la empresa del usuario actual
    // y crear el mensaje de notificación
    // (suponiendo que la empresa está en la sesión)


    Enterprise enterprise = (Enterprise) session.getAttribute("u");
    String notificationText = "Nueva solicitud de parking de la empresa " + enterprise.getName() +
            " con id: " + request.getId() + " en la dirección: " + request.getAddress();
       // ID del administrador al que se le envía el mensaje (puede ser null para enviar a todos los administradores)
        Message message = new Message();
        message.setSender(enterprise);
        message.setRecipient(null); // null para enviar a todos los administradores
        message.setDateSent(LocalDateTime.now());
        message.setText(notificationText);
        entityManager.persist(message);

        // Convertir el mensaje a JSON y enviarlo
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(message.toTransfer());
        messagingTemplate.convertAndSend("/topic/admin", json);

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

    /**
	 * Returns JSON with all received messages
	 */
	@GetMapping(path = "received", produces = "application/json")
	@Transactional // para no recibir resultados inconsistentes
	@ResponseBody // para indicar que no devuelve vista, sino un objeto (jsonizado)
	public List<Message.Transfer> retrieveMessages(HttpSession session) {
		long userId = ((User) session.getAttribute("u")).getId();
		User u = entityManager.find(User.class, userId);
		log.info("Generating message list for user {} ({} messages)",
				u.getUsername(), u.getReceived().size());
		return u.getReceived().stream().map(Transferable::toTransfer).collect(Collectors.toList());
	}
    
    /**
	 * Returns JSON with count of unread messages
	 */
	@GetMapping(path = "unread", produces = "application/json")
	@ResponseBody
	public String checkUnread(HttpSession session) {
		long userId = ((User) session.getAttribute("u")).getId();
		long unread = entityManager.createNamedQuery("Message.countUnread", Long.class)
				.setParameter("userId", userId)
				.getSingleResult();
		session.setAttribute("unread", unread);
		return "{\"unread\": " + unread + "}";
	}
}
