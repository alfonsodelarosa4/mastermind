import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  id: null,
  attempts: null,
};

export const gameSessionSlice = createSlice({
  name: 'gameSession',
  initialState,
  reducers: {
    updateGameSession: (state, action) => {
      const { id, attempts } = action.payload;
      state.id = id;
      state.attempts = attempts;
    },
    updateAttempts: (state, action) => {
      const { attempts } = action.payload;
      state.attempts = attempts;
    }
  },
});

export const { updateGameSession, updateAttempts } = gameSessionSlice.actions

export default gameSessionSlice.reducer