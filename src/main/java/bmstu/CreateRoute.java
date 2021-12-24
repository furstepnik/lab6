package bmstu;

import java.time.Duration;

import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.Query;
import akka.http.javadsl.model.Uri;
import akka.http.javadsl.server.Route;
import akka.japi.Pair;
import akka.pattern.Patterns;

public class CreateRoute {
    private final String URL = "url";
    private final String COUNT = "count";
    private final int TIMEOUT = 10;
    private final Http http;
    private final ActorRef confActor;

    public CreateRoute(Http http, ActorRef conf) {
        this.http = http;
        this.confActor = conf;
    }

    private String initUrl(String serverUrl, String url, int count) {
        String ur = String.valueOf(Uri.create(serverUrl).query(Query.create(Pair.create(URL, url),
        Pair.create(COUNT, String.valueOf(count - 1)))));
        System.out.println("hi" + ur);
        return ur;
    }

    public Route createRoute() {
        return route(
                get(() ->
                parameter(URL_STRING, url -> {
                    if (Integer.parseInt(count) == 0) {
                        return completeWithFuture(this.http.singleRequest(HttpRequest.create(url)));
                    } else {
                        return completeWithFuture(
                                Patterns.ask(this.confActor
                                        ,new EmptyMessege(),
                                        Duration.ofSeconds(TIMEOUT))
                                
                    }
                }
    }

}
