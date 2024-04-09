import { Navigate } from "react-router-dom";
import { useSelector, useDispatch } from "react-redux";
import axios from 'axios'
import styled from "styled-components";
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import { updateGameSession } from "../redux/slices/gameSessionSlice";

export default function GameStart() {
  const dispatch = useDispatch();
  const gameSession = useSelector((state => state.gameSession));

  // creates game session
  const startGameSession = async () => {
    const response = await axios.post(`${process.env.REACT_APP_BACKEND_URL}/api/game-session`);
    const {data} = response;    
    console.log(response);
    dispatch(updateGameSession(data));
  }

  // if gameSession present, go to Mastermind page
  if (gameSession.id !== null) {
    console.log(gameSession);
    return <Navigate to="game" />
  }

  return (
    <StartPageContainer>
      <Card className="text-center" style={{ width: '30rem' }}>
        <Card.Header>Mastermind by Alfonso De La Rosa</Card.Header>
        <Card.Body>
          <Card.Title>Play Mastermind!</Card.Title>
          <Card.Text>
            Guess the correct number 🤔
          </Card.Text>
          <Button variant="primary" onClick={startGameSession}>
            Play Mastermind 😀
          </Button>
        </Card.Body>
      </Card>
    </StartPageContainer>
  )
}

const StartPageContainer = styled.div`
  display: flex;  //Establish flexbox layout
  justify-content: center;  /* Horizontally center content */
  padding-top: 250px;
  width: 100%;
  height: 100%;
  align-items: center; // Vertically center content 
`