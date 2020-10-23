import axios, {AxiosRequestConfig} from 'axios'

const instance = axios.create({})

instance.interceptors.request.use(
    (request: AxiosRequestConfig) => {
        //拦截响应，做统一处理
        const token = localStorage.getItem("JWT-TOKEN")
        if (token != null) {
            request.headers.Authorization = `Bearer ${token}`
        }
        return request;
    },)
export default instance;