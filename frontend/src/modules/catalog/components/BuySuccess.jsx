import {useParams} from 'react-router';
import {FormattedMessage} from 'react-intl';
import Alert from 'react-bootstrap/Alert';
import Card from 'react-bootstrap/Card';

import {BackLink} from '../../common';

const BuySuccess = () => {

    const {orderId} = useParams();

    return (
        <>
            <BackLink />

            <Card className="mt-3 shadow-sm">
                <Card.Body>
                    <Card.Title className="text-center">
                        <FormattedMessage id="project.shopping.Buy.title" />
                    </Card.Title>

                    <Alert variant="success" className="mt-4 mb-0">
                        <FormattedMessage id="project.shopping.Buy.success"
                            values={{orderId}} />
                    </Alert>

                    <p className="mt-3 mb-0">
                        <FormattedMessage id="project.shopping.Buy.instructions" />
                    </p>
                </Card.Body>
            </Card>
        </>
    );

}

export default BuySuccess;