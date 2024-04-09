import { configureStore, combineReducers } from '@reduxjs/toolkit'
import gameSessionReducer from './slices/gameSessionSlice';
import storageSession from 'redux-persist/lib/storage/session'
import { persistReducer, persistStore } from 'redux-persist';
import {thunk} from 'redux-thunk';

const persistConfig = {
  key: 'root',
  storage: storageSession,
}

const rootReducer = combineReducers({ 
  gameSession: gameSessionReducer,
})

const persistedReducer = persistReducer(persistConfig, rootReducer)

export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) => {
    return [thunk]
  }
})

export const persistor = persistStore(store)