package nl.first8.hu.ticketsale.registration;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
@Transactional
public class RegistrationResource {

    private final RegistrationService service;

    @Autowired
    public RegistrationResource(RegistrationService service) {
        this.service = service;
    }

    @PostMapping(path = "/{emailAddress:.+}")
    public ResponseEntity<Void> post(@PathVariable("emailAddress") final String emailAddress) {
        try {
            service.insert(new Account(emailAddress));

            return ResponseEntity.ok().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping(path = "/{emailAddress:.+}")
    public ResponseEntity<Account> get(@PathVariable("emailAddress") final String emailAddress) {
        Optional<Account> account = service.getById(emailAddress);

        return account
                .map(acc -> ResponseEntity.ok(acc))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public List<Account> getAll() {
        return service.list();
    }
}
