import React, {useEffect, useState} from "react";
import {LoadMoreButton, SwitchHotWrapper, Wrapper} from "./style";
import {Question} from "../../entity/Question";
import instance from "../../axiosInstance";
import {Helmet} from "react-helmet";
import QuestionCard from "./QuestionCard";
import {Button, message, Skeleton} from "antd";
import {QuestionHotVo} from "../../entity/QuestionHotVo";
import HotQuestionCard from "./HotQuestionCard";
import {AxiosError} from "axios";
import {ErrorResponse} from "../../entity/ErrorResponse";

const Home = () => {
    const [questions, setQuestions] = useState<Array<Question>>([])
    const [hotQuestions, setHotQuestions] = useState<Array<QuestionHotVo>>([])
    const [isHotClick, setHotClick] = useState(false);
    const [loading, setLoading] = useState(false);

    const loadQuestion = async () => {
        setLoading(true)
        try {
            const response = await instance.get<Array<Question>>('/api/question/random',
                {params: {num: 20}})
            setQuestions(questions => [...questions, ...response.data])
        } catch (e) {
            const ex: AxiosError<ErrorResponse> = e;
            message.warn(ex.response?.data.message)
        } finally {
            setLoading(false)
        }
    }

    const loadHotQuestion = async () => {
        setLoading(true)
        try {
            const response = await instance.get<Array<QuestionHotVo>>('/api/question/hot/')
            setHotQuestions(response.data)
        } catch (e) {
            const ex: AxiosError<ErrorResponse> = e;
            message.warn(ex.response?.data.message)
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        loadQuestion()
    }, [])

    useEffect(() => {
        if (isHotClick) {
            loadHotQuestion()
        }
    }, [isHotClick])

    return (
        <Wrapper>
            <Helmet title="首页"/>
            <SwitchHotWrapper>
                <Button type={isHotClick ? "link" : "text"}
                        style={{fontSize: 30}}
                        onClick={() => setHotClick(false)}>推荐</Button>
                <Button type={isHotClick ? "text" : "link"}
                        style={{fontSize: 30}}
                        onClick={() => setHotClick(true)}>热榜</Button>
            </SwitchHotWrapper>

            {!isHotClick &&
            <>
                {questions.map((item, index) => <QuestionCard item={item} key={index}/>)}
                {loading &&
                <>
                    <Skeleton active/>
                    <Skeleton active/>
                    <Skeleton active/>
                    <Skeleton active/>
                    <Skeleton active/>
                    <Skeleton active/>
                </>}
                <LoadMoreButton type="link" block onClick={loadQuestion}>点击加载更多</LoadMoreButton>
            </>
            }

            {isHotClick &&
            <>
                {hotQuestions.map((item, index) =>
                    <HotQuestionCard item={item} index={index + 1} key={item.question.id}/>)
                }
                {loading &&
                <>
                    <Skeleton active/>
                    <Skeleton active/>
                    <Skeleton active/>
                    <Skeleton active/>
                    <Skeleton active/>
                    <Skeleton active/>
                </>}
            </>
            }
        </Wrapper>
    )
}

export default Home;