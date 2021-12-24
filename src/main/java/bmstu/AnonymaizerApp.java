package bmstu;

import java.io.IOException;

public class AnonymaizerApp {
    private static final String HOST = "127.0.0.1";
    public static void main(String[] args) throws IOException {
        final ActorSystem system = ActorSystem.crete("routes");
        ActorRef storageActor = system.ActorOf(Pops.create(ConfigStorage.class));
        if (args.length != 1) {
            System.out.println("Missed argument for Port. Returning...");
            System.exit(-1);
        }
        int port;
        try {
            port = Integer.parseInt(args[0]);
            new ZookeeperWatcher(storageActor, port);
            final Http http = Http.get(system);
            final ActorMaterializer mat = ActorMaterializer.create(system);
            final Flow<HttpRequest, HttpResponse, NotUsed> flow = new CreateRoute(http, storageActor).createRoute().flow(system, mat);
            final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                    flow,
                    ConnectHttp.toHost(HOST, port),
                    mat);
            
        }
    }
}
