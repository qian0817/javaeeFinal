import React, {Fragment, useEffect} from "react";
import {LogoWrapper, TopContentWrapper, TopWrapper} from "./style";
import {Button} from "antd";
import {Link, useHistory} from "react-router-dom";
import {UserDetailVo} from "../../entity/UserDetailVo";
import instance from "../../axiosInstance";
import {useCookies} from "react-cookie";

interface Interface {
    loginStatus: boolean,
    setLoginStatus: (status: boolean) => void
}

const Header: React.FC<Interface> = ({loginStatus, setLoginStatus}) => {
    const history = useHistory();
    const [cookies, setCookie, removeCookie] = useCookies(['token']);

    const checkLoginStatus = async () => {
        try {
            await instance.get<UserDetailVo>("/api/token/")
            setLoginStatus(true)
        } catch (e) {
            setLoginStatus(false)
        }
    }

    useEffect(() => {
        const checkLoginStatus = async () => {
            try {
                await instance.get<UserDetailVo>("/api/token/")
                // 设置 cookie
                const token = localStorage.getItem("_authing_token");
                setCookie("token", token, {path: '/', sameSite: "strict"})
                setLoginStatus(true)
            } catch (e) {
                setLoginStatus(false)
            }
        }
        checkLoginStatus()
    }, [setLoginStatus])

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
        setLoginStatus(false)
        //清除 cookie
        removeCookie("token")
    }

    return (
        <Fragment>
            <TopWrapper>
                <LogoWrapper>
                    <Link to="/">WEMALL</Link>
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