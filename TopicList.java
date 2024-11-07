import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Set;

public class TopicList implements Serializable {
    Set<String> topicList;

    public TopicList(Set<String> Topics) {
        topicList = Topics;
    }

}
