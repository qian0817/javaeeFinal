import React from "react";
import {Button, Form, message} from "antd";
import BraftEditor from "braft-editor";
import axios, {AxiosError} from 'axios';
import {ErrorResponse} from "../../entity/ErrorResponse";
import {Answer} from "../../entity/Answer";
import {useHistory} from "react-router";

interface WriteAnswerFormProps {
    hidden: boolean,
    setHidden: (value: boolean) => void,
    questionId: number
}

const WriteAnswerForm: React.FC<WriteAnswerFormProps> = ({hidden, setHidden, questionId}) => {
    const history = useHistory();
    const submitAnswer = async (values: any) => {
        const content = values.content.toHTML();
        try {
            const response = await axios.post<Answer>('/api/answer/', {content,questionId})
            const answerId = response.data.id
            history.push(`/question/${questionId}/answer/${answerId}`)
        } catch (e) {
            const ex: AxiosError<ErrorResponse> = e
            message.warn(ex.response?.data.message)
        }
    }
    return (
        <Form hidden={hidden} onFinish={submitAnswer}>
            <Form.Item name="content" rules={[{required: true}]}>
                <BraftEditor/>
            </Form.Item>
            <Form.Item>
                <Button type="primary" htmlType="submit">发表回答</Button>
            </Form.Item>
        </Form>
    )
}

export default WriteAnswerForm;