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
		@NamedQuery(name = "Enterprise.findBytelephone", query = "select obj from Enterprise obj where :telephone = obj.telephone "),
		@NamedQuery(name = "Enterprise.findByparkings", query = "select obj from Enterprise obj where :parkings MEMBER OF obj.parkings ") })
public class Enterprise extends User {

	private String name;

	@Column(nullable = false, unique = true)
	private String CIF;

	private int telephone;

	@OneToMany
	@JoinColumn(name = "enterprise_id")
	private List<Parking> parkings  = new ArrayList<>();

	@Getter
    public static class Transfer extends User.Transfer { 
		private String name;
		private String CIF;
		private int telephone;
		private int totalParkings;

		public Transfer(long id, boolean enabled, String username, String password , String name, String CIF, int telephone, int totalParkings,int totalReceived,int totalSent, double wallet, String role) {
			super(id, enabled, username,totalReceived, totalSent, wallet, role);
			this.name = name;
			this.CIF = CIF;
			this.telephone = telephone;
			this.totalParkings = totalParkings;
		}
		
    }

	@Override
    public Transfer toTransfer() { 
		return new Transfer(this.getId(), this.isEnabled(), this.getUsername(), this.getPassword(),name, this.CIF, this.telephone, this.parkings.size(), this.getReceived().size(), this.getSent().size(), this.getWallet(), this.getRole().toString());
	}
	
	@Override
	public String toString() {
		return toTransfer().toString();
	}
}