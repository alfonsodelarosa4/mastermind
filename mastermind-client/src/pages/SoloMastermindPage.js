import { useSelector } from "react-redux";
import styled from "styled-components";
import GuessForm from "../component/GuessForm";
import Button from 'react-bootstrap/Button';
import SoloOutcomeCard from "../component/SoloOutcomeCard";
import CelebrateWin from "../component/CelebrateWin";

export default function SoloMastermindPage() {
  const gameSession = useSelector((state => state.gameSession));
  const soloGame = useSelector((state) => state.soloGame)

  if (gameSession.id == null) {
    return (
      <GamePageContainer>  
        <Button href="/" >Go back home</Button>
      </GamePageContainer>
    )
  }
  return (
    <GamePageContainer>    
      <CelebrateWin />
      <SoloOutcomeCard />
      <GuessForm attempts={soloGame.attempts}/>
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