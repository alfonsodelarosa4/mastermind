import { useState, useEffect } from "react";
import { useSelector } from "react-redux";
import { GET_GAMEEVENT_BY_GAMESESSIONID } from "../graphql/queries";
import { useLazyQuery } from '@apollo/client';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import Modal from 'react-bootstrap/Modal';
import { timeSince } from "../utils/time";

// displays game event history as modal
export default function GameEventHistory() {
  const gameSession = useSelector((state) => state.gameSession);
  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  return (
  <>
    <Button variant="primary" onClick={handleShow}>
      Show game history
    </Button>
    <Modal show={show} onHide={handleClose} >
      <Modal.Header closeButton>
        <Modal.Title>{`Game Session History: #${gameSession.id}`}</Modal.Title>
      </Modal.Header>
      <Modal.Body style={{ maxHeight: '300px', overflowY: 'auto' }}>
        <GameEventList gameSessionId={gameSession.id} show={show} />
      </Modal.Body>
    </Modal>
  </>
  )
}

// shows list of events
function GameEventList({ gameSessionId, show }) {
  const [gameEvents, setGameEvents] = useState([]);
  // defines graphql request
  const [fetch, { loading, error, data, refetch }]  = useLazyQuery(
    GET_GAMEEVENT_BY_GAMESESSIONID);
  
  // makes graphql request at start of render
  useEffect(() => {
    fetch({
      variables: { gameSessionId: gameSessionId },
    });
  }, [fetch, gameSessionId]);

  // makes graphql request when new message is received
  useEffect(() => {
    if (show) {
      refetch();
    }
  }, [refetch, show]);

  // whenever new data is received, it must be processed
  useEffect(() => {
    if (data && data.getGameEventByGameSessionId) {
      const sortedEvents = processGameEvents(data.getGameEventByGameSessionId);
      setGameEvents(sortedEvents);
    }
  }, [data]);

  // function to process and sort game events
  const processGameEvents = (recentGameEvents) => {
    return recentGameEvents
      .slice() 
      .sort((a, b) => new Date(a.timestamp) - new Date(b.timestamp))
      .map(({ id, timestamp, description }) => ({
        id,
        timestamp,
        description,
      }));
  };
  // if loading, shows loading
  if (loading) return <p>Loading...</p>;
  // if error, shows error
  if (error) return <p>Error : {error.message}</p>;

  return (
    <Row xs={1} lg={1} className="g-4">
      {gameEvents.map(({ id, timestamp, description }) => (
        <Col key={id} className="d-flex justify-content-center">
          <Card
            bg={'primary'}
            style={{ width: '25rem', margin: '5px' }}
            text="white"
            className="mb-2"
          >
            <Card.Body>
              <Card.Text>{description}</Card.Text>
              
            </Card.Body>
            <Card.Footer>{ timeSince(timestamp)}</Card.Footer>
          </Card>
        </Col>
      ))}
    </Row>
  );
}
