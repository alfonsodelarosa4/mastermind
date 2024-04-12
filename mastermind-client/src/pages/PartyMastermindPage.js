import { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { useWebSocket } from "../component/WebSocketProvider";
import styled from "styled-components";
import GuessForm from "../component/GuessForm";
import Button from 'react-bootstrap/Button';
import PartyOutcomeCard from "../component/PartyOutcomeCard";
import PlayerGuessCard from "../component/PlayerGuessCard";
import CelebrateWin from "../component/CelebrateWin";
import { setCurrentTurn, setClientWon } from "../redux/slices/partyGameSlice";
import TurnDisplay from "../component/TurnDisplay";

export default function PartyMastermindPage() {
  const dispatch = useDispatch();
  const {newMessage } = useWebSocket();
  const gameSession = useSelector((state => state.gameSession));
  const partyGame = useSelector((state => state.partyGame));

  // runs every time new websocket message received
  useEffect(() => {
    // if no new message, skip
    if (newMessage=== null) return;

    switch(newMessage.type) {
      case "NextTurn":
        dispatch(setCurrentTurn({currentTurn: newMessage.currentTurn}));
      break;
      case "GameWon":
        const didClientWin = newMessage.username === partyGame.username;
        dispatch(setClientWon({clientWon: didClientWin}));

      break;
      default:
      break;
    }
  },[newMessage]);


  if (gameSession.id == null) {
    return (
      <GamePageContainer>  
        <Button href="/" >Go back home</Button>
      </GamePageContainer>
    )
  }

  return (
    <GamePageContainer>    
      <CelebrateWin enabled={partyGame.clientWon} />
      <PlayerGuessCard enabled={!(partyGame.currentTurn === partyGame.username) }/>
      <PartyOutcomeCard />
      <TurnDisplay />
      <GuessForm 
        enabled={partyGame.currentTurn === partyGame.username} 
        attempts={partyGame.attempts}
        enableHistory={false}/>
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