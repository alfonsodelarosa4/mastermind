import { useWebSocket } from "./WebSocketProvider";
import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import Spinner from 'react-bootstrap/Spinner';
import Card from 'react-bootstrap/Card';
import Fade from 'react-bootstrap/Fade';
import { updateAttempts } from "../redux/slices/gameSessionSlice";

export default function OutcomeCard () {
  const { isLoading, newMessage } = useWebSocket();
  const dispatch = useDispatch();
  const [title, setTitle] = useState("");
  const [body, setBody] = useState("");
  const [color, setColor] = useState("");
  const [open, setOpen] = useState(false);

  useEffect(() => {
    // if no new message, skip
    if (newMessage=== null) return;
    
    // handle new card
    let cardProperties = null;
    let isOutcomeMessage = false;
    switch(newMessage.type) {
      case "Feedback":
        let body = "";
        
        if( newMessage.correctNumber === 0 && newMessage.correctLocation === 0) {
          body = `All were incorrect. ${newMessage.attempts} attempts left.`;
        } else {
          body = `${newMessage.correctNumber} correct number and ${newMessage.correctLocation} correct location. ${newMessage.attempts} attempts left.`;
        }

        dispatch(updateAttempts(newMessage) )

        cardProperties = {
          color: "primary",
          title: "Close, but not quiet",
          body: body
        };
        isOutcomeMessage = true;
      break
      case "GameWon":
        cardProperties = {
          color: "success",
          title: "Game Won! 😄",
          body: newMessage.content
        };
        isOutcomeMessage = true;
      break
      case "GameLost":
        cardProperties = {
          color: "danger",
          title: "Game lost 😅",
          body: newMessage.content
        };
        isOutcomeMessage = true;
      break
      default:
    }
    if (isOutcomeMessage) {
      setTitle(cardProperties.title);
      setBody(cardProperties.body);
      setColor(cardProperties.color);
    }

    // create card animation
    setOpen(false);
    setTimeout(() => {
      setOpen(true);
    }, 500);

  }, [newMessage]);

  // displays spinner if message just sent and waiting for response
  if (isLoading) return ( 
    <Spinner animation="border" role="status">
      <span className="visually-hidden">Loading...</span>
    </Spinner>
  );

  // does not display anything if new message is empty
  if (title === "" || body === "") return (<></>);

  return (
    <Fade in={open}>
      <Card
          bg={color}
          style={{ width: '30rem', margin:"5px", minHeight:'4rem' }}
          text= "white"
          className="mb-2"
        >
        
        <Card.Body>
          <Card.Title>{title} </Card.Title>
          <Card.Text>{body}</Card.Text>
        </Card.Body>
      </Card>
    </Fade>
  )
}