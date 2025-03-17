package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * A parking lot managed by an enterprise.
 */
@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Parking.findAll", query = "SELECT p FROM Parking p"),
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
        @NamedQuery(name = "Parking.findByLongitude", query = "SELECT p FROM Parking p WHERE p.longitude = :longitude"),
        @NamedQuery(name = "Parking.findByLatitude", query = "SELECT p FROM Parking p WHERE p.latitude = :latitude")
})
public class Parking implements Transferable<Parking.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean enabled;

    private String name;

    private String address;

    private int cp;

    private String city;

    private String country;

    private int telephone;

    private String email;

    private double feePerHour;

    private LocalTime openingTime;

    private LocalTime closingTime;

    private double longitude;

    private double latitude;

    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;

    // @OneToMany(mappedBy = "parking", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany
    @JoinColumn(name = "parking_id")
    private List<Spot> spots  = new ArrayList<>();

	@Getter
    @AllArgsConstructor
    public static class Transfer {
		private long id;
        private boolean enabled;
		private String name;
		private String address;
        private int cp;
        private String city;
        private String country;
		private int telephone;
		private String email;
		private double feePerHour;
		private String openingTime;
		private String closingTime;
		private double longitude;
        private double latitude;
		private long enterpriseId;
		private int totalSpots;

        public Transfer(Parking p) {
            this.id = p.getId();
            this.enabled = p.isEnabled();
            this.name = p.getName();
            this.address = p.getAddress();
            this.telephone = p.getTelephone();
            this.email = p.getEmail();
            this.feePerHour = p.getFeePerHour();
            this.openingTime = p.getOpeningTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            this.closingTime = p.getClosingTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            this.longitude = p.getLongitude();
            this.latitude = p.getLatitude();
            this.enterpriseId = p.getEnterprise().getId();
            this.totalSpots = p.getSpots().size();
            this.cp = p.getCp();
            this.city = p.getCity();
            this.country = p.getCountry();
        }
    }

	@Override
    public Transfer toTransfer() {
		return new Transfer(this);
	}
	
	@Override
	public String toString() {
		return toTransfer().toString();
	}
}
