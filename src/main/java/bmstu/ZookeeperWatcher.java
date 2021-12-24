package bmstu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import akka.actor.ActorRef;
import akka.pattern.Patterns;

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

    @Override
    public void process(WatchedEvent event) {
        List<String> servers;
        try {
            servers = this.zoo.getChildren(PATH.substring(0, PATH.length() - 2), this);
            ArrayList<String> urlsOfServers = new ArrayList<>();
            for (String s : servers) {
                urlsOfServers.add(new String(this.zoo.getData(PATH.substring(0, PATH.length() - 1) + s, false, null)));
            }
            Patterns.ask(this.configStorageActor, new MessageServer(urlsOfServers), 3000);
        } catch (KeeperException | InterruptedException e) {
            
        }
}
