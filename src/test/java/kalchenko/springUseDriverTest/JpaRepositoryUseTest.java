package kalchenko.springUseDriverTest;

import kalchenko.data.DataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = DataRepository.class)
public class JpaRepositoryUseTest {

    @Autowired
    DataRepository dataRepository;

    @Test
    public void dataRepositorySmoke(){

        assertNotNull(dataRepository);

    }


}
