import { gql } from '@apollo/client';

export const GET_GAMEEVENT_BY_GAMESESSIONID = gql`
  query ($gameSessionId: String!) {
    getGameEventByGameSessionId(gameSessionId: $gameSessionId) {
        id
        timestamp
        description
    }
  }
`