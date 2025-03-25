package es.ucm.fdi.iw.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ucm.fdi.iw.model.Parking;
import es.ucm.fdi.iw.model.Request;
import es.ucm.fdi.iw.model.Spot;
import es.ucm.fdi.iw.model.User;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Site administration.
 *
 * Access to this end-point is authenticated - see SecurityConfig
 */
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private EntityManager entityManager;

    @ModelAttribute
    public void populateModel(HttpSession session, Model model) {
        for (String name : new String[] { "u", "url", "ws" }) {
            model.addAttribute(name, session.getAttribute(name));
        }
    }

    private static final Logger log = LogManager.getLogger(AdminController.class);

    @GetMapping("/")
    public String index(Model model) {
        log.info("Admin acaba de entrar");
        return "admin";
    }

    @GetMapping("/request-add")
    public String adminRequestAdd(Model model) {
        List<Request> requests = entityManager
                .createNamedQuery("Request.findByEnabledAndType", Request.class)
                .setParameter("enabled", true)
                .setParameter("type", "AÑADIR")
                .getResultList();

        model.addAttribute("requests", requests);

        return "request-add";
    }

    @PostMapping("/guardarParking/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> guardarParking(@PathVariable Long id) {
        try {
            Request request = entityManager.createNamedQuery("Request.findById", Request.class)
                    .setParameter("id", id)
                    .getSingleResult();
            if (request == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se ha encontrado la petición");
            }

            Parking p = new Parking();
            p.setName(request.getName());
            p.setAddress(request.getAddress());
            p.setCity(request.getCity());
            p.setCountry(request.getCountry());
            p.setCp(request.getCp());
            p.setTelephone(request.getTelephone());
            p.setEmail(request.getEmail());
            p.setEnterprise(request.getEnterprise());
            p.setOpeningTime(request.getOpeningTime());
            p.setClosingTime(request.getClosingTime());
            p.setFeePerHour(request.getFeePerHour());
            p.setEnabled(true);
            p.setLatitude(request.getLatitude());
            p.setLongitude(request.getLongitude());
            p.setWidth(20.0); // Example value
            p.setHeight(10.0); // Example value
            p.setEntryX(1.0); // Example value
            p.setEntryY(1.0); // Example value
            p.setExitX(2.0); // Example value
            p.setExitY(2.0); // Example value
            // p.setSpots(new ArrayList<>(Collections.nCopies(request.getTotalSpots(), new
            // Spot())));
            entityManager.persist(p);

            request.setEnabled(false);
            entityManager.persist(request);

            return ResponseEntity.ok("Parking añadido correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al añadir el parking: " + e.getMessage());
        }
    }

    @PostMapping("/eliminarParking/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> eliminarParking(@PathVariable Long id) {
        try {
            Request request = entityManager.createNamedQuery("Request.findById", Request.class)
                    .setParameter("id", id)
                    .getSingleResult();
            if (request == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se ha encontrado la petición");
            }

            Parking parking = entityManager.createNamedQuery("Parking.findById", Parking.class)
                    .setParameter("id", request.getIdParking())
                    .getSingleResult();
            if (parking == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se ha encontrado el parking");
            }

            parking.setEnabled(false);
            entityManager.persist(parking);
            request.setEnabled(false);
            entityManager.persist(request);

            return ResponseEntity.ok("Parking eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar el parking: " + e.getMessage());
        }
    }

    @DeleteMapping("/eliminarRequest/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> eliminarRequest(@PathVariable Long id) {
        try {
            Request request = entityManager.createNamedQuery("Request.findById", Request.class)
                    .setParameter("id", id)
                    .getSingleResult();
            if (request == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se ha encontrado la petición");
            }

            request.setEnabled(false);
            entityManager.persist(request);

            return ResponseEntity.ok("Request eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar la request: " + e.getMessage());
        }
    }

    @GetMapping("/request-delete")
    public String adminRequestDelete(Model model) {
        List<Request> requests = entityManager
                .createNamedQuery("Request.findByEnabledAndType", Request.class)
                .setParameter("enabled", true)
                .setParameter("type", "ELIMINAR")
                .getResultList();

        model.addAttribute("requests", requests);

        return "request-delete";
    }
}
