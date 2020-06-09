package projekti;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import projekti.Account.AccountRepository;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FluentiumTest extends org.fluentlenium.adapter.junit.FluentTest {
    
    @LocalServerPort
    private Integer port;
    
    @Autowired
    private AccountRepository accountRepository;
    
    @Test
    public void canSignUp() {
        int amountOfUsers = accountRepository.findAll().size();
        goTo("http://localhost:" + port + "/signup");
        find("#name").fill().with("12345");
        find("#username").fill().with("54321");
        find("#password").fill().with("56789");
        find("#path").fill().with("98765");
        find("form").first().submit();
        assertTrue(accountRepository.findAll().size() == amountOfUsers + 1);
        assertTrue(pageSource().contains("Networking for tattoo artists."));
    
    }
    
    @Test
    public void canLogIn() {
        int amountOfUsers = accountRepository.findAll().size();
        goTo("http://localhost:" + port + "/signup");
        find("#name").fill().with("1234");
        find("#username").fill().with("4321");
        find("#password").fill().with("5678");
        find("#path").fill().with("8765");
        find("form").first().submit();
        assertTrue(accountRepository.findAll().size() == amountOfUsers + 1);
        
        assertTrue(pageSource().contains("Networking for tattoo artists."));
        find("#username").fill().with("4321");
        find("#password").fill().with("5678");
        find("form").first().submit();
        assertTrue(pageSource().contains("1234"));
        
    }
    
}
