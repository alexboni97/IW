package es.ucm.fdi.iw.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * An authorized user of the system.
 */
@Entity
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
@NamedQueries({
		@NamedQuery(name = "Enterprise.findByname", query = "select obj from Enterprise obj where :name = obj.name "),
		@NamedQuery(name = "Enterprise.findByCIF", query = "select obj from Enterprise obj where :CIF = obj.CIF "),
		@NamedQuery(name = "Enterprise.findByparkings", query = "select obj from Enterprise obj where :parkings MEMBER OF obj.parkings ") })
public class Enterprise extends User {

	private String name;

	@Column(nullable = false, unique = true)
	private String CIF;

	@OneToMany
	@JoinColumn(name = "enterprise_id")
	private List<Parking> parkings = new ArrayList<>();

	@Getter
	public static class Transfer extends User.Transfer { 
		private String name;
		private String CIF;
		private int totalParkings;

		public Transfer(long id, boolean enabled, String username, int totalReceived, int totalSent, double wallet, int telephone, String email, String role, String name, String CIF, int totalParkings) {
			super(id, enabled, username, totalReceived, totalSent, wallet, telephone, email, role);
			this.name = name;
			this.CIF = CIF;
			this.totalParkings = totalParkings;
		}
	}

	@Override
	public Transfer toTransfer() { 
		return new Transfer(this.getId(), this.isEnabled(), this.getUsername(), this.getReceived().size(), this.getSent().size(), this.getWallet(), this.getTelephone(), this.getEmail(), this.getRole().toString(), this.name, this.CIF, this.parkings.size());
	}
	
	@Override
	public String toString() {
		return toTransfer().toString();
	}
}