package webSocketService;

import apiREST.Cons;
import apiREST.apiREST_Topic;
import com.google.gson.Gson;
import entity.Message;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.MessageHandler;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import subscriber.Subscriber;

import util.MySubscription;

@ClientEndpoint
public class WebSocketClientFinal implements MessageHandler{

  static Map<String, Subscriber> subscriberMap;
  static Session session;

  
  private static WebSocketClientFinal mInstance;
  
 
  
  public static WebSocketClientFinal getInstance() {
      if (mInstance==null){
          mInstance = new WebSocketClientFinal();
      }
      return mInstance;
  }

  public WebSocketClientFinal(){
      if (mInstance==null){
        subscriberMap = new HashMap<String, Subscriber>();
        try {
          WebSocketContainer container = ContainerProvider.getWebSocketContainer();
          session = container.connectToServer(WebSocketClientFinal.class,
            URI.create(Cons.SERVER_WEBSOCKET));

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
  }
  
  //only one subscriber per topic allowed:
  public static synchronized void addSubscriber(String topic_name, Subscriber subscriber) {
      //TODO CHECK
    if (subscriber!=null){
        subscriberMap.put(topic_name, subscriber);
        MySubscription message = new MySubscription();
        message.topic = topic_name;
        message.type = true;
        Gson gson = new Gson();
        sendMessage(gson.toJson(message));
    }
    
  }

  public static synchronized void removeSubscriber(String topic_name) {
    if(subscriberMap!=null && subscriberMap.containsKey(topic_name)){
        subscriberMap.remove(topic_name);
        MySubscription message = new MySubscription();
        message.topic = topic_name;
        message.type = false;
        Gson gson = new Gson();
        sendMessage(gson.toJson(message));
    }
  }

  public static void close() {
    try {
      session.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
    private MessageHandler messageHandler;

  @OnMessage
  public void onMessage(String message) {
    if (this.messageHandler != null) {
        this.messageHandler.handleMessage(message);
    }
    Gson gson = new Gson();
    Message mMessage = gson.fromJson(message, Message.class);
    String topic = mMessage.getTopic().getName();
    //message to warn closing a topic:
    if (topic.equals("CLOSED")) {
      //TODO CHECK
      subscriberMap.get(topic).onClose(topic, mMessage.getContent());
    } 
    //ordinary message from topic:
    else {
      //TODO CHECK
      subscriberMap.get(topic).onEvent(topic, mMessage.getContent());
    }
  }
  
  
  @OnError
  public void onError(Throwable error) {
      System.out.print(error.getMessage()); 
  }
  
  public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    /**
     * Send a message.
     *
     * @param message
     */
    public static void sendMessage(String message) {
        session.getAsyncRemote().sendText(message);
    }

    /**
     * Message handler.
     *
     * @author 
     */
    public static interface MessageHandler {

        public void handleMessage(String message);
    }

}
