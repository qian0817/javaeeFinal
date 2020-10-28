import React, {useEffect, useState} from "react";
import {useParams} from "react-router";
import {AxiosError} from 'axios';
import {QuestionDetailVo} from "../../entity/QuestionDetailVo";
import {ErrorResponse} from "../../entity/ErrorResponse";
import {Button, message, Skeleton} from "antd";
import AnswerView from "./AnswerView";
import {TotalAnswerWrapper, Wrapper} from "./style";
import WriteAnswerForm from "./WriteAnswerForm";
import instance from "../../axiosInstance";
import QuestionView from "./QuestionView";

const QuestionDetail = () => {
    const {id} = useParams();
    const [formHidden, setFormHidden] = useState(true)
    const [questionDetail, setQuestionDetail] = useState<QuestionDetailVo>()
    useEffect(() => {
        const loadQuesion = async () => {
            try {
                const response = await instance.get<QuestionDetailVo>(`/api/question/id/${id}`)
                setQuestionDetail(response.data)
            } catch (e) {
                const ex: AxiosError<ErrorResponse> = e
                message.warn(ex.response?.data.message)
            }
        }

        loadQuesion()
    }, [id]);
    if (questionDetail == null) {
        return (
            <Wrapper>
                <Skeleton active/>
                <Skeleton active/>
                <Skeleton active/>
                <Skeleton active/>
            </Wrapper>
        )
    }
    return (
        <Wrapper>
            <QuestionView question={questionDetail.question}/>
            <TotalAnswerWrapper>
                共{questionDetail.answers.totalElements}个回答
            </TotalAnswerWrapper>
            <Button style={{marginBottom: 20}} type="primary"
                    onClick={() => setFormHidden(false)}>写回答</Button>
            <WriteAnswerForm
                hidden={formHidden}
                setHidden={setFormHidden}
                questionId={id}
            />
            {questionDetail.answers.content.map(item => <AnswerView answer={item} key={item.id}/>)}
        </Wrapper>
    )
}

export default QuestionDetail;
