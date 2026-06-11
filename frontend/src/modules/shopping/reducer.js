import {combineReducers} from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    orderSearch: null
};

const orderSearch = (state = initialState.orderSearch, action) => {

    switch (action.type) {

        case actionTypes.FIND_ORDERS_COMPLETED:
            return action.orderSearch;

        case actionTypes.CLEAR_ORDER_SEARCH:
            return initialState.orderSearch;

        default:
            return state;

    }

};

const reducer = combineReducers({
    orderSearch
});

export default reducer;
