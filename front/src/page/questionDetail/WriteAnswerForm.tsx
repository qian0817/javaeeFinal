import React from "react";
import {Button, Form} from "antd";
import {Answer} from "../../entity/Answer";
import {useHistory} from "react-router";
import instance from "../../axiosInstance";
import Editor from "../../component/editor/Editor";

interface WriteAnswerFormProps {
    hidden: boolean,
    questionId: string
}

const WriteAnswerForm: React.FC<WriteAnswerFormProps> = ({hidden, questionId}) => {
    const history = useHistory();
    const submitAnswer = async (values: any) => {
        const content = values.content.toHTML();
        const response = await instance.post<Answer>('/api/answer/', {content, questionId})
        const answerId = response.data.id
        history.push(`/question/${questionId}/answer/${answerId}`)
    }
    return (
        <Form hidden={hidden} onFinish={submitAnswer}>
            <Form.Item name="content"
                       rules={[{required: true, message: '请填写回答内容'}]}>
                <Editor/>
            </Form.Item>
            <Form.Item>
                <Button type="primary" htmlType="submit">发表回答</Button>
            </Form.Item>
        </Form>
    )
}

export default WriteAnswerForm;