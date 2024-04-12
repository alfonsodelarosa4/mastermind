import { createSlice } from '@reduxjs/toolkit'

const initialState = {
  username: null,
  attempts: null,
  roster: {},
  started: false,
  ready: false,
  currentTurn: false,
  clientWon: false
};

export const partyGameSlice = createSlice({
  name: 'partyGameSlice',
  initialState,
  reducers: {
    setupPartyGame: (state, action) => {
      const {username, attempts, roster} = action.payload;
      state.username = username;
      state.attempts = attempts;
      state.roster = roster;
    },
    updateRoster: (state, action) => {
      const { roster } = action.payload;
      state.roster = roster;
    },
    setAttempts: (state, action) => {
      const { attempts } = action.payload;
      state.attempts = attempts;
    },
    startPartyGame: (state, action) => {
      const { roster } = action.payload;
      state.roster = roster;
      state.started = true;
    },
    setReady: (state, action) => {
      const { ready } = action.payload;
      state.ready = ready;
    },
    setCurrentTurn: (state, action) => {
      const {currentTurn} = action.payload;
      state.currentTurn = currentTurn;
    },
    setClientWon: (state, action) => {
      const {clientWon} = action.payload;
      state.clientWon = clientWon;
    },
    emptySession: (state) => {
      state.roster = {}
    }
  },
});

export const { setupPartyGame, updateRoster, setAttempts, startPartyGame, setReady, setCurrentTurn, setClientWon, emptySession } = partyGameSlice.actions

export default partyGameSlice.reducer