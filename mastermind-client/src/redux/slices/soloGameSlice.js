import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  attempts: null,
};

export const soloGameSlice = createSlice({
  name: 'soloGame',
  initialState,
  reducers: {
    updateSoloGame: (state, action) => {
      const { attempts } = action.payload;
      state.attempts = attempts;
    },
    deleteSoloGame: (state) => {
      state.attempts = null;
    }
  },
});

export const { updateSoloGame, deleteSoloGame } = soloGameSlice.actions

export default soloGameSlice.reducer