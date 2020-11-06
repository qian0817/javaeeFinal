import React from "react";
import {Button, Form, Input, message} from "antd";
import {LockOutlined, UserOutlined} from "@ant-design/icons";
import {useDispatch} from "react-redux";
import {useHistory} from "react-router";
import instance from "../../axiosInstance";
import {UserVo} from "../../entity/UserVo";
import {setUser} from "../../reducers/login/actionCreate";
import {AxiosError} from "axios";
import {ErrorResponse} from "../../entity/ErrorResponse";
import {setVisible} from "../../reducers/loginFormVisible/actionCreate";

const RegisterForm = () => {
    const dispatch = useDispatch()
    const history = useHistory();
    const onFinish = async (value: any) => {
        const username = value.username;
        const password = value.password;
        try {
            const response = await instance.post<UserVo>('/api/user/', {username, password})
            dispatch(setUser(response.data))
            dispatch(setVisible(false))
            history.push("/")
        } catch (e) {
            const ex: AxiosError<ErrorResponse> = e
            message.warn(ex.response?.data.message)
        }
    }
    return (
        <Form
            onFinish={onFinish}>
            <Form.Item
                name="username"
                rules={[{required: true, message: '请填写用户名'}]}>
                <Input prefix={<UserOutlined className="site-form-item-icon"/>}
                       placeholder={"用户名"}/>
            </Form.Item>
            <Form.Item
                name="password"
                rules={[{required: true, message: '请填写密码'}]}>
                <Input prefix={<LockOutlined className="site-form-item-icon"/>}
                       placeholder={"密码"}
                       type="password"/>
            </Form.Item>
            <Form.Item>
                <Button type="primary" htmlType="submit">注册</Button>
            </Form.Item>
        </Form>
    )
}

export default RegisterForm;