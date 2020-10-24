import React from "react";
import {Button, Form, Input, message} from "antd";
import {register} from "../../service/userService";

interface Interface {
    setLoginStatus: (status: boolean) => void
}

const Register: React.FC<Interface> = ({setLoginStatus}) => {

    const onFinish = async (values: any) => {
        const username = values.username;
        const password = values.password
        const response = await register(username, password)
        if (response.id !== 0) {
            setLoginStatus(false)
            message.warn(response.message)
        } else {
            setLoginStatus(true);
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

export default Register;