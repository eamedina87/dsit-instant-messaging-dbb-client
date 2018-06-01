package publisher;

import apiREST.apiREST_Message;
import apiREST.apiREST_Publisher;
import apiREST.apiREST_Subscriber;
import apiREST.apiREST_Topic;
import entity.Message;
import entity.Topic;

public class PublisherStub implements Publisher {

  Topic topic;

  public PublisherStub(Topic topic) {
    this.topic = topic;
  }

  public void publish(String topic, String event) {
    //TODO  check
    Message mMessage = new Message();
    mMessage.setContent(event);
    mMessage.setTopic(this.topic);
    apiREST_Message.createMessage(mMessage);
  }

  public String topicName() {
    return topic.getName();
  }

}
