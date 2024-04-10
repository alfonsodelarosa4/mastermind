import { configureStore } from '@reduxjs/toolkit'
import gameSessionReducer from './slices/gameSessionSlice';

export const store = configureStore({
  reducer: {
    gameSession: gameSessionReducer
  }
})