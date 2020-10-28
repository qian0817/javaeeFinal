import {applyMiddleware, combineReducers, createStore} from "redux";
import {composeWithDevTools} from "redux-devtools-extension";
import loginReducer from './reducers/login/loginReducer'
import thunk from 'redux-thunk'


const reducer = combineReducers({
    login: loginReducer
})

export type RootState = ReturnType<typeof reducer>
const store = createStore(reducer, composeWithDevTools(applyMiddleware(thunk)))

export default store