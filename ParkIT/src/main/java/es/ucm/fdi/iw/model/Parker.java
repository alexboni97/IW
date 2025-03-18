package es.ucm.fdi.iw.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * An authorized particular user of the system.
 */
@Entity
@Data
@NoArgsConstructor
@NamedQueries({
		@NamedQuery(name = "negocio.Parker.findByfirstName", query = "select p from Parker p where :firstName = p.firstName "),
		@NamedQuery(name = "negocio.Parker.findBysecondName", query = "select p from Parker p where :secondName = p.secondName "),
		@NamedQuery(name = "negocio.Parker.findByDNI", query = "select p from Parker p where :DNI = p.DNI "),
		@NamedQuery(name = "negocio.Parker.findByvehicles", query = "select p from Parker p where :vehicles MEMBER OF p.vehicles ") 
})
public class Parker extends User {
	
	@Column(nullable = false, unique = true)
	private String DNI;
	
	private String firstName;
	
	private String secondName;

	@OneToMany
	@JoinColumn(name = "parker_id")
	private List<Vehicle> vehicles = new ArrayList<>();

	@Getter
    public static class Transfer extends User.Transfer { 

		private String firstName;
		private String secondName;
		private String DNI;
		private int totalVehicles;

		public Transfer(long id, boolean enabled, String username, int totalReceived, int totalSent, double wallet, int telephone, String email, String role,
						String DNI, String firstName, String secondName, int totalVehicles) {
			super(id, enabled, username, totalReceived, totalSent, wallet, telephone, email, role);
			this.DNI = DNI;
			this.firstName = firstName;
			this.secondName = secondName;
			this.totalVehicles = totalVehicles;
		}
	}

	@Override
	public Transfer toTransfer() {
		return new Transfer(this.getId(), this.isEnabled(), this.getUsername(), this.getReceived().size(), this.getSent().size(), 
		this.getWallet(), this.getTelephone(), this.getEmail(), this.getRole().name(),
		this.DNI, this.firstName, this.secondName, this.vehicles.size());
	}
	
	@Override
	public String toString() {
		return toTransfer().toString();
	}
}