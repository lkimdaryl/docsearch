import static org.junit.Assert.*;

public class DocSearchTest{

    @Test
    public void dummyTest(){
        Handler uriHandler = new Handler("./technical/");
        URI uri = new URI("http://localhost:4000");
        assertEquals("Don't know how to handle that path!", uriHandler.handleRequest(uri));
    }
}


