package es.ucm.fdi.iw;

import java.time.LocalDate;
import java.time.LocalTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import es.ucm.fdi.iw.model.*;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletContext;
import jakarta.transaction.Transactional;

/**
 * This code will execute when the application first starts.
 * 
 * @author mfreire
 */
@Component
public class StartupConfig {

    private static final Logger log = LogManager.getLogger(StartupConfig.class);

    @Autowired
    private Environment env;

    @Autowired
    private ServletContext context;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener(ContextRefreshedEvent.class)
    @Transactional
    public void contextRefreshedEvent() {
        String debugProperty = env.getProperty("es.ucm.fdi.debug");
        context.setAttribute("debug", debugProperty != null
                && Boolean.parseBoolean(debugProperty.toLowerCase()));
        log.info("Setting global debug property to {}",
                context.getAttribute("debug"));

    }

    
}