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

import io.micrometer.common.lang.Nullable;

/**
 * A request managed by an admin.
 */
@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Request.findAll", query = "SELECT p FROM Request p"),
        @NamedQuery(name = "Request.findById", query = "SELECT p FROM Request p WHERE p.id = :id"),
        @NamedQuery(name = "Request.findByEnabled", query = "SELECT p FROM Request p WHERE p.enabled = :enabled"),
        @NamedQuery(name = "Request.findByName", query = "SELECT p FROM Request p WHERE p.name = :name"),
        @NamedQuery(name = "Request.findByAddress", query = "SELECT p FROM Request p WHERE p.address = :address"),
        @NamedQuery(name = "Request.findByTelephone", query = "SELECT p FROM Request p WHERE p.telephone = :telephone"),
        @NamedQuery(name = "Request.findByEmail", query = "SELECT p FROM Request p WHERE p.email = :email"),
        @NamedQuery(name = "Request.findByFeePerHour", query = "SELECT p FROM Request p WHERE p.feePerHour = :feePerHour"),
        @NamedQuery(name = "Request.findByOpeningTime", query = "SELECT p FROM Request p WHERE p.openingTime = :openingTime"),
        @NamedQuery(name = "Request.findByClosingTime", query = "SELECT p FROM Request p WHERE p.closingTime = :closingTime"),
        @NamedQuery(name = "Request.findByEnterprise", query = "SELECT p FROM Request p WHERE p.enterprise = :enterprise"),
        @NamedQuery(name = "Request.findByState", query = "SELECT p FROM Request p WHERE p.state = :state"),
        @NamedQuery(name = "Request.findByEnterpriseId", query = "SELECT p FROM Request p WHERE p.enterprise.id = :enterpriseId"),
        @NamedQuery(name = "Request.findByEnabledAndType", query = "SELECT p FROM Request p WHERE p.enabled = :enabled AND p.type = :type"),
})
public class Request implements Transferable<Request.Transfer> {

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

    @Nullable
    private long idParking;

    private int totalSpots;

    // AÑADIR --> solicitudes de añadir
    // BORRAR --> solicitudes de borrar
    private String type;

    private String state;

    @ManyToOne
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;

    //Allargs permite que se le pasen todos los atributos de la clase al constructor. Si no le pasas todo peta. Y si alguno es null-> peta también...
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
        private long idParking;
        private String state;


        public Transfer(Request p) {
            this.id = p.getId();
            this.enabled = p.isEnabled();
            this.name = p.getName();
            this.address = p.getAddress();
            this.cp = p.getCp();
            this.city = p.getCity();
            this.country = p.getCountry();
            this.telephone = p.getTelephone();
            this.email = p.getEmail();
            this.feePerHour = p.getFeePerHour();
            this.openingTime = p.getOpeningTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            this.closingTime = p.getClosingTime().format(DateTimeFormatter.ofPattern("HH:mm"));
            this.enterpriseId = p.getEnterprise().getId();
            this.idParking = p.getIdParking();
            this.state = p.getState();
            this.totalSpots = p.getTotalSpots();
            this.longitude = p.getLongitude();
            this.latitude = p.getLatitude();
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
