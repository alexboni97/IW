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
    @NamedQuery(name = "Admin.findByCodigoAdmin", query = "select obj from Admin obj where :codigoAdmin = obj.codigoAdmin")
})
@Table(name = "IWAdmin")
public class Admin extends User {
    private String codigoAdmin;

    @Getter
    public static class Transfer extends User.Transfer {
        private String codigoAdmin;

        public Transfer(long id, boolean enabled, String username, String password, String codigoAdmin, int totalReceived, int totalSent) {
            super(id, username, totalReceived, totalSent);
            this.codigoAdmin = codigoAdmin;
        }

    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(getId(), isEnabled(), getUsername(), getPassword(), codigoAdmin, getReceived().size(), getSent().size());
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
}