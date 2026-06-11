import {combineReducers} from 'redux';
import * as actionTypes from './actionTypes';

const initialState = {
    billboard: [],
    billboardDate: ''
};

const billboard = (state = initialState.billboard, action) => {

    switch (action.type) {

        case actionTypes.GET_BILLBOARD_COMPLETED:
            return action.billboard;

        case actionTypes.CLEAR_BILLBOARD:
            return initialState.billboard;

        default:
            return state;
    }
};

const billboardDate = (state = initialState.billboardDate, action) => {

    switch (action.type) {

        case actionTypes.SET_BILLBOARD_DATE:
            return action.billboardDate;


        default:
            return state;
    }
};

export default combineReducers({
    billboard,
    billboardDate
});