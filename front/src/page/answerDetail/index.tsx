import React, {useEffect, useState} from "react";
import {useHistory, useParams} from "react-router";
import {AnswerVo} from "../../entity/AnswerVo";
import {Button, Divider, Skeleton} from "antd";
import {Wrapper} from "./style";
import AnswerFooter from "./answerFooter";
import instance from "../../axiosInstance";

const AnswerDetail = () => {
    const {questionId, answerId} = useParams();
    const history = useHistory();
    const [answer, setAnswer] = useState<AnswerVo>()

    useEffect(() => {
        const loadAnswer = async () => {
            const response = await instance.get<AnswerVo>(`/api/answer/id/${answerId}`)
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
            <Wrapper>
                <Skeleton active/>
                <Skeleton active/>
                <Skeleton active/>
                <Skeleton active/>
            </Wrapper>
        )
    }
    console.log(answer)

    const setAgreeStatus = (number: number, status: boolean) => {
        setAnswer({...answer, agreeNumber: number, canAgree: status});
    }
    // history.push(`/question/${answer.question.id}`
    return (
        <Wrapper>
            <Button
                type="link"
                style={{fontSize: 30, fontWeight: "bold"}}
                block
                onClick={() => history.push(`/question/${answer.question.id}`)}>
                {answer.question.title}
            </Button>
            <Divider/>
            <h2>{answer.user.username}</h2>
            <div dangerouslySetInnerHTML={{__html: answer.content}}/>
            {/*评论部分*/}
            <AnswerFooter answerId={answerId}
                          agreeNumber={answer.agreeNumber}
                          canAgree={answer.canAgree}
                          setAgreeStatus={setAgreeStatus}/>
        </Wrapper>
    )
}

export default AnswerDetail;