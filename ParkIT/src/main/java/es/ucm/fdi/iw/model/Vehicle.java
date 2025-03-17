package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * A vehicle registered in the system.
 */
@Entity
@Data
@NoArgsConstructor
@NamedQueries({
	@NamedQuery(name = "Vehicle.findByid", query = "select v from Vehicle v where :id = v.id "),
	@NamedQuery(name = "Vehicle.findByenabled", query = "select v from Vehicle v where :enabled = v.enabled "),
	@NamedQuery(name = "Vehicle.findByplate", query = "select v from Vehicle v where :plate = v.plate "),
	@NamedQuery(name = "Vehicle.findBybrand", query = "select v from Vehicle v where :brand = v.brand "),
	@NamedQuery(name = "Vehicle.findBymodel", query = "select v from Vehicle v where :model = v.model "),
	@NamedQuery(name = "Vehicle.findBysize", query = "select v from Vehicle v where :size = v.size "),
	@NamedQuery(name = "Vehicle.findByparker", query = "select v from Vehicle v where :parker = v.parker ")
})
public class Vehicle  implements Transferable<Vehicle.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;

	private boolean enabled;

	@Column(nullable = false, unique = true)
	private String plate;

	private String brand;

	private String model;
	
	private String size; // S, M, L, XL

	@ManyToOne
	@JoinColumn(name = "parker_id")
	private Parker parker;

	@Getter
    @AllArgsConstructor
    public static class Transfer {
		private long id;
        private String plate;
		private String brand;
		private String model;
		private String size;
		private boolean enabled;
    }

	@Override
    public Transfer toTransfer() {
		return new Transfer(this.id, this.plate, this.brand, this.model, this.size, this.enabled);
	}

	@Override
	public String toString() {
		return toTransfer().toString();
	}
}