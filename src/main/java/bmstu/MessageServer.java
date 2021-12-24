package bmstu;

import java.util.ArrayList;

public class MessageServer {
    private final ArrayList<String> urls;
    public MessageServer(ArrayList<String> urls) {
        this.urls = urls;
    }
    public ArrayList<String> getUrls() {
        return this.urls;
    }


}
