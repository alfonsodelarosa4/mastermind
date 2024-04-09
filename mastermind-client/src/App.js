import './App.css';
import { BrowserRouter, Route, Routes, Outlet } from "react-router-dom";
import styled from "styled-components";
import "bootstrap/dist/css/bootstrap.min.css";
import GameStart from './pages/GameStart';
import MastermindPage from './pages/MastermindPage';

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
            <Route path="game" element={<MastermindPage />} />
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