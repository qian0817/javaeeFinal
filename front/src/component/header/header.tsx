import React, {Fragment, useEffect} from "react";
import {LogoWrapper, TopContentWrapper, TopWrapper} from "./style";
import {Button} from "antd";
import {Link} from "react-router-dom";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../store";
import {checkLogin, logout} from "../../reducers/login/actionCreate";

const Header = () => {
    const dispatch = useDispatch()
    const loginStatus = useSelector((state: RootState) => state.login)

    useEffect(() => {
        dispatch(checkLogin())
    }, [dispatch])

    const login = () => {
        const loginWindow = window.open('/login.html', '登录', 'height=600, width=400, top=50%, left=50%, toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no"')
        let timer = setInterval(function () {
            // 检查登录窗口是否已经关闭
            if (loginWindow == null || loginWindow.closed) {
                clearInterval(timer);
                dispatch(checkLogin())
            }
        }, 100);
    }

    return (
        <Fragment>
            <TopWrapper>
                <LogoWrapper>
                    <Link to="/">WEMALL</Link>
                </LogoWrapper>
                {loginStatus ? (
                    <TopContentWrapper>
                        <Button type="link" onClick={() => dispatch(logout())}>登出</Button>
                        <Link to="/cart"><Button type="link">购物车</Button></Link>
                        <Link to="/order"><Button type="link">我的订单</Button></Link>
                    </TopContentWrapper>
                ) : (
                    <TopContentWrapper>
                        <Button type="link" onClick={login}>登录</Button>
                    </TopContentWrapper>
                )}
            </TopWrapper>
        </Fragment>
    );
}

export default Header;