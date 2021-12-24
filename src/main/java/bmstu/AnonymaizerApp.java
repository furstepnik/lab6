package bmstu;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

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
