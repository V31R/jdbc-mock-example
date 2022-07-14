package kalchenko.data.springUseServerTest;

import com.fasterxml.jackson.databind.util.ArrayIterator;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import kalchenko.data.Data;
import kalchenko.data.DataCRUD;
import kalchenko.data.DataRepository;
import org.example.HttpPreparedStatement;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@WireMockTest(httpPort = 8080)
public class CrudRepositoryTest {

    @Autowired
    DataCRUD dataCrud;

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
    public void dataCrudSmoke(){

        assertNotNull(dataCrud);

    }

    @Test
    public void findAll(WireMockRuntimeInfo wmRuntimeInfo){

        stubForOneData(wmRuntimeInfo);

        var result = dataCrud.findAll();

        assertEquals(getData(0), result.get(0));

    }

    @Test
    public void findAll_Sort(WireMockRuntimeInfo wmRuntimeInfo){

        stubForOneData(wmRuntimeInfo);

        var result = dataCrud.findAll(Sort.by("name"));

        assertEquals(getData(0), result.get(0));

    }

    @Test
    public void findAll_Page(WireMockRuntimeInfo wmRuntimeInfo){

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".+count.+"))
                .willReturn(okForContentType("text/plain","col_0_0_\n1")));

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".+limit.+"))
                .willReturn(okForContentType("text/plain",getCsvData())));


        var result = dataCrud.findAll(Pageable.ofSize(1)).get().findFirst().get();

        assertEquals(getData(0), result);

    }


    @Test
    public void findAllById(WireMockRuntimeInfo wmRuntimeInfo){

        stubForOneData(wmRuntimeInfo);

        var ids =new ArrayList<Long>();
        ids.add(0L);

        var result = dataCrud.findAllById(ids);

        assertEquals(getData(0), result.get(0));

    }

    @Test
    public void findById(WireMockRuntimeInfo wmRuntimeInfo){

        stubForOneDataWithZero(wmRuntimeInfo);

        var result = dataCrud.findById(1L);

        assertEquals(getData(1), result.get());

    }

    @Test
    public void save_IfSameObjectAlreadyExist(WireMockRuntimeInfo wmRuntimeInfo){

        stubForOneDataWithZero(wmRuntimeInfo);;

        var result = dataCrud.save(getData(1));

        assertEquals(getData(1), result);

    }

    @Test
    public void save_IfSameObjectDoNotExist(WireMockRuntimeInfo wmRuntimeInfo){

        stubForOneDataWithZero(wmRuntimeInfo);

        Data changed = getData(1);
        changed.setDescription("changed");
        var result = dataCrud.save(changed);

        assertEquals(changed, result);

    }




    static Data getData(long id){

        Data data = new Data();
        data.setId(id);
        data.setDescription("description");
        data.setName("name");

        return data;

    }

    static  String getCsvData(){
        Data data = getData(0);
        return "\"id1_0_\", \"descript2_0_\", \"name3_0_\"" +
                "\n " + data.getId() + ", \""
                + data.getDescription() + "\", \""
                + data.getName() + "\"";

    }

    static void stubForOneData(WireMockRuntimeInfo wmRuntimeInfo){

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .willReturn(okForContentType("text/plain",getCsvData())));

    }

    static  String getCsvDataWithZero(){
        //for method getById spring generated names with adding '0_' in query
        Data data = getData(1);
        return "\"id1_0_\", \"descript2_0_0_\", \"name3_0_0_\"" +
                "\n " + data.getId() + ", \""
                + data.getDescription() + "\", \""
                + data.getName() + "\"";

    }

    static void stubForOneDataWithZero(WireMockRuntimeInfo wmRuntimeInfo){

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .willReturn(okForContentType("text/plain",getCsvDataWithZero())));

    }

}
