package kalchenko;

import org.example.HttpDriver;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ApplicationSmokeTest {

    @Test
    public void contextLoad(){
        HttpDriver.driverPropertiesLoad();
        assertTrue(true);

    }

}
