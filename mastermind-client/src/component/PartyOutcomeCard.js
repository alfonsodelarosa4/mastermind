import { useWebSocket } from "./WebSocketProvider";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import Card from 'react-bootstrap/Card';
import Fade from 'react-bootstrap/Fade';
import { setAttempts } from "../redux/slices/partyGameSlice";

export default function SoloOutcomeCard () {
  // websocket
  const { newMessage } = useWebSocket();
  // redux
  const dispatch = useDispatch();
  const partyGame = useSelector((state => state.partyGame));
  // hooks
  const [cardProperties, setCardProperties] = useState({ color: "primary",title: "",body: "" });
  const [open, setOpen] = useState(false);

  // runs every time new websocket message received
  useEffect(() => {
    // if no new message, skip
    if (newMessage=== null) return;
    
    // handle new card
    switch(newMessage.type) {
      case "Feedback":
        // updates attempts if feedback was given to user
        if (newMessage.username === partyGame.username) dispatch(setAttempts(newMessage));
        setOpen(false);
        setCardProperties({
          color: "primary",
          title: "Close, but not quite",
          body: newMessage.content
        });
        setTimeout(() => {
          setOpen(true);
        }, 500);
      break
      case "GameWon":
        setOpen(false);
        const title = newMessage.username === partyGame.username? "You won! 🙌" : `${newMessage.username} won 😅`;
        const body = newMessage.username === partyGame.username? `You won the game! ${newMessage.content}` : `${newMessage.username} won the game! ${newMessage.content}`;
        setCardProperties({
          color: "success",
          title: title,
          body: body
        });
        setTimeout(() => {
          setOpen(true);
        }, 500);
      break
      case "GameLost":
        setOpen(false);
        setCardProperties({
          color: "danger",
          title: "Game lost. 😅",
          body: newMessage.content
        });
        setTimeout(() => {
          setOpen(true);
        }, 500);
      break
      default:
    }
  }, [newMessage]);

  return (
    <Fade in={open}>
      <Card
          bg={cardProperties.color}
          style={{ width: '30rem', margin:"5px", minHeight:'4rem' }}
          text= "white"
          className="mb-2"
        >
        <Card.Body>
          <Card.Title>{cardProperties.title} </Card.Title>
          <Card.Text>{cardProperties.body}</Card.Text>
        </Card.Body>
      </Card>
    </Fade>
  )
}