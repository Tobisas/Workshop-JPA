package nl.first8.hu.ticketsale.registration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.hamcrest.Matchers;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Rollback(false)
public class RegistrationRepositoryIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Sql(statements = {
        "TRUNCATE TABLE `ticketswap_db`.`account`;"
    })
    @Test
    public void testInsert() throws Exception {
        final Account expectedAccount = new Account("f.dejong@first8.nl");

        mvc.perform(
                post("/registration/{emailAddress}", expectedAccount.getEmailAddress())
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        final Account actualAccount = entityManager.find(Account.class, expectedAccount.getEmailAddress());
        assertThat(actualAccount.getEmailAddress(), is(expectedAccount.getEmailAddress()));
    }

    @Sql(statements = {
        "TRUNCATE TABLE `ticketswap_db`.`account`;",
        "INSERT INTO `ticketswap_db`.`account` VALUES('f.dejong@first8.nl')"})
    @Test
    public void testInsertDuplicateEmailAddress() throws Exception {
        mvc.perform(
                post("/registration/{emailAddress}", "f.dejong@first8.nl")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isConflict());
    }

    @Sql(statements = {
        "TRUNCATE TABLE `ticketswap_db`.`account`;",
        "INSERT INTO `ticketswap_db`.`account` VALUES('f.dejong@first8.nl'),  ('t.poll@first8.nl')"})
    @Test
    public void testGetById() throws Exception {

        final String expectedAccount = "t.poll@first8.nl";

        final MvcResult result = mvc.perform(
                get("/registration/{emailAddress}", expectedAccount)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        assertThat(account(result).getEmailAddress(), is(expectedAccount));
    }

    @Sql(statements = {
        "TRUNCATE TABLE `ticketswap_db`.`account`;",
        "INSERT INTO `ticketswap_db`.`account` VALUES('f.dejong@first8.nl'), ('t.poll@first8.nl')"})
    @Test
    public void testGetByIdNotAvailable() throws Exception {
        mvc.perform(
                get("/registration/{emailAddress}", "r.boss@first8.nl")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Sql(statements = {
        "TRUNCATE TABLE `ticketswap_db`.`account`;",
        "INSERT INTO `ticketswap_db`.`account` VALUES('f.dejong@first8.nl'), ('t.poll@first8.nl')"})
    @Test
    public void testGetAll() throws Exception {

        List<Account> expectedAccounts = Arrays.asList(new Account("t.poll@first8.nl"), new Account("f.dejong@first8.nl"));

        MvcResult result = mvc.perform(
                get("/registration/")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        assertThat(accounts(result), containsInAnyOrder(expectedAccounts.toArray()));
    }

    @Sql(statements = {"TRUNCATE TABLE `ticketswap_db`.`account`;"})
    @Test
    public void testGetAllNoResultsAvailable() throws Exception {
        List<Account> expectedAccounts = Collections.emptyList();

        MvcResult result = mvc.perform(
                get("/registration/")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andReturn();

        assertThat(accounts(result), containsInAnyOrder(expectedAccounts.toArray()));
    }

    private Account account(final MvcResult result) throws UnsupportedEncodingException, IOException {
        return objectMapper.readValue(result.getResponse().getContentAsString(), Account.class);
    }

    private List<Account> accounts(final MvcResult result) throws UnsupportedEncodingException, IOException {
        return objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<Account>>() {
        });
    }

}
