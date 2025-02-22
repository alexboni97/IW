package es.ucm.fdi.iw.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * An authorized admin user of the system.
 */
@Entity
@Data
@NoArgsConstructor
public class Admin extends User {

}