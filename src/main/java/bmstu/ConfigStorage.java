package bmstu;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import java.util.ArrayList;
import java.util.Random;

public class ConfigStorage extends AbstractActor{
    private ArrayList<String> storageServers;

    @Override
    public Receive createReceive() {
        return ReceiveBuilder.create()
        .match(MessageServer.class, msg -> this.storageServers=msg.getUrls())
        .match(EmptyMessege.class, msg -> sender().tell(storageServers.get(new Random().nextInt(storageServers.size())), self()))
        .build();
    }
}
