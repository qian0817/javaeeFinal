import React, {useEffect, useState} from "react";
import {useHistory, useParams} from "react-router";
import axios from 'axios';
import {AnswerVo} from "../../entity/AnswerVo";
import {Button} from "antd";
import {Wrapper} from "./style";

const AnswerDetail = () => {
    const {questionId, answerId} = useParams();
    const history = useHistory();
    const [answer, setAnswer] = useState<AnswerVo>()

    useEffect(() => {
        const loadAnswer = async () => {
            const response = await axios.get<AnswerVo>(`/api/answer/id/${answerId}`)
            if (response.data.questionId !== Number(questionId)) {
                history.push(`/question/${questionId}`)
            } else {
                setAnswer(response.data)
            }
        }
        loadAnswer();
    }, [questionId, answerId, history])
    if (answer == null) {
        return (
            <Wrapper>加载中</Wrapper>
        )
    }
    return (
        <Wrapper>
            <Button type="link" onClick={() => history.push(`/question/${questionId}`)}>查看其他回答</Button>
            <h2>{answer.user.username}</h2>
            <div dangerouslySetInnerHTML={{__html: answer.content}}/>
        </Wrapper>
    )
}

export default AnswerDetail;