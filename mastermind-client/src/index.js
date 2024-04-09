import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { persistor, store} from './redux/store';
import { Provider } from 'react-redux'
import { PersistGate } from 'redux-persist/integration/react';
import { ApolloClient, InMemoryCache, ApolloProvider} from '@apollo/client';
import { WebSocketProvider } from './component/WebSocketProvider';

const root = ReactDOM.createRoot(document.getElementById('root'));

const client = new ApolloClient({
  uri: `http://localhost:8080/graphql`,
  cache: new InMemoryCache(),
});

root.render(
  <Provider store={store}>
    <PersistGate loading={null} persistor={persistor} >
      <ApolloProvider client={client}>
        <WebSocketProvider>
          <App />
        </WebSocketProvider>
      </ApolloProvider>
    </PersistGate>
  </Provider>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
