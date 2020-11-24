import axios from 'axios'

const instance = axios.create({})

instance.interceptors.request.use(config => {
    const jwt = localStorage.getItem("jwt_token");
    if (jwt !== null) {
        config.headers.Authorization = `Bearer ${jwt}`;
    }
    return config
})

export default instance;