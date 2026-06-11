import * as actionTypes from './actionTypes.js';

export const getBillboardCompleted = billboard => ({
    type: actionTypes.GET_BILLBOARD_COMPLETED,
    billboard
});

export const clearBillboard = () => ({
    type: actionTypes.CLEAR_BILLBOARD,
});

export const setBillboardDate = billboardDate => ({
    type: actionTypes.SET_BILLBOARD_DATE,
    billboardDate
});