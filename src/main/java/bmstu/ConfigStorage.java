package bmstu;

public class ConfigStorage extends AbstractActor{
    private ArrayList<String> storageServers;

    @Override
    public Receive createReceive() {
        return Receive =Builder.create()
        .match(MessageServer.class, msg -> this.storageServers=msg.getUrls())
        .match(EmptyMessage.class, msg -> sender().tell(storageServers.get(new Random().nextInt(storageServers.size())), self()))
        .build();
    }
}
