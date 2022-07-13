package kalchenko.data.springUseServerTest;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import kalchenko.data.Data;
import kalchenko.data.DataRepository;
import org.example.HttpPreparedStatement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

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
                .willReturn(okForContentType("text/plain",getCsvData())));

        var result = dataRepository.findAll();

        assertEquals(getData(), result.get(0));

    }


    @Test
    public void findAllById(WireMockRuntimeInfo wmRuntimeInfo){

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .willReturn(okForContentType("text/plain",getCsvData())));
        var ids =new ArrayList<Long>();
        ids.add(0L);
        var result = dataRepository.findAllById(ids);

        assertEquals(getData(), result.get(0));

    }

    static Data getData(){

        Data data = new Data();
        data.setId(0L);
        data.setDescription("description");
        data.setName("name");

        return data;

    }

    static  String getCsvData(){
        Data data = getData();
        return "\"id1_0_\", \"descript2_0_\", \"name3_0_\"" +
                "\n " + data.getId() + ", \""
                + data.getDescription() + "\", \""
                + data.getName() + "\"";

    }


}
