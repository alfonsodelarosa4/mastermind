import { configureStore } from '@reduxjs/toolkit'
import gameSessionReducer from './slices/gameSessionSlice';
import soloGameSliceReducer from './slices/soloGameSlice';
import partyGameReducer from './slices/partyGameSlice';

export const store = configureStore({
  reducer: {
    gameSession: gameSessionReducer,
    soloGame: soloGameSliceReducer,
    partyGame: partyGameReducer
  }
})