import React, {useCallback, useEffect} from "react";
import {LogoWrapper, TopContentWrapper, TopWrapper} from "./style";
import {Affix, Button} from "antd";
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
    const loginUser = useSelector((state: RootState) => state.login)
    const [, setCookie, removeCookie] = useCookies(['token']);

    const checkLoginStatus = useCallback(async () => {
        try {
            const response = await instance.get<UserVo>("/api/token/")
            dispatch(setUser(response.data))
        } catch (e) {
            dispatch(setUser(null))
        }
    }, [dispatch])

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
                const token = localStorage.getItem("_authing_token");
                setCookie("token", token, {path: '/', sameSite: "strict"})
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
        <Affix offsetTop={0}>
            <TopWrapper>
                <LogoWrapper>
                    <Link to="/">知否</Link>
                </LogoWrapper>
                <TopContentWrapper>
                    {loginUser ? (
                        <>
                            <Button type="primary" onClick={() => history.push("/question/action/create")}>提问</Button>
                            <Button type="link" onClick={logout}>登出</Button>
                        </>
                    ) : (
                        <Button type="link" onClick={showLoginWindow}>登录</Button>
                    )}
                </TopContentWrapper>
            </TopWrapper>
        </Affix>
    );
}

export default Header;