package io.quarkus.rest.client.reactive.subresource;

import io.quarkus.test.QuarkusUnitTest;
import io.quarkus.test.common.http.TestHTTPResource;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

public class SubSubResourceTest {

    @RegisterExtension
    static final QuarkusUnitTest TEST = new QuarkusUnitTest()
            .withApplicationRoot((jar) -> jar
                    .addClasses(RootClient1.class, SubClient1.class, SubClient2.class, Resource.class));

    @TestHTTPResource
    URI baseUri;

    @Test
    void shouldSupportSubSubClients() {
        RootClient1 rootClient = RestClientBuilder.newBuilder().baseUri(baseUri).build(RootClient1.class);
        var result = rootClient.sub().subSub().subSubGet();
        assertThat(result).isEqualTo("foobar");
    }


    @Path("/path/")
    interface RootClient1 {
        @Path("/sub")
        SubClient1 sub();
    }

    interface SubClient1 {
        @Path("/sub2")
        SubClient2 subSub();
    }

    @Produces("text/plain")
    interface SubClient2 {
        @GET
        @Path("/simple")
        String subSubGet();
    }
}
