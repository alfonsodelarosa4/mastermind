import { useWebSocket } from "./WebSocketProvider";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import Card from 'react-bootstrap/Card';
import Fade from 'react-bootstrap/Fade';

export default function PlayerGuessCard ({enabled}) {
  // websocket
  const { newMessage } = useWebSocket();
  // redux
  const partyGame = useSelector((state => state.partyGame));
  // hooks
  const [open, setOpen] = useState(false);
  const [playerGuess, setPlayerGuess] = useState({username:"", guess:""})

  // runs every time new websocket message received
  useEffect(() => {
    // if no new message, skip
    if (newMessage=== null) return;
    
    // handle new card
    switch(newMessage.type) {
      case "GameWon":
        setOpen(false);
      break
      case "GameLost":
        setOpen(false);
      break
      case "PlayerGuess":
        if (partyGame.username === newMessage.username) {
          setOpen(false);
          setPlayerGuess({username:"", guess:""});
        } else {
          setOpen(true);
          setPlayerGuess({
            username:newMessage.username,
            guess: newMessage.guess
          });
        }

      break;
      default:
      break;
    }
  }, [newMessage]);

  return (
    <Fade in={open}>
      <Card
          bg="light"
          style={{ width: '30rem', margin:"5px", minHeight:'4rem' }}
          text= "dark"
          className="mb-2"
        >
        <Card.Body>
          <Card.Title>{`${playerGuess.username}'s Guess`} </Card.Title>
          <Card.Text>{`${playerGuess.username} guessed ${playerGuess.guess}`}</Card.Text>
        </Card.Body>
      </Card>
    </Fade>
  )
}