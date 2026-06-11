import * as actionTypes from './actionTypes';

export const findOrdersCompleted = orderSearch => ({
    type: actionTypes.FIND_ORDERS_COMPLETED,
    orderSearch
});

export const clearOrderSearch = () => ({
    type: actionTypes.CLEAR_ORDER_SEARCH
});
