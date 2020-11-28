import React, {useEffect, useState} from "react";
import {useHistory, useParams} from "react-router";
import {AnswerVo} from "../../entity/AnswerVo";
import {Affix, Divider, Skeleton} from "antd";
import {TitleWrapper} from "./style";
import instance from "../../axiosInstance";
import CommentView from "./CommentView";
import AgreeButton from "../../component/AgreeButton";
import {Helmet} from "react-helmet";
import {Link} from "react-router-dom";

const AnswerDetail = () => {
    const {questionId, answerId} = useParams<{ questionId: string, answerId: string }>();
    const history = useHistory();
    const [answer, setAnswer] = useState<AnswerVo>()

    useEffect(() => {
        const loadAnswer = async () => {
            const response = await instance.get<AnswerVo>(`/api/answer/id/${answerId}`)
            if (response.data.questionId !== questionId) {
                history.push(`/question/${questionId}`)
            } else {
                setAnswer(response.data)
            }
        }
        loadAnswer();
    }, [questionId, answerId, history])

    const setAgreeStatus = (number: number, status: boolean) => {
        if (answer == null) {
            return
        }
        setAnswer({...answer, agreeNumber: number, canAgree: status});
    }

    return (
        <>{
            answer ? <>
                <Helmet title={`${answer.question.title}-${answer.user.username}的回答`}/>
                <TitleWrapper onClick={() => history.push(`/question/${answer.question.id}`)}>
                    {answer.question.title}
                </TitleWrapper>
                <Divider/>
                <h2><Link to={`/user/${answer.user.id}`}>{answer.user.username}</Link></h2>
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
            </> : <>
                <Skeleton active/>
                <Skeleton active/>
                <Skeleton active/>
                <Skeleton active/>
            </>
        }</>
    )
}

export default AnswerDetail;