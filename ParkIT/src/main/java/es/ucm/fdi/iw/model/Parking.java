package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A parking lot managed by an enterprise.
 */
@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Parking.findById", query = "SELECT p FROM Parking p WHERE p.id = :id"),
        @NamedQuery(name = "Parking.findByEnabled", query = "SELECT p FROM Parking p WHERE p.enabled = :enabled"),
        @NamedQuery(name = "Parking.findByName", query = "SELECT p FROM Parking p WHERE p.name = :name"),
        @NamedQuery(name = "Parking.findByAddress", query = "SELECT p FROM Parking p WHERE p.address = :address"),
        @NamedQuery(name = "Parking.findByTelephone", query = "SELECT p FROM Parking p WHERE p.telephone = :telephone"),
        @NamedQuery(name = "Parking.findByEmail", query = "SELECT p FROM Parking p WHERE p.email = :email"),
        @NamedQuery(name = "Parking.findByFeePerHour", query = "SELECT p FROM Parking p WHERE p.feePerHour = :feePerHour"),
        @NamedQuery(name = "Parking.findByOpeningTime", query = "SELECT p FROM Parking p WHERE p.openingTime = :openingTime"),
        @NamedQuery(name = "Parking.findByClosingTime", query = "SELECT p FROM Parking p WHERE p.closingTime = :closingTime"),
        @NamedQuery(name = "Parking.findByEnterprise", query = "SELECT p FROM Parking p WHERE p.enterprise = :enterprise"),
        @NamedQuery(name = "Parking.findByCoords", query = "SELECT p FROM Parking p WHERE p.coords = :coords")
})
public class Parking implements Transferable<Parking.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean enabled;

    private String name;

    private String address;

    private int telephone;

    private String email;

    private double feePerHour;

    private LocalTime openingTime;

    private LocalTime closingTime;
    
    private String coords;

    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;

    @OneToMany(mappedBy = "parking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Spot> spots  = new ArrayList<>();

	@Getter
    @AllArgsConstructor
    public static class Transfer {
		private long id;
        private boolean enabled;
		private String name;
		private String address;
		private int telephone;
		private String email;
		private double feePerHour;
		private LocalTime openingTime;
		private LocalTime closingTime;
		private String coords;
		private long enterpriseId;
		private int totalSpots;
    }

	@Override
    public Transfer toTransfer() {
		return new Transfer(this.id, this.enabled, this.name, this.address, this.telephone, this.email, 
        this.feePerHour, this.openingTime, this.closingTime, this.coords, this.enterprise.getId(), this.spots.size());
	}
	
	@Override
	public String toString() {
		return toTransfer().toString();
	}
}
