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
    @NamedQuery(name = "Spot.findByType", query = "SELECT s FROM Spot s WHERE s.type = :type"),
    @NamedQuery(name = "Spot.findBySize", query = "SELECT s FROM Spot s WHERE s.size = :size"),
    @NamedQuery(name = "Spot.findByCoords", query = "SELECT s FROM Spot s WHERE s.coords = :coords"),
    @NamedQuery(name = "Spot.findByParking", query = "SELECT s FROM Spot s WHERE s.parking = :parking"),
    @NamedQuery(name = "Spot.findByReserves", query = "SELECT s FROM Spot s WHERE :reserves MEMBER OF s.reserves")
})
public class Spot implements Transferable<Spot.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    private boolean enabled;
    
    public enum Type {
        COMPACT, LARGE, HANDICAPPED, ELECTRIC
    }

    @Enumerated(EnumType.STRING)
    private Type type;

    private String size;

    private String coords;

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
        private String type;
        private String size;
        private String coords;
        private long parkingId;
        private int totalReserves;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(this.id, this.enabled, this.type.name(), this.size, this.coords, this.parking.getId(), this.reserves.size());
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
