package bmstu;

import org.apache.zookeeper.CreateMode;

public class ZookeeperWatcher implements Watcher {
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
        String adress = "http://" + HOST + ":" + this.currentPort;
        System.out.println(adress);
        try {
            this.zoo.create(PATH,
                            adress.getBytes(),
                            ZooDefs.Ids.OPEN_ACL_UNSAFE,
                            CreateMode.PERSISTENT_SEQUENTIAL);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
