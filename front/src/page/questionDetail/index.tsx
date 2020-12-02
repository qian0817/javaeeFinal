import React, {useEffect, useState} from "react";
import {useHistory, useParams} from "react-router";
import {Button, Skeleton} from "antd";
import {TotalAnswerWrapper} from "./style";
import WriteAnswerForm from "./WriteAnswerForm";
import instance from "../../axiosInstance";
import QuestionView from "./QuestionView";
import {Page} from "../../entity/Page";
import {AnswerVo} from "../../entity/AnswerVo";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../store";
import {Helmet} from "react-helmet";
import {setVisible} from "../../reducers/loginFormVisible/actionCreate";
import {QuestionVo} from "../../entity/QuestionVo";
import AnswerCard from "../../component/answerCard";

const QuestionDetail = () => {
    const {id} = useParams<{ id: string }>();
    const history = useHistory();
    const [formHidden, setFormHidden] = useState(true)
    const [questionDetail, setQuestionDetail] = useState<QuestionVo>()
    const [answers, setAnswers] = useState<AnswerVo[]>([])
    const [totalAnswer, setTotalAnswer] = useState(0)
    const [current, setCurrent] = useState(0)
    const [last, setLast] = useState(true)

    const dispatch = useDispatch()
    const loginUser = useSelector((state: RootState) => state.login)

    const loadQuestion = async (id: string, current: number) => {
        const questionResponse = await instance.get<QuestionVo>(`/api/question/id/${id}`)
        setQuestionDetail(questionResponse.data)
        const response = await instance.get<Page<AnswerVo>>(`/api/question/id/${id}/answers/`, {
            params: {
                pageNum: current,
                pageSize: 10,
            }
        });
        setLast(response.data.last)
        setTotalAnswer(response.data.totalElements)
        setAnswers(answers => answers.concat(response.data.content))
    }

    useEffect(() => {
        loadQuestion(id, current)
    }, [id, current]);

    const showWriteAnswerForm = () => {
        if (loginUser == null) {
            dispatch(setVisible(true))
            return
        }
        setFormHidden(false);
    }

    function loadMore() {
        setCurrent(current => current + 1)
    }

    return (
        <>
            {questionDetail && <>
                <Helmet title={questionDetail.question.title}/>
                <QuestionView question={questionDetail.question}/>
                {
                    questionDetail.canCreateAnswer ?
                        <Button style={{marginBottom: 20}}
                                type="primary"
                                onClick={showWriteAnswerForm}>写回答</Button> :
                        <Button style={{marginBottom: 20}}
                                type="primary"
                                onClick={() => history.push(`/question/${id}/answer/${questionDetail?.myAnswerId}`)}>查看我的回答</Button>
                }

                {answers && <TotalAnswerWrapper>共{totalAnswer}个回答</TotalAnswerWrapper>}
                <WriteAnswerForm
                    hidden={formHidden || !questionDetail.canCreateAnswer}
                    questionId={id}
                />
            </>
            }
            {!questionDetail && <Skeleton active/>}
            {answers && answers.map(item => <AnswerCard answer={item} showUser={true} showTime={true} key={item.id}/>)}
            {!last && <Button block type="link" onClick={loadMore}>加载更多</Button>}
            {!answers && <>
                <Skeleton active/>
                <Skeleton active/>
                <Skeleton active/>
            </>}
        </>
    )
}

export default QuestionDetail;
