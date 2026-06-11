import {useState} from 'react';
import {FormattedMessage, useIntl} from 'react-intl';
import Card from 'react-bootstrap/Card';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';

import backend from '../../../backend';
import {BackLink, Errors, Success} from '../../common';

const DeliverTickets = () => {

    const [orderId, setOrderId] = useState('');
    const [creditCardNumber, setCreditCardNumber] = useState('');
    const [formValidated, setFormValidated] = useState(false);
    const [backendErrors, setBackendErrors] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null);
    const intl = useIntl();

    let form;

    const handleSubmit = async event => {

        event.preventDefault();

        if (form.checkValidity()) {
            const response = await backend.shoppingService.deliverTickets(
                Number(orderId),
                creditCardNumber
            );

            if (response.ok) {
                setBackendErrors(null);
                setSuccessMessage(intl.formatMessage(
                    {id: 'project.shopping.DeliverTickets.success'},
                    {orderId}
                ));
                setOrderId('');
                setCreditCardNumber('');
                setFormValidated(false);
            } else {
                setSuccessMessage(null);
                setBackendErrors(response.payload);
            }
        } else {
            setSuccessMessage(null);
            setBackendErrors(null);
            setFormValidated(true);
        }

    };

    return (
        <>
            <BackLink />

            <Errors errors={backendErrors} onClose={() => setBackendErrors(null)}/>
            <Success message={successMessage} onClose={() => setSuccessMessage(null)} />

            <Card className="mt-3 shadow-sm">
                <Card.Body>
                    <Card.Title className="text-center">
                        <FormattedMessage id="project.shopping.DeliverTickets.title" />
                    </Card.Title>
                    <Form ref={node => form = node}
                        noValidate validated={formValidated} onSubmit={e => handleSubmit(e)}>
                        <Form.Group as={Row} className="mb-3" controlId="orderId">
                            <Form.Label column md={4}>
                                <FormattedMessage id="project.global.fields.orderId" />
                            </Form.Label>
                            <Col md={4}>
                                <Form.Control type="number"
                                    min="1"
                                    step="1"
                                    value={orderId}
                                    onChange={e => setOrderId(e.target.value)}
                                    inputMode="numeric"
                                    required/>
                                <Form.Control.Feedback type="invalid">
                                    <FormattedMessage id='project.global.validator.required'/>
                                </Form.Control.Feedback>
                            </Col>
                        </Form.Group>
                        <Form.Group as={Row} className="mb-3" controlId="creditCardNumber">
                            <Form.Label column md={4}>
                                <FormattedMessage id="project.global.fields.creditCardNumber" />
                            </Form.Label>
                            <Col md={5}>
                                <Form.Control type="text"
                                    inputMode="numeric"
                                    maxLength="32"
                                    pattern="[0-9]{1,32}"
                                    value={creditCardNumber}
                                    onChange={e => setCreditCardNumber(e.target.value.trim().replace(/\D/g, ''))}
                                    required/>
                                <Form.Control.Feedback type="invalid">
                                    <FormattedMessage id='project.global.validator.creditCardNumber'/>
                                </Form.Control.Feedback>
                            </Col>
                        </Form.Group>
                        <Form.Group as={Row}>
                            <Col md={{ span: 4, offset: 4 }}>
                                <Button id="deliverButton" type="submit">
                                    <FormattedMessage id="project.shopping.DeliverTickets.submit" />
                                </Button>
                            </Col>
                        </Form.Group>
                    </Form>
                </Card.Body>
            </Card>
        </>
    );

};

export default DeliverTickets;
