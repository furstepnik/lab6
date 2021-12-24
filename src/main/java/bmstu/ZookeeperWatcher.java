package bmstu;

public class ZookeeperWatcher {
    private final String CONNECET = "127.0.0.1:2181";
    private final String HOST = "127.0.0.1";
    private final String PATH = "/servers/s";
    private final Integer SESSION_TIMEOUT = 5000;
    private final ActorRef configStorageActor;
    private final ZooKeeper zoo;
    private int currentPort;

    public ZookeeperWatcher(ActorRef sActor, int p) throws IOException {
        this.configStorageActor = sActor;
        this.zoo = new ZooKeeper(CONNECET, SESSION_TIMEOUT, this);
        this.currentPort = p;
        this.CreateZooServers();
    }

    private void CreateZooServers() {
        
    }
}
