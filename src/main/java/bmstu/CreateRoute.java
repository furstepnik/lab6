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

}
