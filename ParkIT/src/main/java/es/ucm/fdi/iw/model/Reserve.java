package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A parking reservation in the system.
 */
@Entity
@Data
@NoArgsConstructor
@NamedQueries({
    @NamedQuery(name = "Reserve.findById", query = "SELECT r FROM Reserve r WHERE r.id = :id"),
    @NamedQuery(name = "Reserve.findByState", query = "SELECT r FROM Reserve r WHERE r.state = :state"),
    @NamedQuery(name = "Reserve.findByStartDate", query = "SELECT r FROM Reserve r WHERE r.startDate = :startDate"),
    @NamedQuery(name = "Reserve.findByEndDate", query = "SELECT r FROM Reserve r WHERE r.endDate = :endDate"),
    @NamedQuery(name = "Reserve.findByPrice", query = "SELECT r FROM Reserve r WHERE r.price = :price"),
    @NamedQuery(name = "Reserve.findByComments", query = "SELECT r FROM Reserve r WHERE r.comments = :comments"),
    @NamedQuery(name = "Reserve.findByParker", query = "SELECT r FROM Reserve r WHERE r.parker = :parker"),
    @NamedQuery(name = "Reserve.findBySpot", query = "SELECT r FROM Reserve r WHERE r.spot = :spot")
})
public class Reserve implements Transferable<Reserve.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;
	
    public enum State {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    @Enumerated(EnumType.STRING)
    private State state;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private double price;

    private String comments;

    @ManyToOne
    @JoinColumn(name = "spot_id")
    private Spot spot;
    
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    
    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String state;
        private LocalDate startDate;
        private LocalDate endDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private double price;
        private String comments;
        private long spotId;
        private long vehicleId;
        private String spotAddress;
        }

    @Override
    public Transfer toTransfer() {
        return new Transfer(this.id, this.state.name(), this.startDate, this.endDate, this.startTime, this.endTime, this.price, this.comments, this.spot.getId(), this.vehicle.getId(), this.spot.getAddress());
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}
