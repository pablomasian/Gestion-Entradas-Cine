import Alert from 'react-bootstrap/Alert';

const Success = ({message, onClose}) => message && (
    <Alert id="success" variant="success" onClose={() => onClose()} dismissible>
        {message}
    </Alert>
);

export default Success;
