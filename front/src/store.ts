import {applyMiddleware, combineReducers, createStore} from "redux";
import {composeWithDevTools} from "redux-devtools-extension";
import loginReducer from './reducers/login/loginReducer'
import thunk from 'redux-thunk'
import visibleReducer from "./reducers/loginFormVisible/loginFormVisibleReducer";


const reducer = combineReducers({
    login: loginReducer,
    loginFormVisible: visibleReducer
})

export type RootState = ReturnType<typeof reducer>
const store = createStore(reducer, composeWithDevTools(applyMiddleware(thunk)))

export default store