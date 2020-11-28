import axios, {AxiosError} from 'axios'
import {ErrorResponse} from "./entity/ErrorResponse";
import {message} from "antd";
import store from "./store";
import {SetLoginStatus} from "./reducers/login/loginReducer";

const instance = axios.create({})

instance.interceptors.request.use(config => {
    const jwt = localStorage.getItem("jwt_token");
    if (jwt !== null) {
        config.headers.Authorization = `Bearer ${jwt}`;
    }
    return config
})

instance.interceptors.response.use(config => config, error => {
        const ex: AxiosError<ErrorResponse> = error;
        if (ex.response != null) {
            if (ex.response.status === 401) {
                store.dispatch({
                    type: SetLoginStatus,
                    data: null
                })
                message.warn(ex.response.data.message)
            } else if (ex.response.status >= 500) {
                message.warn("服务器内部错误")
            } else {
                message.warn(ex.response.data.message)
            }
        }

        return Promise.reject(error);
    }
)
;

export default instance;