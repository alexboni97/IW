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
		@NamedQuery(name = "negocio.Parker.findBytelephone", query = "select p from Parker p where :telephone = p.telephone "),
		@NamedQuery(name = "negocio.Parker.findByemail", query = "select p from Parker p where :email = p.email "),
		@NamedQuery(name = "negocio.Parker.findByvehicles", query = "select p from Parker p where :vehicles MEMBER OF p.vehicles "),
		@NamedQuery(name = "negocio.Parker.findByreserves", query = "select p from Parker p where :reserves MEMBER OF p.reserves ") })
public class Parker extends User {
	
	private String firstName;

	private String secondName;

	@Column(nullable = false, unique = true)
	private String DNI;

	private int telephone;

	private String email;

	@OneToMany
	@JoinColumn(name = "parker_id")
	private List<Vehicle> vehicles = new ArrayList<>();

	@OneToMany
	@JoinColumn(name = "parker_id")
	private List<Reserve> reserves = new ArrayList<>();

	@Getter
    public static class Transfer extends User.Transfer { 

		private String firstName;
		private String secondName;
		private String DNI;
		private int telephone;
		private String email;
		private int totalVehicles;
		private int totalReserves;

		public Transfer(long id, boolean enabled, String username, String password, int totalReceived, int totalSent,  
		String firstName, String secondName, String DNI, int telephone, String email, int totalVehicles, int totalReserves,
		double wallet, String role) {
            super(id, enabled, username, totalReceived, totalSent, wallet, role);
            this.firstName = firstName;
			this.secondName = secondName;
			this.DNI = DNI;
			this.telephone = telephone;
			this.email = email;
			this.totalVehicles = totalVehicles;
			this.totalReserves = totalReserves;
        }
    }

	@Override
    public Transfer toTransfer() {
		return new Transfer(this.getId(), this.isEnabled(), this.getUsername(), this.getPassword(), this.getReceived().size(), this.getSent().size(), 
		this.firstName, this.secondName, this.DNI, this.telephone, this.email, this.vehicles.size(), this.reserves.size(), this.getWallet(), this.getRole().name());
	}
	
	@Override
	public String toString() {
		return toTransfer().toString();
	}
}