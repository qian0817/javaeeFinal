import React, {useState} from "react";
import {Button, Col, Form, Input, message, Row} from "antd";
import {LockOutlined, MailOutlined, SafetyCertificateOutlined, UserOutlined} from "@ant-design/icons";
import {useDispatch} from "react-redux";
import instance from "../../axiosInstance";
import {UserVo} from "../../entity/UserVo";
import {setUser} from "../../reducers/login/actionCreate";
import {AxiosError} from "axios";
import {ErrorResponse} from "../../entity/ErrorResponse";
import {setVisible} from "../../reducers/loginFormVisible/actionCreate";

const RegisterForm = () => {
    const dispatch = useDispatch()
    const [form] = Form.useForm();
    const [remain, setRemain] = useState(0);

    const onFinish = async (value: any) => {
        const username = value.username;
        const password = value.password;
        const email = value.email;
        const code = value.code;
        try {
            const token = await instance.post<string>('/api/user/', {username, password, email, code})
            localStorage.setItem("jwt_token", token.data)

            const user = await instance.get<UserVo>('/api/token/')
            dispatch(setUser(user.data))

            dispatch(setVisible(false))
        } catch (e) {
            const ex: AxiosError<ErrorResponse> = e
            message.warn(ex.response?.data.message)
        }
    }

    const sendEmail = async () => {
        const email = form.getFieldValue("email")
        try {
            await instance.post("/api/user/registerCode/", {email})
            setRemain(60);
            message.info("验证码已发送")
            const interval = setInterval(() => {
                setRemain(remain => {
                    if (remain <= 0) {
                        clearInterval(interval)
                        return 0;
                    }
                    return remain - 1
                });
            }, 1000)
        } catch (e) {
            const ex: AxiosError<ErrorResponse> = e;
            message.warn(ex.response?.data.message)
        }
    }

    return (
        <Form form={form} onFinish={onFinish}>
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

            <Form.Item
                name="email"
                rules={[{required: true, message: '请填写邮箱'}]}>
                <Input
                    type="email"
                    placeholder={"邮箱"} prefix={<MailOutlined className="site-form-item-icon"/>}/>
            </Form.Item>

            <Form.Item>
                <Row gutter={12}>
                    <Col span={12}>
                        <Form.Item
                            name="code"
                            noStyle
                            rules={[{required: true, message: '请输入验证码'}]}>
                            <Input prefix={<SafetyCertificateOutlined/>}
                                   placeholder={"验证码"}
                                   type="text"/>
                        </Form.Item>
                    </Col>
                    <Col span={12}>
                        <Button onClick={sendEmail}

                                disabled={remain > 0}>
                            {
                                remain > 0 ? remain + "秒后可重新发送" : "发送验证码"
                            }
                        </Button>
                    </Col>
                </Row>

            </Form.Item>
            <Form.Item>
                <Button type="primary" htmlType="submit">注册</Button>
            </Form.Item>
        </Form>
    )
}

export default RegisterForm;