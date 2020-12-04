import React from "react";
import {Modal, Tabs} from "antd";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../store";
import LoginForm from "./LoginForm";
import RegisterForm from "./RegisterForm";
import {setVisible} from "../../reducers/loginFormVisible/actionCreate";

const {TabPane} = Tabs;

const LoginModal = () => {

    const dispatch = useDispatch()
    const visible = useSelector((state: RootState) => state.loginFormVisible)

    return (
        <Modal visible={visible} footer={null} onCancel={() => dispatch(setVisible(false))}>
            <Tabs defaultActiveKey="1" centered>
                <TabPane tab="登录" key="1">
                    <LoginForm/>
                </TabPane>
                <TabPane tab="注册" key="2">
                    <RegisterForm/>
                </TabPane>
            </Tabs>
        </Modal>
    )
}

export default LoginModal