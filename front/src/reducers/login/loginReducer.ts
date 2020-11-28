import {Action} from "redux";
import {UserVo} from "../../entity/UserVo";

export const SetLoginStatus = "set_login_status"

interface LoginAction extends Action {
    data: UserVo | null
}

const loginReducer = (state: UserVo | null = null, action: LoginAction) => {
    switch (action.type) {
        case SetLoginStatus:
            state = action.data
            return state
        default:
            return state
    }
}


export default loginReducer