import React from "react";
import {Button, Form, Input} from "antd";
import {useHistory} from "react-router";
import {Store} from "rc-field-form/lib/interface";
import {Question} from "../../entity/Question";
import instance from "../../axiosInstance";
import {useSelector} from "react-redux";
import {RootState} from "../../store";
import {Helmet} from "react-helmet";
import Editor from "../../component/editor/Editor";

const CreateQuestion = () => {
    const [form] = Form.useForm();
    const history = useHistory();
    const loginUser = useSelector((state: RootState) => state.login)

    if (loginUser == null) {
        history.push('/')
    }

    const onFinish = async (value: Store) => {
        const title = value.title
        const content = value.content.toHTML()
        const tags = value.tags
        const response = await instance.post<Question>('/api/question/', {title, content, tags})
        history.push(`/question/${response.data.id}`)
    }

    return (
        <>
            <Helmet title={`创建问题`}/>
            <Form form={form}
                  labelCol={{span: 2}}
                  wrapperCol={{span: 20}}
                  onFinish={onFinish}>
                <Form.Item
                    name="title"
                    label="问题标题"
                    rules={[{required: true, message: '请填写回答标题'}]}>
                    <Input/>
                </Form.Item>
                <Form.Item
                    name="tags"
                    label="问题标签">
                    <Input placeholder="标签按逗号分隔"/>
                </Form.Item>
                <Form.Item
                    name="content"
                    label="问题内容"
                    rules={[{required: true, message: '请填写问题内容'}]}>
                    <Editor/>
                </Form.Item>
                <Form.Item wrapperCol={{offset: 2, span: 16}}>
                    <Button type="primary" htmlType="submit">提交</Button>
                </Form.Item>
            </Form>
        </>
    )
}

export default CreateQuestion;