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

        assertEquals(getData(), result.get(0));

    }

    @Test
    public void findAll_Sort(WireMockRuntimeInfo wmRuntimeInfo){

        stubForOneData(wmRuntimeInfo);

        var result = dataCrud.findAll(Sort.by("name"));

        assertEquals(getData(), result.get(0));

    }

    @Test
    public void findAll_Page(WireMockRuntimeInfo wmRuntimeInfo){

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".+count.+"))
                .willReturn(okForContentType("text/plain","number\n1")));

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".+limit.+"))
                .willReturn(okForContentType("text/plain",getCsvData())));


        var result = dataCrud.findAll(Pageable.ofSize(1)).get().findFirst().get();

        assertEquals(getData(), result);

    }


    @Test
    public void findAllById(WireMockRuntimeInfo wmRuntimeInfo){

        stubForOneData(wmRuntimeInfo);

        var ids =new ArrayList<Long>();
        ids.add(getData().getId());

        var result = dataCrud.findAllById(ids);

        assertEquals(getData(), result.get(0));

    }

    @Test
    public void findById(WireMockRuntimeInfo wmRuntimeInfo){

        stubForOneData(wmRuntimeInfo);

        var result = dataCrud.findById(getData().getId());

        assertEquals(getData(), result.get());

    }

    @Test
    public void save_IfSameObjectAlreadyExist(WireMockRuntimeInfo wmRuntimeInfo){

        stubForOneData(wmRuntimeInfo);;

        var result = dataCrud.save(getData());

        assertEquals(getData(), result);

    }

    @Test
    public void save_IfSameObjectDoNotExist(WireMockRuntimeInfo wmRuntimeInfo){

        stubForOneData(wmRuntimeInfo);

        Data changed = getData();
        changed.setDescription("changed");
        var result = dataCrud.save(changed);

        assertEquals(changed, result);

    }


    @Test
    public void save_IfSameObjectDoNotExist_(WireMockRuntimeInfo wmRuntimeInfo){

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".?select data.+"))
                .willReturn(okForContentType("text/plain","\"id\", \"description\", \"name\"")));
        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".?insert.+"))
                .willReturn(okForContentType("text/plain","key\n2")));

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".?select currval.+"))
                .willReturn(okForContentType("text/plain","key, key\n1, 1")));

        Data changed = getData();
        var result = dataCrud.save(changed);

        assertEquals(changed, result);

    }

    @Test
    public void deleteAll(WireMockRuntimeInfo wmRuntimeInfo){

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".?delete.+"))
                .willReturn(okForContentType("text/plain", "rows\n1")));

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".?select.+"))
                .willReturn(okForContentType("text/plain",getCsvData())));

        dataCrud.deleteAll();
        assertTrue(true);

    }

    @Test
    public void deleteAll_DataList(WireMockRuntimeInfo wmRuntimeInfo){

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".?delete.+"))
                .willReturn(okForContentType("text/plain", "rows\n1")));

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".?select.+"))
                .willReturn(okForContentType("text/plain",getCsvData())));

        var entities = new ArrayList<Data>();
        entities.add(getData());

        dataCrud.deleteAll(entities);

        assertTrue(true);

    }

    @Test
    public void deleteAllById(WireMockRuntimeInfo wmRuntimeInfo){

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".?delete.+"))
                .willReturn(okForContentType("text/plain", "rows\n1")));

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".?select.+"))
                .willReturn(okForContentType("text/plain",getCsvData())));

        var ids =new ArrayList<Long>();
        ids.add(0L);

        dataCrud.deleteAllById(ids);

        assertTrue(true);

    }


    @Test
    public void deleteById(WireMockRuntimeInfo wmRuntimeInfo){

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".?delete.+"))
                .willReturn(okForContentType("text/plain", "rows\n1")));

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .withRequestBody(WireMock.matching(".?select.+"))
                .willReturn(okForContentType("text/plain",getCsvData())));


        dataCrud.deleteById(0L);

        assertTrue(true);

    }



    static Data getData(){

        Data data = new Data();
        data.setId(1L);
        data.setDescription("description");
        data.setName("name");

        return data;

    }

    static  String getCsvData(){
        Data data = getData();
        return "\"id\", \"description\", \"name\"" +
                "\n " + data.getId() + ", \""
                + data.getDescription() + "\", \""
                + data.getName() + "\"";

    }

    static void stubForOneData(WireMockRuntimeInfo wmRuntimeInfo){

        wmRuntimeInfo.getWireMock().stubFor(post(WireMock.urlEqualTo(url))
                .willReturn(okForContentType("text/plain",getCsvData())));

    }


}
