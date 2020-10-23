import {ThunkAction} from "redux-thunk";
import {Action, Dispatch} from "redux";
import {RootState} from "../../store";
import {SetLoginStatus} from "./loginReducer";

export const checkLogin = (): ThunkAction<void, RootState, unknown, Action> => {
    return async (dispatch: Dispatch) => {
        try {
            const token = localStorage.getItem("JWT-TOKEN")
            dispatch({
                type: SetLoginStatus,
                data: token != null
            })
        } catch (e) {
        }
    }
}

export const logout = (): ThunkAction<void, RootState, unknown, Action> => {
    return async (dispatch: Dispatch) => {
        try {
            localStorage.removeItem("JWT-TOKEN")
            dispatch({
                type: SetLoginStatus,
                data: false
            })
        } catch (e) {
        }
    }
}