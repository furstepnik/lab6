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
            System.out.println("Server online at " + HOST + ":" + port + "/\nPress RETURN to stop...");
            System.in.read();
            binding.thenCompose(ServerBinding::unbind).thenAccept(unbound -> system.terminate());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
