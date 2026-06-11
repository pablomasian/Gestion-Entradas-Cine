import {FormattedMessage} from 'react-intl';
import Table from 'react-bootstrap/Table';

import {MovieLink, SessionLink} from '../../common';

const Movies = ({movies}) => (

    <Table striped hover>

        <thead>
        <tr>
            <th>
                <FormattedMessage id='project.global.fields.name'/>
            </th>
            <th>
                <FormattedMessage id='project.global.fields.hours'/>
            </th>
        </tr>
        </thead>

        <tbody>
        {movies.map(movie =>
            <tr key={movie.movieId}>
                <td><MovieLink id={movie.movieId} name={movie.movieTitle}/></td>
                <td>
                    {movie.sessions && movie.sessions.map(session =>
                        <SessionLink 
                            key={session.sessionId} 
                            id={session.sessionId} 
                            date={session.date}
                            movieTitle={movie.movieTitle}
                        />
                    )}
                </td>
            </tr>
        )}
        </tbody>

    </Table>

);

export default Movies;
