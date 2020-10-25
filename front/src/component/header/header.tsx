import React, {Fragment, useEffect} from "react";
import {LogoWrapper, TopContentWrapper, TopWrapper} from "./style";
import {Button} from "antd";
import {Link, useHistory} from "react-router-dom";
import axios from 'axios'
import {User} from "../../entity/User";

interface Interface {
    loginStatus: boolean,
    setLoginStatus: (status: boolean) => void
}

const Header: React.FC<Interface> = ({loginStatus, setLoginStatus}) => {
    const history = useHistory();
    useEffect(() => {
        const checkLoginStatus = async () => {
            try {
                await axios.get<User>("/api/token/")
                setLoginStatus(true)
            } catch (e) {
                setLoginStatus(false)
            }
        }

        checkLoginStatus()
    }, [setLoginStatus])

    return (
        <Fragment>
            <TopWrapper>
                <LogoWrapper>
                    <Link to="/">WEMALL</Link>
                </LogoWrapper>
                {loginStatus ? (
                    <TopContentWrapper>
                        {/*<Button type="link" onClick={() => dispatchlogout())}>登出</Button>*/}
                        <Button type="primary" onClick={() => history.push("/question/action/create")}>提问</Button>
                    </TopContentWrapper>
                ) : (
                    <TopContentWrapper>
                        <Button type="link" onClick={() => history.push("/login")}>登录</Button>
                        <Button type="link" onClick={() => history.push("/register")}>注册</Button>
                    </TopContentWrapper>
                )}
            </TopWrapper>
        </Fragment>
    );
}

export default Header;