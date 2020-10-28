import React, {Fragment, useCallback, useEffect} from "react";
import {LogoWrapper, TopContentWrapper, TopWrapper} from "./style";
import {Button} from "antd";
import {Link, useHistory} from "react-router-dom";
import instance from "../../axiosInstance";
import {useCookies} from "react-cookie";
import {useDispatch, useSelector} from "react-redux";
import {setUser} from "../../reducers/login/actionCreate";
import {UserVo} from "../../entity/UserVo";
import {RootState} from "../../store";


const Header = () => {
    const history = useHistory();
    const dispatch = useDispatch()
    const loginStatus = useSelector((state: RootState) => state.login)
    const [, setCookie, removeCookie] = useCookies(['token']);

    const checkLoginStatus = useCallback(async () => {
        try {
            const response = await instance.get<UserVo>("/api/token/")
            const token = localStorage.getItem("_authing_token");
            setCookie("token", token, {path: '/', sameSite: "strict"})
            dispatch(setUser(response.data))
        } catch (e) {
            dispatch(setUser(null))
        }
    }, [dispatch, setCookie])

    useEffect(() => {
        checkLoginStatus()
    }, [checkLoginStatus, dispatch, setCookie])

    //打开登录界面窗口
    const showLoginWindow = () => {
        const loginWindow = window.open('/login.html'
            , '登录'
            , 'height=600, width=400, top=50%, left=50%, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no"')
        let timer = setInterval(function () {
            // 检查登录窗口是否已经关闭
            if (loginWindow == null || loginWindow.closed) {
                clearInterval(timer);
                checkLoginStatus();
            }
        }, 100);
    }

    const logout = () => {
        dispatch(setUser(null))
        //清除 cookie
        removeCookie("token")
    }

    return (
        <Fragment>
            <TopWrapper>
                <LogoWrapper>
                    <Link to="/">知否</Link>
                </LogoWrapper>
                {loginStatus ? (
                    <TopContentWrapper>
                        <Button type="link" onClick={logout}>登出</Button>
                        <Button type="primary" onClick={() => history.push("/question/action/create")}>提问</Button>
                    </TopContentWrapper>
                ) : (
                    <TopContentWrapper>
                        <Button type="link" onClick={showLoginWindow}>登录</Button>
                    </TopContentWrapper>
                )}
            </TopWrapper>
        </Fragment>
    );
}

export default Header;