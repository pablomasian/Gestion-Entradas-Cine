import {useState, useEffect} from 'react';
import {useParams, useLocation, useNavigate} from 'react-router';
import {useSelector} from 'react-redux';
import {FormattedMessage, FormattedDate, FormattedTime, useIntl} from 'react-intl';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';

import backend from '../../../backend';
import users from '../../users';
import {BackLink, Errors} from '../../common';

const SessionDetails = () => {

    const location = useLocation();
    const navigate = useNavigate();
    const [session, setSession] = useState(location.state?.sessionPreview || null);
    const [quantity, setQuantity] = useState(1);
    const [creditCardNumber, setCreditCardNumber] = useState('');
    const [formValidated, setFormValidated] = useState(false);
    const [backendErrors, setBackendErrors] = useState(null);
    const [buyBackendErrors, setBuyBackendErrors] = useState(null);
    const user = useSelector(users.selectors.getUser);
    const intl = useIntl();
    const {id} = useParams();
    const sessionId = Number(id);
    const isSpectator = user?.role === 'ESPECTATOR';
    const canBuy = isSpectator && session && !backendErrors && session.localitiesLeft > 0;

    let form;

    useEffect(() => {

        setSession(location.state?.sessionPreview || null);
        setQuantity(1);
        setCreditCardNumber('');
        setFormValidated(false);
        setBackendErrors(null);
        setBuyBackendErrors(null);

        const findSessionById = async sessionId => {
            if (!Number.isNaN(sessionId)) {
                const response = await backend.catalogService.findSessionById(sessionId);
                if (response && response.ok) {
                    setSession(prev => ({...(prev || {}), ...response.payload}));
                    setQuantity(prev => Math.min(prev, response.payload.localitiesLeft));
                } else if (response) {
                    setBackendErrors(response.payload);
                }
            }
        }

        findSessionById(sessionId);

    }, [sessionId]);

    const handleSubmit = async event => {

        event.preventDefault();

        if (form.checkValidity()) {

            const response = await backend.shoppingService.buy(sessionId, quantity,
                creditCardNumber);

            if (response.ok) {
                setBuyBackendErrors(null);
                setQuantity(1);
                setCreditCardNumber('');
                setSession(prev => prev ? {...prev, localitiesLeft: prev.localitiesLeft - quantity} : prev);
                navigate(`/catalog/buy-success/${response.payload}`);
            } else {
                setBuyBackendErrors(response.payload);
            }

        } else {

            setBuyBackendErrors(null);
            setFormValidated(true);

        }

    }

    const maxQuantity = session ? Math.min(session.localitiesLeft, 10) : 1;

    return (
        <>
            <BackLink />

            <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>

            <Card className="mt-3 shadow-sm">
                <Card.Body>
                    {session ? (
                        <>
                            <Card.Title className="text-center">
                                <span id="movieTitle">
                                {session.movieTitle}
                                </span>
                            </Card.Title>
                            <Card.Subtitle id="sessionDate" className="text-muted mb-3">
                                <FormattedDate value={new Date(session.date)} />
                                {' '}
                                <FormattedTime value={new Date(session.date)} />
                            </Card.Subtitle>

                            <Card.Text id="runtime">
                                <strong><FormattedMessage id="project.global.fields.duration" /></strong>: {session.movieDuration ?? '-'} {session.movieDuration != null && <FormattedMessage id="project.global.fields.minutes" />}
                            </Card.Text>

                            <Card.Text id="room">
                                <strong><FormattedMessage id="project.global.fields.room" /></strong>: {session.roomName ?? '-'}
                            </Card.Text>

                            <Card.Text id="price">
                                <strong><FormattedMessage id="project.global.fields.price" /></strong>: {session.price != null ? `€${session.price}` : '-'}
                            </Card.Text>

                            <Card.Text id="availableSeats">
                                <strong><FormattedMessage id="project.catalog.fields.availableSeats" /></strong>: {session.localitiesLeft ?? '-'}
                            </Card.Text>

                            {canBuy && (
                                <>
                                    <Errors errors={buyBackendErrors}
                                        onClose={() => setBuyBackendErrors(null)}/>

                                    <Card className="mt-4 bg-light border-0">
                                        <Card.Body>
                                            <Card.Title as="h6" id="buyForm" classname="mb-3">
                                                <FormattedMessage id="project.shopping.Buy.title" />
                                            </Card.Title>

                                            <Form ref={node => form = node}
                                                noValidate validated={formValidated} onSubmit={e => handleSubmit(e)}>
                                                <Form.Group as={Row} className="mb-3" controlId="quantity">
                                                    <Form.Label column md={4}>
                                                        <FormattedMessage id="project.global.fields.quantity" />
                                                    </Form.Label>
                                                    <Col md={3}>
                                                        <Form.Select id="quantity" value={quantity}
                                                            onChange={e => setQuantity(Number(e.target.value))}
                                                            min={1}
                                                            max={10}
                                                            required>
                                                            {Array.from({length: maxQuantity}, (_, index) => index + 1)
                                                                .map(option =>
                                                                    <option key={option} value={option}>{option}</option>
                                                                )}
                                                        </Form.Select>
                                                    </Col>
                                                </Form.Group>

                                                <Form.Group as={Row} className="mb-3" controlId="creditCardNumber">
                                                    <Form.Label column md={4}>
                                                        <FormattedMessage id="project.global.fields.creditCardNumber" />
                                                    </Form.Label>
                                                    <Col md={5}>
                                                        <Form.Control id="creditCardNumber" type="text"
                                                            inputMode="numeric"
                                                            maxLength={32}
                                                            value={creditCardNumber}
                                                            onChange={e => setCreditCardNumber(e.target.value.trim().replace(/\D/g, ''))}
                                                            pattern="[0-9]{16,32}"
                                                            required/>
                                                        <Form.Control.Feedback type="invalid">
                                                            <FormattedMessage id='project.global.validator.creditCardNumber'/>
                                                        </Form.Control.Feedback>
                                                    </Col>
                                                </Form.Group>

                                                <Form.Group as={Row}>
                                                    <Col md={{ span: 4, offset: 4 }}>
                                                        <Button id="buyBotton" type="submit">
                                                            <FormattedMessage id="project.shopping.Buy.buy" />
                                                        </Button>
                                                    </Col>
                                                </Form.Group>
                                            </Form>
                                        </Card.Body>
                                    </Card>
                                </>
                            )}
                        </>
                    ) : (
                        <div className="text-muted">
                            <FormattedMessage id="project.global.loading" />
                        </div>
                    )}
                </Card.Body>
            </Card>
        </>
    );

}

export default SessionDetails;
