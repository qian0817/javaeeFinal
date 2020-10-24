import React from "react";
import {Button, Form, Input, message} from "antd";
import {useHistory} from "react-router";
import {Store} from "rc-field-form/lib/interface";
import BraftEditor from "braft-editor";
import axios from 'axios';
import {BaseResponse} from "../../entity/BaseResponse";
import {Question} from "../../entity/Question";

const CreateQuestion = () => {
    const [form] = Form.useForm();
    const history = useHistory()
    const onFinish = async (value: Store) => {
        console.log(value.title)
        const title = value.title
        const content = value.content.toHTML()
        const tags = value.tags
        const response = await axios.post<BaseResponse<Question>>('/api/question/', {title, content, tags})
        if (response.data.id !== 0) {
            message.warn(response.data.message)
        } else {
            history.push(`/question/${response.data.data.id}`)
        }
    }

    return (
        <div>
            <Form form={form} labelCol={{span: 2}} name="control-ref" onFinish={onFinish}>
                <Form.Item name="title" label="问题标题" rules={[{required: true}]}>
                    <Input/>
                </Form.Item>
                <Form.Item name="content" label="问题内容" rules={[{required: true}]}>
                    <BraftEditor/>
                </Form.Item>
                <Form.Item name="tags" label="问题标签" rules={[{required: true}]}>
                    <Input/>
                </Form.Item>
                <Form.Item>
                    <Button type="primary" htmlType="submit">提交</Button>
                </Form.Item>
            </Form>
        </div>
    )
}

export default CreateQuestion;