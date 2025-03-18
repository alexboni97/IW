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
    @NamedQuery(name = "Admin.findByCodigo", query = "select a from Admin a where :codigo = a.codigo")
})
@Table(name = "IWAdmin")
public class Admin extends User {

    @Column(nullable = false, unique = true)
    private String codigo;

    @Getter
    public static class Transfer extends User.Transfer {
        
        private String codigo;

        public Transfer(long id, boolean enabled, String username, int totalReceived, int totalSent, double wallet, int telephone, String email, String role, String codigo) {
            super(id, enabled, username, totalReceived, totalSent, wallet, telephone, email, role);
            this.codigo = codigo;
        }
        }

        @Override
        public Transfer toTransfer() {
        return new Transfer(this.getId(), this.isEnabled(), this.getUsername(), this.getReceived().size(), this.getSent().size(), this.getWallet(), this.getTelephone(), this.getEmail(), this.getRole().name(), this.codigo);
        }

        @Override
        public String toString() {
        return toTransfer().toString();
    }
}