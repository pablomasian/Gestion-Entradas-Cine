import {appFetch} from "./appFetch.js";

export const getBillboard = async (date) => {

    let path = date ? `/catalog/billboard?date=${date}`
        : '/catalog/billboard';

    return await appFetch('GET', path);
}

export const findMovieById = async id => await appFetch('GET', `/catalog/movies/${id}`);

export const findSessionById = async id => await appFetch('GET', `/catalog/sessions/${id}`);