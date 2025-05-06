package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A parking spot available in the system.
 */
@Entity
@Data
@NoArgsConstructor
@NamedQueries({
    @NamedQuery(name = "Spot.findById", query = "SELECT s FROM Spot s WHERE s.id = :id"),
    @NamedQuery(name = "Spot.findByEnabled", query = "SELECT s FROM Spot s WHERE s.enabled = :enabled"),
    @NamedQuery(name = "Spot.findBySize", query = "SELECT s FROM Spot s WHERE s.size = :size"),
    @NamedQuery(name = "Spot.findByParking", query = "SELECT s FROM Spot s WHERE s.parking = :parking"),
    @NamedQuery(name = "Spot.findByReserves", query = "SELECT s FROM Spot s WHERE :reserves MEMBER OF s.reserves")
})
public class Spot implements Transferable<Spot.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean enabled;

    private String size; // S, M, L, XL

    private double x;
    private double y;

    private boolean horizontal;

    @ManyToOne
    @JoinColumn(name = "parking_id")
    private Parking parking;

    @OneToMany
    @JoinColumn(name = "spot_id")
    private List<Reserve> reserves = new ArrayList<>();

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private boolean enabled;
        private String size;
        private double x;
        private double y;
        private boolean horizontal;
        private long parkingId;
        private int totalReserves;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(this.id, this.enabled, this.size, this.x, this.y, this.horizontal, this.parking.getId(), this.reserves.size());
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }

    public String getAddress(){
        return this.parking.getAddress();
    }
}
