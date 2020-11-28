import React, {useState} from "react";
import {Button, Modal} from "antd";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../store";
import LoginForm from "./LoginForm";
import RegisterForm from "./RegisterForm";
import {setVisible} from "../../reducers/loginFormVisible/actionCreate";

const LoginModal = () => {

    const dispatch = useDispatch()
    const [loginClick, setLoginClick] = useState(true)
    const visible = useSelector((state: RootState) => state.loginFormVisible)

    return (
        <Modal visible={visible} footer={null} onCancel={() => dispatch(setVisible(false))}>
            <div style={{paddingBottom: 20}}>
                <Button type={loginClick ? "text" : "link"}
                        style={{marginLeft: "20%"}}
                        size={"large"}
                        onClick={() => setLoginClick(true)}>
                    登录
                </Button>
                <Button type={loginClick ? "link" : "text"}
                        size={"large"}
                        style={{marginRight: "20%", float: "right"}}
                        onClick={() => setLoginClick(false)}>
                    注册
                </Button>
            </div>

            {loginClick && <LoginForm/>}
            {!loginClick && <RegisterForm/>}
        </Modal>
    )
}

export default LoginModal