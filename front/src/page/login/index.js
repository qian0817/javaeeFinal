import Guard from '@authing/guard'
import React, {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {useHistory} from "react-router";
import instance from "../../axiosInstance";
import {setUser} from "../../reducers/login/actionCreate";
import {useCookies} from "react-cookie";

const Login = () => {
    const dispatch = useDispatch();
    const history = useHistory();
    const loginUser = useSelector(state => state.login)
    const [, setCookie,] = useCookies([]);

    const userPoolId = "5f8eb6f27371b7d5ab760831";
    const guard = new Guard(userPoolId, {
        hideClose: true,
        logo: "https://usercontents.authing.cn/client/logo@2.png",
        title: "知否",
        // 把表单插入到 id 为 my-form 的标签
        mountId: "my-form",
    });

    useEffect(() => {
        return () => guard.hide();
    }, [guard])
    
    // 判断用户是否已经登陆，如果已经登陆则跳转到首页
    if (loginUser != null) {
        history.push('/')
    }

    const handleLoginSuccess = async (userInfo) => {
        console.log(userInfo)
        setCookie("token", userInfo.token);
        try {
            const response = await instance.get("/api/token/")
            dispatch(setUser(response.data))
        } catch (e) {
            dispatch(setUser(null))
        } finally {
            history.push('/')
        }
    }

    guard.on("authenticated", userInfo => handleLoginSuccess(userInfo))
    guard.on("scanned-success", userInfo => handleLoginSuccess(userInfo));
    return (
        <div className="my-form"/>
    )
}

export default Login