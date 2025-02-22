package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
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
	
	private String DNI;

	private int telephone;

	private String email;

	@OneToMany(mappedBy = "parker")
	private List<Vehicle> vehicles = new ArrayList<>();

	@OneToMany(mappedBy = "parker")
	private List<Reserve> reserves = new ArrayList<>();

	@Getter
	//TODO: preguntar si se puede hacer con @AllArgsConstructor de alguna manera
    public static class Transfer extends User.Transfer { 
		public Transfer(long id, boolean enabled, String username, String password , String firstName, String secondName, String DNI, int telephone, String email, int totalVehicles, int totalReserves) {
			super(totalReserves, email, totalReserves, totalReserves); //TODO: sustituir cunado esté por super(id, enabled, username, password);
			this.firstName = firstName;
			this.secondName = secondName;
			this.DNI = DNI;
			this.telephone = telephone;
			this.email = email;
			this.totalVehicles = totalVehicles;
			this.totalReserves = totalReserves;
		}
		
		private String firstName;
		private String secondName;
		private String DNI;
		private int telephone;
		private String email;
		private int totalVehicles;
		private int totalReserves;
    }

	@Override
    public User.Transfer toTransfer() { //FIXME: cuando se arregle user
		return null; //new Transfer(getId(), isEnabled(), getUsername(), getPassword(), firstName, secondName, DNI, telephone, email, vehicles.size(), reserves.size());
	}
	
	@Override
	public String toString() {
		return toTransfer().toString();
	}
}