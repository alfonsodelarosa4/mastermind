import { useState, useEffect } from "react";
import { useWebSocket } from "../component/WebSocketProvider"
import { useSelector } from "react-redux";
import styled from "styled-components";
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import Collapse from 'react-bootstrap/Collapse';
import Button from 'react-bootstrap/Button';
import GameEventHistory from "./GameEventHistory";

// allows user to make guess
export default function GuessForm({enabled = true, attempts, enableHistory=true}) {
  // redux
  const gameSession = useSelector((state => state.gameSession));
  const partyGame = useSelector((state => state.partyGame));
  // websocket
  const { newMessage, sendWebSocketMessage } = useWebSocket();
  const [guess, setGuess] = useState(null);
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

  // submits guess
  const handleSubmit = (e) => {
    e.preventDefault();
    var message = {};
    if(gameSession.multiplayer) {
      message = {
        username: partyGame.username,
        gameSessionId: gameSession.id,
        guess: guess
      }
    } else {
      message = {
        gameSessionId: gameSession.id,
        guess: guess
      }
    }
    sendWebSocketMessage(message,"/guess")
  }

  // handles guess
  const handleGuess = (e) => {
    const { value } = e.target;
    setGuess(value);
  }

  return (
    <Collapse in={show && enabled}>
      <Card style={{ width: '30rem' }}>
        <Card.Header>{attempts} attempts left</Card.Header>      
        <Card.Body>
          <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3" controlId="guess">
            <Form.Label>Guess the combination</Form.Label>
            <Form.Control 
              type="text"
              name="guess"
              onChange={handleGuess}
              placeholder="Type here"
            />
          </Form.Group>
          <ButtonSection>
              <Button variant="primary" type="submit">
                Submit answer
              </Button>
              {enableHistory && (<GameEventHistory />)}              
            </ButtonSection>
        </Form>
        </Card.Body>
      </Card>
    </Collapse>
  )
}

const ButtonSection = styled.div`
  display: flex;
  justify-content: space-between;
`