import { useState } from "react";
import { Navigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import { updateGameSession } from "../redux/slices/gameSessionSlice";
import { updateSoloGame } from "../redux/slices/soloGameSlice";
import { setupPartyGame } from "../redux/slices/partyGameSlice";
import axios from 'axios';
import styled from "styled-components";
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Collapse from 'react-bootstrap/Collapse';
import Form from 'react-bootstrap/Form';
import Tab from 'react-bootstrap/Tab';
import Tabs from 'react-bootstrap/Tabs'

export default function GameStart() {
  // redux
  const dispatch = useDispatch();
  const gameSession = useSelector((state => state.gameSession));
  // form: create game session
  const [key, setKey] = useState('create');
  const [multiplayer, setMultiplayer] = useState("false");
  const [username, setUsername] = useState("");
  // form: join game session
  const [hostSesssionId, setHostSessionId] = useState("");

  // creates game session
  const startGameSession = async () => {
    // create request body for both solo game and multiplayer
    var requestBody = {};
    if (multiplayer === "true") {
      requestBody = {
        multiplayer: true,
        username:username
      };
    } else {
      requestBody = {
        multiplayer: false,
      }
    }
    // make REST request
    const response = await axios.post("http://localhost:8080/api/game-session",requestBody);
    const {data} = response;
    if (multiplayer === "true" ) {
      // if multiplayer game
      // update partyGame: roster, username, attempts
      dispatch(setupPartyGame(data));
    } else {
      // if solo
      // update soloGame: attempts
      console.log(data);
      dispatch(updateSoloGame(data));
    }
    // gameSession: id, multiplayer
    dispatch(updateGameSession(data));
  }

  // joins multiplayer game session
  const joinGameSession = async () => {
    const response = await axios.post("http://localhost:8080/api/join-game-session",{
      gameSessionId: hostSesssionId,
      username:username
    });
    const {data} = response;
    // update partyGame: roster, username, attempts
    dispatch(setupPartyGame(data));
    // gameSession: id, multiplayer
    dispatch(updateGameSession(data));
  }

  // if gameSession present and multiplayer, go to lobby
  if (gameSession.id !== null && gameSession.multiplayer) {
    return <Navigate to="party" />
  }

  // if gameSession present but no multiplayer, go to solo mastermind
  if (gameSession.id !== null && !gameSession.multiplayer) {
    return <Navigate to="solo/game" />
  }

  return (
    <StartPageContainer>
      <Tabs
        id="controlled-tab-example"
        activeKey={key}
        onSelect={(k) => setKey(k)}
        className="mb-3"
      >
        <Tab eventKey="create" title="Create Game">
          <Card style={{ width: '30rem' }}>
            <Card.Header>Mastermind by Alfonso De La Rosa</Card.Header>
            <Card.Body>
              <Card.Title>Play Mastermind</Card.Title>
              <Form >
                <Form.Group className="mb-3" controlId="name">
                  <Form.Label>Are you playing by yourself or with friends?</Form.Label>
                  <Form.Select onChange={(e)=>{const {value} = e.target; setMultiplayer(value)}} >
                    <option value="false">Solo</option>
                    <option value="true">With friends</option>
                  </Form.Select>
                </Form.Group>
                <Collapse in={multiplayer ==="true"}>
                <Form.Group className="mb-3" controlId="username">
                  <Form.Label>Enter username</Form.Label>
                  <Form.Control 
                    type="text"
                    name="username"
                    onChange={(e)=>{const {value} = e.target;setUsername(value)}}
                  />
                </Form.Group>
                </Collapse>
              </Form>
              <Collapse in={multiplayer === "false" || (multiplayer === "true" && username !== "") }>
                <Button variant="primary" onClick={startGameSession}>
                  Play Mastermind 😀
                </Button>
              </Collapse>
            </Card.Body>
          </Card>
        </Tab>
        <Tab eventKey="join" title="Join Game">
        <Card style={{ width: '30rem' }}>
            <Card.Header>Mastermind by Alfonso De La Rosa</Card.Header>
            <Card.Body>
              <Card.Title>Join someone's Mastermind game</Card.Title>
              <Form >
                <Form.Group className="mb-3" controlId="username">
                  <Form.Label>Enter game session code</Form.Label>
                  <Form.Control 
                    type="text"
                    name="username"
                    onChange={(e)=>{const {value} = e.target;setHostSessionId(value)}}
                  />
                </Form.Group>
                <Form.Group className="mb-3" controlId="username">
                  <Form.Label>Enter username</Form.Label>
                  <Form.Control 
                    type="text"
                    name="username"
                    onChange={(e)=>{const {value} = e.target;setUsername(value)}}
                  />
                </Form.Group>
              </Form>
              <Collapse in={ hostSesssionId !== "" && username !== "" }>
                <Button variant="primary" onClick={joinGameSession}>
                  Join 😀
                </Button>
              </Collapse>
            </Card.Body>
          </Card>
        </Tab>
      </Tabs>
    </StartPageContainer>
  )
}

const StartPageContainer = styled.div`
  display: flex;  //Establish flexbox layout
  flex-direction: column;
  justify-content: center;  /* Horizontally center content */
  padding-top: 250px;
  width: 100%;
  height: 100%;
  align-items: center; // Vertically center content 
`