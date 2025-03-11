package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;

/**
 * An authorized user of the system.
 */
@Entity
@Data
@NoArgsConstructor
@NamedQueries({
    @NamedQuery(name="User.byUsername", query="SELECT u FROM User u WHERE u.username = :username AND u.enabled = TRUE"),
    @NamedQuery(name="User.hasUsername", query="SELECT COUNT(u) FROM User u WHERE u.username = :username"),
    @NamedQuery(name="User.all", query="SELECT u FROM User u")
})
@Table(name="IWUser")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Transferable<User.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
	private long id;

    private boolean enabled;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private double wallet;

    // private String firstName; //TODO: eliminar en algun momento
    // private String lastName; 

    public enum Role {
        USER,			// normal users 
        ADMIN,          // admin users
        ENTERPRISE,     // enterprise users
    }

    @Enumerated(EnumType.STRING) // TODO: invento JP, si no va quitar
    private Role role; // split by ',' to separate roles

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "sender_id")
	private List<Message> sent = new ArrayList<>();
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "recipient_id")	
	private List<Message> received = new ArrayList<>();		

    /**
     * Checks whether this user has a given role.
     * @param role to check
     * @return true iff this user has that role.
     public boolean hasRole(Role role) {
        String roleName = role.name();
        return Arrays.asList(roles.split(",")).contains(roleName);
    }
    */
    public boolean hasRole(Role role) { // TODO: invento JP, si no va cambiar como estaba
        return this.role == role;
    }


    @Getter
    @AllArgsConstructor
    public static class Transfer {
		private long id;
        private boolean enabled;
        private String username;
		private int totalReceived;
		private int totalSent;
        private double wallet;
        private String role;
    }

	@Override
    public Transfer toTransfer() {
		return new Transfer(this.id, this.enabled, this.username, this.received.size(), this.sent.size(), this.wallet, this.role.name());
	}
	
	@Override
	public String toString() {
		return toTransfer().toString();
	}
}

