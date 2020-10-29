import React, {useEffect, useState} from "react";
import {useParams} from "react-router";
import {AxiosError} from 'axios';
import {ErrorResponse} from "../../entity/ErrorResponse";
import {Button, message, Skeleton} from "antd";
import AnswerView from "./AnswerView";
import {TotalAnswerWrapper, Wrapper} from "./style";
import WriteAnswerForm from "./WriteAnswerForm";
import instance from "../../axiosInstance";
import QuestionView from "./QuestionView";
import {Page} from "../../entity/Page";
import {AnswerVo} from "../../entity/AnswerVo";
import {Question} from "../../entity/Question";
import {useSelector} from "react-redux";
import {RootState} from "../../store";
import {Helmet} from "react-helmet";

const QuestionDetail = () => {
    const {id} = useParams();
    const [formHidden, setFormHidden] = useState(true)
    const [questionDetail, setQuestionDetail] = useState<Question>()
    const [answers, setAnswers] = useState<Page<AnswerVo>>()
    const loginUser = useSelector((state: RootState) => state.login)

    const loadQuestion = async (id: number) => {
        try {
            const questionResponse = await instance.get<Question>(`/api/question/id/${id}`)
            setQuestionDetail(questionResponse.data)
            const response = await instance.get<Page<AnswerVo>>(`/api/question/id/${id}/answers/`, {
                params: {
                    pageNum: 0,
                    pageSize: 10
                }
            });
            setAnswers(response.data)
        } catch (e) {
            const ex: AxiosError<ErrorResponse> = e
            message.warn(ex.response?.data.message)
        }
    }

    useEffect(() => {
        loadQuestion(id)
    }, [id]);

    function showWriteAnswerForm() {
        if (loginUser == null) {
            message.warn("请先登录")
            return
        }
        setFormHidden(false);
    }

    return (
        <Wrapper>
            {questionDetail ? (
                <>
                    <Helmet title={questionDetail.title}/>
                    <QuestionView question={questionDetail}/>
                    <Button style={{marginBottom: 20}}
                            type="primary"
                            onClick={showWriteAnswerForm}>写回答</Button>
                    {answers ? <TotalAnswerWrapper>
                        共{answers.totalElements}个回答
                    </TotalAnswerWrapper> : null}
                    <WriteAnswerForm
                        hidden={formHidden}
                        setHidden={setFormHidden}
                        questionId={id}
                    />
                </>
            ) : (
                <>
                    <Skeleton active/>
                </>)}
            {
                answers ? (
                    answers.content.map(item => <AnswerView answer={item} key={item.id}/>)
                ) : <>
                    <Skeleton active/>
                    <Skeleton active/>
                    <Skeleton active/>
                </>}
        </Wrapper>
    )
}

export default QuestionDetail;
