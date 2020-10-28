import {ThunkAction} from "redux-thunk";
import {Action, Dispatch} from "redux";
import {RootState} from "../../store";
import {SetLoginStatus} from "./loginReducer";
import {UserVo} from "../../entity/UserVo";

export const setUser = (user: UserVo | null = null): ThunkAction<void, RootState, unknown, Action> => {
    return (dispatch: Dispatch) => {
        try {
            dispatch({
                type: SetLoginStatus,
                data: user
            })
        } catch (e) {
        }
    }
}