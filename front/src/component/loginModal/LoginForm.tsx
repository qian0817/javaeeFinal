import {Button, Form, Input} from "antd";
import {LockOutlined, UserOutlined} from "@ant-design/icons";
import React from "react";
import instance from "../../axiosInstance";
import {UserVo} from "../../entity/UserVo";
import {setUser} from "../../reducers/login/actionCreate";
import {useDispatch} from "react-redux";
import {setVisible} from "../../reducers/loginFormVisible/actionCreate";

const LoginForm = () => {
    const dispatch = useDispatch()

    const onFinish = async (value: any) => {
        const username = value.username;
        const password = value.password;
        const token = await instance.post<string>('/api/token/', {username, password})
        localStorage.setItem("jwt_token", token.data)

        const user = await instance.get<UserVo>('/api/token/')
        dispatch(setUser(user.data))
        dispatch(setVisible(false))
    }
    return (
        <Form
            onFinish={onFinish}>
            <Form.Item
                name="username"
                rules={[{required: true, message: '请填写用户名'}]}>
                <Input prefix={<UserOutlined className="site-form-item-icon"/>}
                       autoComplete={"username"}
                       placeholder={"用户名"}/>
            </Form.Item>
            <Form.Item
                name="password"
                rules={[{required: true, message: '请填写密码'}]}>
                <Input prefix={<LockOutlined className="site-form-item-icon"/>}
                       placeholder={"密码"}
                       autoComplete={"current-password"}
                       type="password"/>
            </Form.Item>
            <Form.Item>
                <Button type="primary" htmlType="submit">登录</Button>
            </Form.Item>
        </Form>
    )
}

export default LoginForm