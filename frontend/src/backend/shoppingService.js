import {appFetch} from './appFetch.js';

export const buy = async (sessionId, quantity, creditCardNumber) =>
    await appFetch('POST', '/orders/buy', {sessionId, quantity, creditCardNumber});

export const findOrders = async ({page}) =>
    await appFetch('GET', `/orders?page=${page}`);

export const deliverTickets = async (orderId, creditCardNumber) =>
    await appFetch('POST', `/orders/${orderId}/deliver`, {creditCardNumber});
