package nl.first8.hu.ticketsale.registration;

import java.util.List;
import java.util.Optional;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RegistrationRepository {

    private final EntityManager entityManager;

    @Autowired
    public RegistrationRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Persists the given <code>account</code> in the underlying data source.
     *
     * @param account the account to persist
     *
     * @throws EntityExistsException if the entity already exists
     */
    public void insert(final Account account) {
        throw new UnsupportedOperationException("Not supported yet!");
    }

    /**
     * Optionally returns the Account identified by the given <code>id</code>
     *
     * @param id the id of the Account to find
     * @return Returns the Account identified by the given <code>id</code> or an
     * {@link Optional#empty() empty} Optional if no Account could be identified
     * with the given <code>id</code>.
     */
    public Optional<Account> findById(final Object id) {
        throw new UnsupportedOperationException("Not supported yet!");
    }

    /**
     * Returns a list of all {@link Account}s in the underlying data source
     *
     * @return a list of all known {@link Account}s or an empty list if no
     * {@link Account}s exist in the underlying data source
     */
    public List<Account> findAll() {
        throw new UnsupportedOperationException("Not supported yet!");
    }

}
