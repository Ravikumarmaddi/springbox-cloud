package io.springbox.apigateway.services.catalog;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

@Service
public class CatalogIntegrationService {

    Log log = LogFactory.getLog(CatalogIntegrationService.class);

    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "stubMovie")
    public Observable<Movie> getMovie(final String mlId) {
        return new ObservableResult<Movie>() {
            @Override
            public Movie invoke() {
                final Movie movie = restTemplate.getForObject("http://springbox-api-gateway/catalog/movies/{mlId}", Movie.class, mlId);
                log.debug(movie);
                return movie;
            }
        };
    }

    private Movie stubMovie(final String mlId) {
        Movie stub = new Movie();
        stub.setMlId(mlId);
        stub.setTitle("Interesting...the wrong title. Sssshhhh!");
        return stub;
    }
}
