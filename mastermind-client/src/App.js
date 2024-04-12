import './App.css';
import { BrowserRouter, Route, Routes, Outlet } from "react-router-dom";
import styled from "styled-components";
import "bootstrap/dist/css/bootstrap.min.css";
import GameStart from './pages/GameStart';
import SoloMastermindPage from './pages/SoloMastermindPage';
import GameLobby from './pages/GameLobby';
import PartyMastermindPage from './pages/PartyMastermindPage';

function App() {
  return (
    <Background>
      <BrowserRouter> 
        <Routes>
          <Route path="/" element={
            <PageContainer >
              <Outlet />
            </PageContainer>
          }>
            <Route index element={<GameStart />} />
            <Route path="party" >
              <Route index element={<GameLobby />} />
              <Route path="game" element={<PartyMastermindPage />} />
            </Route>
            <Route path="solo">
              <Route path="game" element={<SoloMastermindPage />} />
            </Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </Background>
  );
}

export default App;

const Background = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
`

const PageContainer = styled.div`
  max-width: 900px;
  margin: auto;
`