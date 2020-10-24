import axios from 'axios'
import {BaseResponse} from "../entity/BaseResponse";
import {User} from "../entity/User";

export const register = async (username: string, password: string) => {
    const response = await axios.post<BaseResponse<User>>("/api/user/", {username, password})
    return response.data
}