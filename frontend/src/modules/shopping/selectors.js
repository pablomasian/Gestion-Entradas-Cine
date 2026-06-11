const getModuleState = state => state.shopping;

export const getOrderSearch = state =>
    getModuleState(state).orderSearch;
