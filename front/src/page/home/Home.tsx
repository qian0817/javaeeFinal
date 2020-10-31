import React, {useEffect, useState} from "react";
import {LoadMoreButton, Wrapper} from "./style";
import {Question} from "../../entity/Question";
import instance from "../../axiosInstance";
import {Helmet} from "react-helmet";
import QuestionCard from "../../component/questionCard";

const Home = () => {
    const [questions, setQuesions] = useState<Array<Question>>([])

    const loadQuestion = async () => {
        const response = await instance.get<Array<Question>>('/api/question/random', {params: {num: 20}})
        setQuesions(questions.concat(response.data))
    }

    useEffect(() => {
        loadQuestion()
    }, [])

    return (
        <Wrapper>
            <Helmet title="首页"/>
            {
                questions.map((item, index) => <QuestionCard item={item} key={index}/>)
            }
            <LoadMoreButton type="link" block onClick={loadQuestion}>点击加载更多</LoadMoreButton>
        </Wrapper>
    )
}

export default Home;