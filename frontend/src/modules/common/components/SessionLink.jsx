import {Link} from 'react-router';
import {FormattedTime} from 'react-intl';

const SessionLink = ({id, date, movieTitle}) => {
    return (
        <Link
            to={`/catalog/session-details/${id}`}
            state={{sessionPreview: {id, date, movieTitle}}}
            className="me-2"
        >
            <FormattedTime value={new Date(date)} />
        </Link>
    );
}

export default SessionLink;
