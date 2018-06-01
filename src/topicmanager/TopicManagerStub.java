package topicmanager;

import apiREST.apiREST_Message;
import apiREST.apiREST_Publisher;
import apiREST.apiREST_Subscriber;
import apiREST.apiREST_Topic;
import entity.Message;
import entity.Topic;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import publisher.Publisher;
import publisher.PublisherStub;
import subscriber.Subscriber;
import webSocketService.WebSocketClientFinal;

public class TopicManagerStub implements TopicManager {

  public entity.User user;

  public TopicManagerStub(entity.User user) {
    //WebSocketClient.newInstance();
    WebSocketClientFinal.getInstance();
    this.user = user;
  }

  public void close() {
    WebSocketClientFinal.close();
  }

  public Publisher addPublisherToTopic(String topic) {
    Topic mTopic = new Topic();
    mTopic.setName(topic);
    entity.Publisher publisher = new entity.Publisher();
    publisher.setTopic(mTopic);
    publisher.setUser(user);
    publisher = apiREST_Publisher.create_and_return_Publisher(publisher);
    
    return new PublisherStub(mTopic);
  }

  public int removePublisherFromTopic(String topic) {
      //Publisher publisher = new PublisherStub(topic);
    //Topic mTopic = apiREST_Topic.retrieveTopicByName(topic);
      
    //apiREST_Publisher.deletePublisher(new PublisherStub(mTopic));
    apiREST_Publisher.deletePublisher(apiREST_Publisher.PublisherOf(user));
    return -1;

  }

  public boolean isTopic(String topic_name) {
    return apiREST_Topic.retrieveTopicByName(topic_name).getName()!=null;
  }

  public Set<String> topics() {
    Set<String> topic_names = new HashSet<String>();
    List<Topic> mTopics = apiREST_Topic.allTopics();
    for (Topic topic : mTopics){
        topic_names.add(topic.getName());
    }
    return topic_names;
  }

  public boolean subscribe(String topic, Subscriber subscriber) {
      //TODO check
    entity.Subscriber mSubscriber = new entity.Subscriber();
    mSubscriber.setTopic(apiREST_Topic.retrieveTopicByName(topic));
    mSubscriber.setUser(user);
    mSubscriber = apiREST_Subscriber.create_and_return_Subscriber(mSubscriber);
    return mSubscriber!=null;
  }

  public boolean unsubscribe(String topic, Subscriber subscriber) {
      //TODO check
      entity.Subscriber mSubs = new entity.Subscriber();
      mSubs.setTopic(apiREST_Topic.retrieveTopicByName(topic));
      mSubs.setUser(user);
    apiREST_Subscriber.deleteSubscriber(mSubs);
    return true;
  }
  
  public Publisher publisherOf() {
    entity.Publisher publisher = apiREST_Publisher.PublisherOf(user);
    Publisher mPub = new PublisherStub(publisher.getTopic());
    return mPub;
  }

  public List<entity.Subscriber> mySubscriptions() {
    return apiREST_Subscriber.mySubscriptions(user);
  }

  public List<Message> messagesFrom(entity.Topic topic) {
    return apiREST_Message.messagesFrom(topic);
  }

}
