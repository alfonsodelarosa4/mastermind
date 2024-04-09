import React, { useState, useEffect, useRef, createContext, useContext, } from 'react';
import { useSelector } from 'react-redux';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

// holds WebSocket State
export const WebSocketContext = createContext();

// makes WebSocket state accesible in child components as React Hook
export function useWebSocket() {
  const context = useContext(WebSocketContext);

  if (!context) {
    throw new Error('useWebSocket must be within WebSocketProvider');
  }
  return context;
}

// websocket provider: receives message and sends message
export const WebSocketProvider = ({ children }) => {
  // stores latest message
  const [newMessage, setNewMessage] = useState(null);
  // stores is loading
  const [isLoading, setIsLoading] = useState(false);
  // stores stompclient 
  const [stompClient, setStompClient] = useState(null);  
  // mutext: only one function
  let isWebSocketSetupInProgress = useRef(false);
  // stores websocket topic
  const gameSession = useSelector((state => state.gameSession));

  // sends message over websocket
  const sendWebSocketMessage = (message, path) => {
    // returns if no websocket topic or if no stomp client
    if (gameSession.id == null || stompClient === null) return;
    stompClient.send('/app' + path, {}, JSON.stringify(message));
    setIsLoading(true);
  }

  // runs on every render of component
  useEffect(() => {
    // returns if no websocket topic
    if (gameSession.id === null) return;
    // returns if websocket already running
    if (isWebSocketSetupInProgress.current) return;
    // sets to true to state that websocket already running
    isWebSocketSetupInProgress.current = true;

    // creates new WebSocket connnection with SockJS
    const socket = new SockJS('http://localhost:8080/ws');
    // initializes STOMP client over WebSocket connection
    const client = Stomp.over(socket);
    // initiates connection to WebSocket server with STOMP client
    client.connect({}, () => {
      // subscribes to topic
      client.subscribe('/game-session/' + gameSession.id, (message) => {
        setIsLoading(false);
        const messageObject = JSON.parse(message.body);
        setNewMessage(messageObject);
      });
    });

    // updates stomp client: to make ws messages
    setStompClient(client);

    return () => {
      // when the component unmounts, the STOMP client disconnects
      if (stompClient) {
        stompClient.disconnect();
      }
    };
  }, [gameSession]);

  return (<WebSocketContext.Provider value={{ sendWebSocketMessage, newMessage, isLoading }}>
    {children}
  </ WebSocketContext.Provider>)

}