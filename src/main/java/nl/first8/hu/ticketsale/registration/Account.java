package nl.first8.hu.ticketsale.registration;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String emailAddress;

    public Account(String emailAddress) {
        this.emailAddress = emailAddress;
    }

}
