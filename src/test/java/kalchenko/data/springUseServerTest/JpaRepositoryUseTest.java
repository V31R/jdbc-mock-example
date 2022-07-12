package kalchenko.data.springUseServerTest;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import kalchenko.data.DataRepository;
import org.example.HttpPreparedStatement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WireMockTest(httpPort = 8080)
public class JpaRepositoryUseTest {

    @Autowired
    DataRepository dataRepository;

    private static String host="http://localhost:";
    private static String url="/sql-mock";
    private static String uri;


    @BeforeAll
    static void startWireMock(WireMockRuntimeInfo wmRuntimeInfo){

        uri = host + wmRuntimeInfo.getHttpPort() + url;

    }

    @Test
    public void testWireMockSetUp(WireMockRuntimeInfo wmRuntimeInfo){

        assertEquals(host + wmRuntimeInfo.getHttpPort(),wmRuntimeInfo.getHttpBaseUrl());

    }

    @Test
    public void dataRepositorySmoke(){

        assertNotNull(dataRepository);

    }

    @Test
    public void findAll(WireMockRuntimeInfo wmRuntimeInfo){

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .willReturn(okForContentType("text/plain","\"Timestamp\",\"Age\",\"Gender\" " +
                        "\n 2014-08-27 11:29:31,37,\"Female\"")));

        HttpPreparedStatement httpPreparedStatement;

        var result = dataRepository.findAll();

        assertNotNull(result);

    }


}
