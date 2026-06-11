import {useState, useEffect} from 'react';
import {useParams} from 'react-router';
import {FormattedMessage} from 'react-intl';
import Card from 'react-bootstrap/Card';

import backend from '../../../backend';
import {BackLink} from '../../common';

const MovieDetails = () => {

    const [movie, setMovie] = useState(null);
    const {id} = useParams();
    const movieId = Number(id);

    useEffect(() => {

        const findMovieById = async movieId => {
            if (!Number.isNaN(movieId)) {
                const response = await backend.catalogService.findMovieById(movieId);
                if (response.ok) {
                    setMovie(response.payload);
                }
            }
        }

        findMovieById(movieId);

    }, [movieId]);

    if (!movie) {
        return null;
    }

    return (
        <>
            <BackLink />

            <Card className="mt-3 shadow-sm">
                <Card.Body>

                    <Card.Title className="text-center">
                        {movie.title}
                    </Card.Title>

                    <Card.Subtitle className="text-muted mb-3">
                        {movie.duration} <FormattedMessage id="project.global.fields.minutes" />
                    </Card.Subtitle>

                    <Card.Text className="text-justify">
                        {movie.resume}
                    </Card.Text>

                </Card.Body>
            </Card>
        </>
    );

}

export default MovieDetails;
