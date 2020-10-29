import React from "react";
import {Button, Form, Input, message} from "antd";
import {useHistory} from "react-router";
import {Store} from "rc-field-form/lib/interface";
import BraftEditor from "braft-editor";
import {AxiosError} from 'axios';
import {Question} from "../../entity/Question";
import {ErrorResponse} from "../../entity/ErrorResponse";
import instance from "../../axiosInstance";

const CreateQuestion = () => {
    const [form] = Form.useForm();
    const history = useHistory()
    const onFinish = async (value: Store) => {
        const title = value.title
        const content = value.content.toHTML()
        const tags = value.tags
        try {
            const response = await instance.post<Question>('/api/question/', {title, content, tags})
            history.push(`/question/${response.data.id}`)
        } catch (e) {
            const ex: AxiosError<ErrorResponse> = e
            message.warn(ex.response?.data.message)
        }
    }

    return (
        <div style={{marginTop:10}}>
            <Form form={form}
                  labelCol={{span: 2}}
                  wrapperCol={{span: 16}}
                  onFinish={onFinish}>
                <Form.Item
                    name="title"
                    label="问题标题"
                    rules={[{required: true, message: '请填写回答标题'}]}>
                    <Input/>
                </Form.Item>
                <Form.Item
                    name="tags"
                    label="问题标签"
                    rules={[{message: '请填写回答内容'}]}>
                    <Input/>
                </Form.Item>
                <Form.Item
                    name="content"
                    label="问题内容"
                    rules={[{required: true, message: '请填写问题内容'}]}>
                    <BraftEditor/>
                </Form.Item>
                <Form.Item wrapperCol={{offset: 2, span: 16}}>
                    <Button type="primary" htmlType="submit">提交</Button>
                </Form.Item>
            </Form>
        </div>
    )
}

export default CreateQuestion;