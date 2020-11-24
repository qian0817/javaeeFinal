import React from "react";
import {Button, Form, message} from "antd";
import BraftEditor from "braft-editor";
import {AxiosError} from 'axios';
import {ErrorResponse} from "../../entity/ErrorResponse";
import {Answer} from "../../entity/Answer";
import {useHistory} from "react-router";
import instance from "../../axiosInstance";

interface WriteAnswerFormProps {
    hidden: boolean,
    setHidden: (value: boolean) => void,
    questionId: string
}

const WriteAnswerForm: React.FC<WriteAnswerFormProps> = ({hidden, setHidden, questionId}) => {
    const history = useHistory();
    const submitAnswer = async (values: any) => {
        const content = values.content.toHTML();
        try {
            const response = await instance.post<Answer>('/api/answer/', {content, questionId})
            const answerId = response.data.id
            history.push(`/question/${questionId}/answer/${answerId}`)
        } catch (e) {
            const ex: AxiosError<ErrorResponse> = e
            message.warn(ex.response?.data.message)
        }
    }
    return (
        <Form hidden={hidden} onFinish={submitAnswer}>
            <Form.Item name="content"
                       rules={[{required: true,message: '请填写回答内容'}]}>
                <BraftEditor/>
            </Form.Item>
            <Form.Item>
                <Button type="primary" htmlType="submit">发表回答</Button>
            </Form.Item>
        </Form>
    )
}

export default WriteAnswerForm;