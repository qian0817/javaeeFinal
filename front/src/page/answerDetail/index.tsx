import React, {useEffect, useState} from "react";
import {useHistory, useParams} from "react-router";
import {AnswerVo} from "../../entity/AnswerVo";
import {Affix, Button, Divider, Skeleton} from "antd";
import {Wrapper} from "./style";
import instance from "../../axiosInstance";
import CommentView from "./CommentView";
import AgreeButton from "../../component/AgreeButton";

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

    const setAgreeStatus = (number: number, status: boolean) => {
        setAnswer({...answer, agreeNumber: number, canAgree: status});
    }

    return (
        <Wrapper>
            {/*<TitleWrapper onClick={() => history.push(`/question/${answer.question.id}`)}>*/}
            {/*    {answer.question.title}*/}
            {/*</TitleWrapper>*/}
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
            <Affix offsetBottom={0}>
                <div style={{backgroundColor: "white", padding: 10}}>
                    <AgreeButton canAgree={answer.canAgree}
                                 agreeNumber={answer.agreeNumber}
                                 answerId={answerId}
                                 setAgreeStatus={setAgreeStatus}/>
                </div>
            </Affix>
            <Divider/>
            <CommentView answerId={answerId}/>
        </Wrapper>
    )
}

export default AnswerDetail;