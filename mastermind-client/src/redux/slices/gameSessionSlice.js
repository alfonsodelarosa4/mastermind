import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  id: null,
  multiplayer: false
};

export const gameSessionSlice = createSlice({
  name: 'gameSession',
  initialState,
  reducers: {
    updateGameSession: (state, action) => {
      const { id, multiplayer } = action.payload;
      state.id = id;
      state.multiplayer = multiplayer;
    },
    emptyGameSession: (state) => {
      state.id = null;
      state.multiplayer = false;
    }
  },
});

export const { updateGameSession, emptyGameSession } = gameSessionSlice.actions

export default gameSessionSlice.reducer