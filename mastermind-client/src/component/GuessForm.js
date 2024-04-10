import { useState, useEffect } from "react"
import { useWebSocket } from "../component/WebSocketProvider"
import { useSelector } from "react-redux";
import styled from "styled-components";
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import Collapse from 'react-bootstrap/Collapse';
import Button from 'react-bootstrap/Button';
import GameEventHistory from "./GameEventHistory";

// allows user to make guess
export default function GuessForm() {
  const gameSession = useSelector((state => state.gameSession));
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
    const message = {
      gameSessionId: gameSession.id,
      guess: guess
    }
    sendWebSocketMessage(message,"/guess")
  }

  // handles guess
  const handleGuess = (e) => {
    const { value } = e.target;
    setGuess(value);
  }

  return (
    <Collapse in={show}>
      <Card style={{ width: '30rem' }}>
        <Card.Header>{gameSession.attempts} attempts left</Card.Header>      
        <Card.Body>
          <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3" controlId="name">
              <Form.Label>Guess the number</Form.Label>
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
              <GameEventHistory />
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