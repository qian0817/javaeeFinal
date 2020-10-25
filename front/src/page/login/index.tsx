import React from "react";
import {Button, Form, Input, message} from "antd";
import axios, {AxiosError} from 'axios';
import {User} from "../../entity/User";
import {ErrorResponse} from "../../entity/ErrorResponse";
import {useHistory} from "react-router";

interface LoginProps {
    setLoginStatus: (status: boolean) => void
}

const Login: React.FC<LoginProps> = ({setLoginStatus}) => {
    const history = useHistory()
    const onFinish = async (values: any) => {
        const username = values.username
        const password = values.password
        try {
            await axios.post<User>('/api/token/', {username, password});
            setLoginStatus(true)
            history.push("/")
        } catch (e) {
            const ex: AxiosError<ErrorResponse> = e
            message.warn(ex.response?.data.message)

        }
    }
    return (
        <Form
            name="basic"
            style={{margin: "0 30%"}}
            initialValues={{remember: true}}
            onFinish={onFinish}
        >
            <Form.Item
                label="用户名"
                name="username"
                rules={[{required: true, message: '请输入用户名'}]}
            >
                <Input/>
            </Form.Item>

            <Form.Item
                label="密码"
                name="password"
                rules={[{required: true, message: '请输入密码'}]}
            >
                <Input.Password/>
            </Form.Item>

            <Form.Item>
                <Button type="primary" htmlType="submit">
                    注册
                </Button>
            </Form.Item>
        </Form>
    )
}

export default Login;