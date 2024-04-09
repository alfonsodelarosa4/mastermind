import { useSelector } from "react-redux";
import styled from "styled-components";
import GuessForm from "../component/GuessForm";
import Button from 'react-bootstrap/Button';
import OutcomeCard from "../component/OutcomeCard";
import { CelebrateWin } from "../component/CelebrateWin";

export default function MastermindPage() {
  const gameSession = useSelector((state => state.gameSession));
  // if now game session, take user back to home page
  if (gameSession.id == null) {
    console.log(gameSession.id);
    return (<Button href="/" >Go back home</Button>)
  }

  return (
    <GamePageContainer>
      <CelebrateWin />
      <OutcomeCard />
      <GuessForm />
    </GamePageContainer>
  )
}

const GamePageContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;  /* Horizontally center content */
  padding-top: 250px;
  width: 100%;
  height: 100%;
  align-items: center; // Vertically center content 
`