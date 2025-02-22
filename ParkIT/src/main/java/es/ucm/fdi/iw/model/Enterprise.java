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
		@NamedQuery(name = "Enterprise.findByaccountNumber", query = "select obj from Enterprise obj where :accountNumber = obj.accountNumber "),
		@NamedQuery(name = "Enterprise.findBytelephone", query = "select obj from Enterprise obj where :telephone = obj.telephone "),
		@NamedQuery(name = "Enterprise.findByparkings", query = "select obj from Enterprise obj where :parkings MEMBER OF obj.parkings ") })
public class Enterprise extends User {

	private String name;

	@Column(nullable = false, unique = true)
	private String CIF;

	private String accountNumber;

	private int telephone;

	@OneToMany(mappedBy = "enterprise")
	private List<Parking> parkings  = new ArrayList<>();

	@Getter
    public static class Transfer extends User.Transfer { 
		private String name;
		private String CIF;
		private String accountNumber;
		private int telephone;
		private int totalParkings;

		public Transfer(long id, boolean enabled, String username, String password , String name, String CIF, 
		String accountNumber, int telephone, int totalParkings,int totalReceived,int totalSent) {
			super(id, enabled, username,totalReceived, totalSent);
			this.name = name;
			this.CIF = CIF;
			this.accountNumber = accountNumber;
			this.telephone = telephone;
			this.totalParkings = totalParkings;
		}
		
    }

	@Override
    public Transfer toTransfer() { 
		return new Transfer(this.getId(), this.isEnabled(), this.getUsername(), this.getPassword(),name, this.CIF, 
		this.accountNumber, this.telephone, this.parkings.size(), this.getReceived().size(), this.getSent().size());
	}
	
	@Override
	public String toString() {
		return toTransfer().toString();
	}
}