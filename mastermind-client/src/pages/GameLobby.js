import { useEffect } from "react";
import { Navigate } from "react-router-dom";
import { useWebSocket } from "../component/WebSocketProvider";
import { useSelector, useDispatch } from "react-redux";
import { setReady, startPartyGame, updateRoster, setCurrentTurn } from "../redux/slices/partyGameSlice";
import styled from "styled-components";
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import ListGroup from 'react-bootstrap/ListGroup';

export default function GameLobby() {
  // websocket
  const {newMessage, sendWebSocketMessage } = useWebSocket();
  // redux
  const dispatch = useDispatch();
  const gameSession = useSelector((state => state.gameSession));
  const partyGame = useSelector((state => state.partyGame));

  // runs once for each message
  useEffect(() => {
    // if no new message, skip
    if (newMessage=== null) return;

    switch(newMessage.type) {
      case "Roster":
        dispatch(updateRoster(newMessage));
      break;
      case "GameStart":
        dispatch(startPartyGame(newMessage));
        dispatch(setCurrentTurn({currentTurn: newMessage.currentTurn}));
      break;
      default:
      break;
    }
  },[newMessage]);

  // if gameSession present and multiplayer, go to lobby
  if (gameSession.id !== null && gameSession.multiplayer && partyGame.started) {
    return <Navigate to="/party/game" />
  }

  // toggle ready
  const toggleReady = (e) => {
    e.preventDefault();
    // send ready message
    const message = {
      gameSessionId: gameSession.id,
      username: partyGame.username,
      ready: !partyGame.ready
    }
    sendWebSocketMessage(message, "/ready");
    // update client ready
    dispatch(setReady({ready:!partyGame.ready}));
  }

  return (
    <LobbyPageContainer>
      <Card >
        <Card.Body>
          <Card.Title>Game Lobby: {gameSession.id}</Card.Title>
          <ListGroup>
            {partyGame.roster && Object.entries(partyGame.roster).map(([player, ready]) => (
              <ListGroup.Item key={player}>
                {`${player} is ${ready ? 'ready ✅' : 'not ready 🛑'}`}
              </ListGroup.Item>
            ))}
          </ListGroup>
          <Button variant="primary" onClick={toggleReady} >{partyGame.ready ? "Click if you're not ready" : "Click if you're ready" }</Button>
        </Card.Body>
      </Card>
    </LobbyPageContainer>
  )
}

const LobbyPageContainer = styled.div`
  display: flex; 
  flex-direction: column;
  justify-content: center;
  padding-top: 250px;
  width: 100%;
  height: 100%;
  align-items: center; 
`