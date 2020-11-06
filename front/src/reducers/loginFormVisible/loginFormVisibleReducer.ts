import {Action} from "redux";
import {SetVisible} from "./actionCreate";

interface VisibleAction extends Action {
    visible: boolean
}

const visibleReducer = (state: boolean = false, action: VisibleAction) => {
    console.log(action)
    switch (action.type) {
        case SetVisible:
            state = action.visible
            return state;
        default:
            return state
    }
}

export default visibleReducer