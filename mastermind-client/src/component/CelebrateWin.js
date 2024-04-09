import { useState, useEffect } from 'react';
import Confetti from 'react-confetti'
import { useWebSocket } from "../component/WebSocketProvider";

// celebrates win when GameWon message received
export function CelebrateWin() {
  const [celebrate, setCelebrate] = useState(false);
  const { newMessage } = useWebSocket();

  // runs every time websocket receives a message
  useEffect(() => {
    // null skip
    if (newMessage === null ) return;
    // if "GameWon" message, celebrate
    if (newMessage.type === "GameWon") setCelebrate(true);
    
  }, [newMessage])
  
  return (<>
    {celebrate && (<Confetti />)}
    </>
  )
}