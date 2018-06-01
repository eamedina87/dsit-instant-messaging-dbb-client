/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apiREST;

/**
 *
 * @author juanluis
 */
public interface Cons {
  
  String localhost = "localhost";
  //String outer = "192.168.0.34";
  String address = localhost;  
  
  String projectNamePrevious = "InstantMessagingRemote_server_with_ddbb";
  String projectNameNew = "dsit-instant-messaging-dbb-server";
  String projectName = projectNameNew;
  
  String SERVER_REST = "http://"+address+":8080/"+projectName+"/webresources";
  String SERVER_WEBSOCKET = "ws://"+address+":8080/"+projectName+"/ws";
  
}
