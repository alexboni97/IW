package es.ucm.fdi.iw.controller;
import es.ucm.fdi.iw.AppConfig;
import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Enterprise;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.Parking;
import es.ucm.fdi.iw.model.Parking.Transfer;
import es.ucm.fdi.iw.model.Parker;
import es.ucm.fdi.iw.model.Reserve;
import es.ucm.fdi.iw.model.Spot;
import es.ucm.fdi.iw.model.Transferable;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.Vehicle;
import es.ucm.fdi.iw.model.User.Role;
import io.micrometer.common.lang.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.ArrayList;

//Importame el transfer

/**
 * User management.
 *
 * Access to this end-point is authenticated.
 */
@Controller()
@RequestMapping("user")
public class UserController {

    private final AppConfig appConfig;

    private final AdminController adminController;

	private static final Logger log = LogManager.getLogger(UserController.class);

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private LocalData localData;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private PasswordEncoder passwordEncoder;

    UserController(AdminController adminController, AppConfig appConfig) {
        this.adminController = adminController;
        this.appConfig = appConfig;
    }

	@ModelAttribute
	public void populateModel(HttpSession session, Model model) {

		for (String name : new String[] { "u", "url", "ws" }) {
			model.addAttribute(name, session.getAttribute(name));
		}
	}

	public double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
		final int R = 6371; // Radio de la Tierra en km
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
				   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
				   Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return R * c;
	}
	
	// El return es por la vista que devuelve.
	@PostMapping("/map")
	public String map(
			@RequestParam @Nullable LocalDate startDate, @RequestParam @Nullable LocalDate endDate,
			@RequestParam @Nullable LocalTime startTime, @RequestParam @Nullable LocalTime endTime,
			@RequestParam @Nullable String latitude,
			@RequestParam @Nullable String longitude,
			Model model) {
		Double radio = 30.0;
		List<Parking> parkings = entityManager.createNamedQuery("Parking.findAll", Parking.class).getResultList();
		double lat, lon;
		List<Parking> parkingsInRange;
		if(latitude == null || longitude == null || latitude=="" || longitude=="") {
			parkingsInRange = parkings;
		}else {
			lat = Double.parseDouble(latitude);
			lon = Double.parseDouble(longitude);
			parkingsInRange = parkings.stream()
					.filter(p -> calcularDistancia(lat, lon, p.getLatitude(), p.getLongitude()) <= radio).collect(Collectors.toList());
		}

		List<Transfer> transferParkings = new ArrayList<>();
		for (Parking p : parkingsInRange) {
			List<Spot> spots = p.getSpots();
			List<Reserve> reserves = new ArrayList<>();
			for (Spot s : spots) {
				reserves.addAll(s.getReserves());
			}
			if(reserves.size()==0){
				transferParkings.add(p.toTransfer());
			}else{
				for (Reserve r : reserves) {
					if ((r.getStartDate().isBefore(endDate) && r.getEndDate().isAfter(startDate)) ||
							(r.getStartDate().isEqual(startDate) && r.getStartTime().isBefore(endTime)) ||
							(r.getEndDate().isEqual(endDate) && r.getEndTime().isAfter(startTime))) {
						System.out.println("reservado");
					}else{
						transferParkings.add(p.toTransfer());
						break;
					}
				}
			}
			
		}

		model.addAttribute("parkings", transferParkings);
		model.addAttribute("latitude", latitude);
		model.addAttribute("longitude", longitude);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);

		return "map";
	}

	@GetMapping("/reserve/{id}")
	public String reserve(Model model,
			HttpSession session,
			@PathVariable long id,
			@RequestParam(required = false) Integer selectedSlot,
			@RequestParam(required = false) Long vehicleId,
			@RequestParam @Nullable String startDate, @RequestParam @Nullable String endDate,
			@RequestParam @Nullable String startTime, @RequestParam @Nullable String endTime) {

		Parking parking = entityManager.find(Parking.class, id);
		if (parking == null) {
			model.addAttribute("erro", "Parking no válido");
			return "error";
		}
		Parker parker = (Parker) session.getAttribute("u");
		System.out.println(parker.getId());
		List<Vehicle> vehicles = entityManager.createQuery("SELECT v FROM Vehicle v WHERE v.parker.id = :parkerId", Vehicle.class)
                                          .setParameter("parkerId", parker.getId())
                                          .getResultList();

		model.addAttribute("parking", parking.toTransfer());
		model.addAttribute("vehicles", vehicles);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("id", id);
		model.addAttribute("vehicleId", vehicleId);
		model.addAttribute("selectedSlot", selectedSlot);

		System.out.println(parking.getAddress());
		return "reserve";
	}

	@GetMapping("/confirm-select-parking/{id}")
	public String confirmSelectParking(@PathVariable long id, 
	@RequestParam Integer selectedSlot,
	@RequestParam(required = false) Long vehicleId,
	@RequestParam @Nullable String startDate, @RequestParam @Nullable String endDate,
	@RequestParam @Nullable String startTime, @RequestParam @Nullable String endTime,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addAttribute("selectedSlot", selectedSlot);
		redirectAttributes.addAttribute("startDate", startDate);
		redirectAttributes.addAttribute("endDate", endDate);
		redirectAttributes.addAttribute("startTime", startTime);
		redirectAttributes.addAttribute("endTime", endTime);
		redirectAttributes.addAttribute("id", id);
		redirectAttributes.addAttribute("vehicleId", vehicleId);

		return "redirect:/user/reserve/" + id;
	}
	
	@GetMapping("/select-parking/{id}")
	public String selectParkingView(@PathVariable long id,
			@RequestParam(required = false) Integer selectedSlot,
			@RequestParam(required = false) Long vehicleId,
			@RequestParam @Nullable LocalDate startDate, @RequestParam @Nullable LocalDate endDate,
			@RequestParam @Nullable LocalTime startTime, @RequestParam @Nullable LocalTime endTime,
			Model model) {
		List<Integer> occupiedSpots = new ArrayList<>();
		Parking parking = entityManager.find(Parking.class, id);
		if (parking != null) {
			List<Spot> spots = parking.getSpots();
			List<Reserve> reserves = new ArrayList<>();
			for (Spot s : spots) {
				reserves.addAll(s.getReserves());
			}

			for (Reserve r : reserves) {
				if ((r.getStartDate().isBefore(endDate) && r.getEndDate().isAfter(startDate)) ||
				(r.getStartDate().isEqual(startDate) && r.getStartTime().isBefore(endTime)) ||
				(r.getEndDate().isEqual(endDate) && r.getEndTime().isAfter(startTime))) {
					Integer spotId = Integer.valueOf((int) r.getSpot().getId());
					occupiedSpots.add(spotId);
				}
			}
		}
		System.out.println("Ocupadas: " + occupiedSpots);
		model.addAttribute("occupiedSpots", occupiedSpots);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("id", id);
		model.addAttribute("selectedSlot", selectedSlot);
		model.addAttribute("vehicleId", vehicleId);
		return "select-parking";
	}
	
	@PostMapping("/add-vehicule")
	@ResponseBody
	public  ResponseEntity<?> addVehicle (
		@RequestParam String brand,
		@RequestParam String model,
		@RequestParam String plate,
		@RequestParam String size

		) {
		//TODO: process POST request
		
		return  ResponseEntity.ok("{\"message\": \"Vehiculo agregado\"}");
	}
	

	@PostMapping("/confirm-reserve")
	@Transactional
	public String postReserve(
			@ModelAttribute("startDate") LocalDate startDate,
			@ModelAttribute("endDate") LocalDate endDate,
			@ModelAttribute("startTime") LocalTime startTime,
			@ModelAttribute("endTime") LocalTime endTime,
			@RequestParam("vehicleId") Long vehicleId,
			@RequestParam("parkingId") Long parkingId,
			@RequestParam("totalPrice") Double totalPrice,
			@RequestParam("selectedParkingSpot") Integer selectedParkingSpot,
			Model model,
			RedirectAttributes redirectAttributes) {

		User user = (User) model.getAttribute("u");
		if (!(user instanceof Parker parker)) {
			redirectAttributes.addFlashAttribute("error", "No eres un parker válido.");
			return "redirect:/error";
		}

		if (startDate == null || endDate == null || startTime == null || endTime == null || vehicleId == null
				|| totalPrice == null) {
			redirectAttributes.addFlashAttribute("error", "Faltan campos por rellenar");
			return "redirect:/user/reserve/" + parkingId;
		}
		if (startDate.isAfter(endDate) || (startDate.isEqual(endDate) && startTime.isAfter(endTime))) {
			redirectAttributes.addFlashAttribute("error", "La fecha de inicio no puede ser posterior a la de fin");
			return "redirect:/user/reserve/" + parkingId;
		}

		Vehicle vehicle = entityManager.find(Vehicle.class, vehicleId);
		if (vehicle == null) {
			redirectAttributes.addFlashAttribute("error", "Vehículo no válido");
			return "redirect:/user/reserve/" + parkingId;

		}
		Long spotId = selectedParkingSpot.longValue();
		Spot spot = entityManager.find(Spot.class, spotId);
		if (spot == null) {
			redirectAttributes.addFlashAttribute("error", "Plaza no válida");
			return "redirect:/user/reserve/" + parkingId;
		}

		List<Reserve> reservas = entityManager
				.createQuery("SELECT r FROM Reserve r WHERE r.spot = :spot", Reserve.class)
				.setParameter("spot", spot)
				.getResultList();
		for (Reserve r : reservas) {
			if ((r.getStartDate().isBefore(endDate) && r.getEndDate().isAfter(startDate)) ||
					(r.getStartDate().isEqual(startDate) && r.getStartTime().isBefore(endTime)) ||
					(r.getEndDate().isEqual(endDate) && r.getEndTime().isAfter(startTime))) {

				redirectAttributes.addFlashAttribute("error", "No se puede reservar esta plaza en esas fechas y horas");
				return "redirect:/user/reserve/" + parkingId;
			}
		}

		Reserve reserve = new Reserve();
		reserve.setStartDate(startDate);
		reserve.setEndDate(endDate);
		reserve.setStartTime(startTime);
		reserve.setEndTime(endTime);
		reserve.setPrice(totalPrice);
		reserve.setState(Reserve.State.PENDING);
		reserve.setSpot(spot);
		reserve.setVehicle(vehicle);

		try {
			entityManager.persist(reserve);
			entityManager.flush();
			entityManager.clear();
			model.addAttribute("success", "Reserva realizada con éxito");
		} catch (Exception e) {
			model.addAttribute("error", "Hubo un error al guardar la reserva: " + e.getMessage());
			return "redirect:/error";
		}

		return myReserves(model);
	}

	@GetMapping("/modify-reserve")
	public String modifyReserve(Model model) {
		return "modify-reserve";
	}

	@GetMapping("/my-reserves")
	public String myReserves(Model model) {

		User user = (User) model.getAttribute("u");
		if (user instanceof Parker) {
			Parker parker = (Parker) user;
			List<Reserve> reservas = entityManager
					.createQuery("SELECT r FROM Reserve r WHERE r.parker = :parker", Reserve.class)
					.setParameter("parker", parker)
					.getResultList();

			model.addAttribute("reservas", reservas);
		} else {
			model.addAttribute("error", "No eres un parker válido.");
			return "error";
		}

		return "my-reserves";
	}

	/**
	 * Exception to use when denying access to unauthorized users.
	 * 
	 * In general, admins are always authorized, but users cannot modify
	 * each other's profiles.
	 */
	@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "No eres administrador, y éste no es tu perfil") // 403
	public static class NoEsTuPerfilException extends RuntimeException {
	}

	/**
	 * Encodes a password, so that it can be saved for future checking. Notice
	 * that encoding the same password multiple times will yield different
	 * encodings, since encodings contain a randomly-generated salt.
	 * 
	 * @param rawPassword to encode
	 * @return the encoded password (typically a 60-character string)
	 *         for example, a possible encoding of "test" is
	 *         {bcrypt}$2y$12$XCKz0zjXAP6hsFyVc8MucOzx6ER6IsC1qo5zQbclxhddR1t6SfrHm
	 */
	public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

	/**
	 * Generates random tokens. From https://stackoverflow.com/a/44227131/15472
	 * 
	 * @param byteLength
	 * @return
	 */
	public static String generateRandomBase64Token(int byteLength) {
		SecureRandom secureRandom = new SecureRandom();
		byte[] token = new byte[byteLength];
		secureRandom.nextBytes(token);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(token); // base64 encoding
	}

	/**
	 * Landing page for a user profile
	 */
	@GetMapping("{id}")
	public String index(@PathVariable long id, Model model, HttpSession session) {
		User target = entityManager.find(User.class, id);
		// Parker parker = entityManager.find(Parker.class, id);
		model.addAttribute("user", target);
		return "user";
	}

	/**
	 * Alter or create a user
	 */
	@PostMapping("/{id}")
	@Transactional
	public String postUser(
			HttpServletResponse response,
			@PathVariable long id,
			@ModelAttribute User edited,
			@RequestParam(required = false) String pass2,
			Model model, HttpSession session) throws IOException {

		User requester = (User) session.getAttribute("u");
		User target = null;
		if (id == -1 && requester.hasRole(Role.ADMIN)) {
			// create new user with random password
			target = new User();
			target.setPassword(encodePassword(generateRandomBase64Token(12)));
			target.setEnabled(true);
			entityManager.persist(target);
			entityManager.flush(); // forces DB to add user & assign valid id
			id = target.getId(); // retrieve assigned id from DB
		}

		// retrieve requested user
		target = entityManager.find(User.class, id);
		model.addAttribute("user", target);

		if (requester.getId() != target.getId() &&
				!requester.hasRole(Role.ADMIN)) {
			throw new NoEsTuPerfilException();
		}

		if (edited.getPassword() != null) {
			if (!edited.getPassword().equals(pass2)) {
				log.warn("Passwords do not match - returning to user form");
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				model.addAttribute("user", target);
				return "user";
			} else {
				// save encoded version of password
				target.setPassword(encodePassword(edited.getPassword()));
			}
		}
		target.setUsername(edited.getUsername());
		// target.setFirstName(edited.getFirstName());
		// target.setLastName(edited.getLastName());

		// update user session so that changes are persisted in the session, too
		if (requester.getId() == target.getId()) {
			session.setAttribute("u", target);
		}

		return "user";
	}

	/**
	 * Returns the default profile pic
	 * 
	 * @return
	 */
	private static InputStream defaultPic() {
		return new BufferedInputStream(Objects.requireNonNull(
				UserController.class.getClassLoader().getResourceAsStream(
						"static/img/default-pic.jpg")));
	}

	/**
	 * Downloads a profile pic for a user id
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@GetMapping("{id}/pic")
	public StreamingResponseBody getPic(@PathVariable long id) throws IOException {
		File f = localData.getFile("user", "" + id + ".jpg");
		InputStream in = new BufferedInputStream(f.exists() ? new FileInputStream(f) : UserController.defaultPic());
		return os -> FileCopyUtils.copy(in, os);
	}

	/**
	 * Uploads a profile pic for a user id
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	@PostMapping("{id}/pic")
	@ResponseBody
	public String setPic(@RequestParam("photo") MultipartFile photo, @PathVariable long id,
			HttpServletResponse response, HttpSession session, Model model) throws IOException {

		User target = entityManager.find(User.class, id);
		model.addAttribute("user", target);

		// check permissions
		User requester = (User) session.getAttribute("u");
		if (requester.getId() != target.getId() &&
				!requester.hasRole(Role.ADMIN)) {
			throw new NoEsTuPerfilException();
		}

		log.info("Updating photo for user {}", id);
		File f = localData.getFile("user", "" + id + ".jpg");
		if (photo.isEmpty()) {
			log.info("failed to upload photo: emtpy file?");
		} else {
			try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f))) {
				byte[] bytes = photo.getBytes();
				stream.write(bytes);
				log.info("Uploaded photo for {} into {}!", id, f.getAbsolutePath());
			} catch (Exception e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				log.warn("Error uploading " + id + " ", e);
			}
		}
		return "{\"status\":\"photo uploaded correctly\"}";
	}

	@GetMapping("error")
	public String error(Model model, HttpSession session, HttpServletRequest request) {
		model.addAttribute("sess", session);
		model.addAttribute("req", request);
		return "error";
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

	/**
	 * Posts a message to a user.
	 * 
	 * @param id of target user (source user is from ID)
	 * @param o  JSON-ized message, similar to {"message": "text goes here"}
	 * @throws JsonProcessingException
	 */
	@PostMapping("/{id}/msg")
	@ResponseBody
	@Transactional
	public String postMsg(@PathVariable long id,
			@RequestBody JsonNode o, Model model, HttpSession session)
			throws JsonProcessingException {

		String text = o.get("message").asText();
		User u = entityManager.find(User.class, id);
		User sender = entityManager.find(
				User.class, ((User) session.getAttribute("u")).getId());
		model.addAttribute("user", u);

		// construye mensaje, lo guarda en BD
		Message m = new Message();
		m.setRecipient(u);
		m.setSender(sender);
		m.setDateSent(LocalDateTime.now());
		m.setText(text);
		entityManager.persist(m);
		entityManager.flush(); // to get Id before commit

		ObjectMapper mapper = new ObjectMapper();
		/*
		 * // construye json: método manual
		 * ObjectNode rootNode = mapper.createObjectNode();
		 * rootNode.put("from", sender.getUsername());
		 * rootNode.put("to", u.getUsername());
		 * rootNode.put("text", text);
		 * rootNode.put("id", m.getId());
		 * String json = mapper.writeValueAsString(rootNode);
		 */
		// persiste objeto a json usando Jackson
		String json = mapper.writeValueAsString(m.toTransfer());

		log.info("Sending a message to {} with contents '{}'", id, json);

		messagingTemplate.convertAndSend("/user/" + u.getUsername() + "/queue/updates", json);
		return "{\"result\": \"message sent.\"}";
	}
}
