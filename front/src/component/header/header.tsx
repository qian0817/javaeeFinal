import React, {useCallback, useEffect} from "react";
import {LogoWrapper, TopContentWrapper, TopWrapper} from "./style";
import {Affix, Button, Input} from "antd";
import {Link, useHistory} from "react-router-dom";
import instance from "../../axiosInstance";
import {useDispatch, useSelector} from "react-redux";
import {setUser} from "../../reducers/login/actionCreate";
import {UserVo} from "../../entity/UserVo";
import {RootState} from "../../store";
import {setVisible} from "../../reducers/loginFormVisible/actionCreate";


const Header = () => {
    const history = useHistory();
    const dispatch = useDispatch()
    const loginUser = useSelector((state: RootState) => state.login)

    const checkLoginStatus = useCallback(async () => {
        if (localStorage.getItem("jwt_token") == null) {
            return
        }
        const response = await instance.get<UserVo | string>("/api/token/")
        if (typeof response.data == "string") {
            dispatch(setUser(null))
        } else {
            dispatch(setUser(response.data))
        }
    }, [dispatch])

    useEffect(() => {
        checkLoginStatus()
    }, [checkLoginStatus, dispatch])

    const logout = async () => {
        localStorage.removeItem("jwt_token")
        dispatch(setUser(null))
    }

    const onSearch = (value: string) => {
        if (value.length === 0) {
            return
        }
        history.push(`/question/search/${value}`)
    }

    return (
        <Affix offsetTop={0}>
            <TopWrapper>
                <LogoWrapper>
                    <Link to="/">知否</Link>
                </LogoWrapper>
                <TopContentWrapper>
                    <Input.Search
                        enterButton
                        style={{width: 300, marginRight: 200}}
                        placeholder="搜索"
                        onSearch={onSearch}
                    />
                    {loginUser ? (
                        <>
                            <Button type="primary" onClick={() => history.push("/question/action/create")}>提问</Button>
                            <Button type="link" onClick={() => history.push(`/user/${loginUser?.id}`)}>我的主页</Button>
                            <Button type="link" onClick={logout}>登出</Button>
                        </>
                    ) : (
                        <Button type="link" onClick={() => dispatch(setVisible(true))}>登录</Button>
                    )}
                </TopContentWrapper>
            </TopWrapper>
        </Affix>
    );
}

export default Header;