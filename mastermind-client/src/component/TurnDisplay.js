import { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { useWebSocket } from "./WebSocketProvider"
import Card from 'react-bootstrap/Card';
import Collapse from 'react-bootstrap/Collapse';
import GameEventHistory from "./GameEventHistory";

export default function TurnDisplay() {
  const { newMessage } = useWebSocket();
  const partyGame = useSelector((state => state.partyGame));
  
  const [show, setShow] = useState(true);

  useEffect(()=>{
    if(newMessage == null) return;
    switch(newMessage.type) {
      case "GameWon":
        setShow(false);
      break
      case "GameLost":
        setShow(false);
      break
      default:
    }
  
    }, [newMessage])
      
  return(
    <Collapse in={show} style={{position: 'fixed', top: '10px', right: '10px', zIndex: '1000', }}>
      <Card
        bg={partyGame.username === partyGame.currentTurn? "warning" : "secondary"}
        style={{ width: '15rem', }}
        text= "white"
        className="mb-2"
      >
      <Card.Body>
        <Card.Title>{partyGame.username == partyGame.currentTurn? 
          `It's your turn`: `It's ${partyGame.currentTurn}'s turn` }
        </Card.Title>
        < GameEventHistory />
      </Card.Body>
      </Card>
    </Collapse>
  );
}