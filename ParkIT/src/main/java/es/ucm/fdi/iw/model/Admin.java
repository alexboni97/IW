package es.ucm.fdi.iw.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

/**
 * An authorized admin user of the system.
 */
@Entity
@Data
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id")
@NamedQueries({
    @NamedQuery(name = "Admin.findByCodigoAdmin", query = "select a from Admin a where :codigoAdmin = a.codigoAdmin")
})
@Table(name = "IWAdmin")
public class Admin extends User {

    @Column(nullable = false, unique = true)
    private String codigoAdmin;

    @Getter
    public static class Transfer extends User.Transfer {
        
        private String codigoAdmin;

        public Transfer(long id, boolean enabled, String username, String password, String codigoAdmin, int totalReceived, int totalSent, double wallet, String role) {
            super(id, enabled, username, totalReceived, totalSent, wallet, role);
            this.codigoAdmin = codigoAdmin;
        }
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(this.getId(), this.isEnabled(), this.getUsername(), this.getPassword(), this.codigoAdmin, this.getReceived().size(), this.getSent().size(), this.getWallet(), this.getRole().toString());
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}