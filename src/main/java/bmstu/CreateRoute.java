package bmstu;

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
        Srting ur = String.valueOf(Uri.create(serverUrl).query(Query.create(Pair.create(URL_STRING, url),
        Pair.create(COUNT_STRING, String.valueOf(count - 1)))));
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
                        
                    }
                }
    }

}
