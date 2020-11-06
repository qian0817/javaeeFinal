import {ThunkAction} from "redux-thunk";
import {RootState} from "../../store";
import {Action, Dispatch} from "redux";

export const SetVisible = "setVisible";

export const setVisible = (visible: boolean): ThunkAction<void, RootState, unknown, Action> => {
    return (dispatch: Dispatch) => {
        try {
            dispatch({
                type: SetVisible,
                visible: visible
            })
        } catch (e) {
        }
    }
}