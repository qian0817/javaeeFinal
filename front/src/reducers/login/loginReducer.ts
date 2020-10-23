export const SetLoginStatus = "set_login_status"

const loginReducer = (state = false, action: any) => {
    switch (action.type) {
        case SetLoginStatus:
            state = action.data
            return state
        default:
            return state
    }
}


export default loginReducer