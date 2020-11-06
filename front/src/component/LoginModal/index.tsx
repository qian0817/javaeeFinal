import React, {useState} from "react";
import {Button, Modal} from "antd";
import {useHistory} from "react-router";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../store";
import LoginForm from "./LoginForm";
import RegisterForm from "./RegisterForm";
import {setVisible} from "../../reducers/loginFormVisible/actionCreate";

const LoginModal = () => {

    const dispatch = useDispatch()
    const [loginClick, setLoginClick] = useState(true)
    const history = useHistory();
    const loginUser = useSelector((state: RootState) => state.login)
    const visible = useSelector((state: RootState) => state.loginFormVisible)

    if (loginUser != null) {
        history.push("/")
    }

    return (
        <div>
            <Modal visible={visible} footer={null} onCancel={() => dispatch(setVisible(false))}>
                <div>
                    <Button type={loginClick ? "text" : "link"}
                            style={{marginLeft: "20%"}}
                            onClick={() => setLoginClick(true)}>
                        登录
                    </Button>
                    <Button type={loginClick ? "link" : "text"}
                            style={{marginRight: "20%", float: "right"}}
                            onClick={() => setLoginClick(false)}>
                        注册
                    </Button>
                </div>

                {loginClick && <LoginForm/>}
                {!loginClick && <RegisterForm/>}
            </Modal>
        </div>
    )
}

export default LoginModal