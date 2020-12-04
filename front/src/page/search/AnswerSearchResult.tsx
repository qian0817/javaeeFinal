import React, {useEffect, useState} from "react";
import {useParams} from "react-router";
import {AnswerVo} from "../../entity/AnswerVo";
import instance from "../../axiosInstance";
import {Page} from "../../entity/Page";
import {Button, Skeleton} from "antd";
import AnswerCard from "../../component/answerCard";

const AnswerSearchResult = () => {
    const {keyword} = useParams<{ keyword: string }>();
    const [answers, setAnswers] = useState<AnswerVo[]>([])
    const [isLast, setIsLast] = useState(false);
    const [current, setCurrent] = useState(0)

    const loadAnswer = async (keyword: string, current: number) => {
        const response = await instance.get<Page<AnswerVo>>(`/api/answer/keyword/${keyword}`, {
            params: {pageNum: current}
        })
        setIsLast(response.data.last)
        setAnswers(answers => answers.concat(response.data.content))
    }

    useEffect(() => {
        setAnswers([])
        setCurrent(0)
    }, [keyword])

    useEffect(() => {
        loadAnswer(keyword, current)
    }, [keyword, current])

    const loadMore = async () => {
        setCurrent(current => current + 1)
    }

    if (answers == null) {
        return <>
            <Skeleton active/>
            <Skeleton active/>
            <Skeleton active/>
        </>
    }

    return (
        <>
            {answers.map(item => <AnswerCard showQuestion={true} showUser={true}
                                             hiddenAgree={true} answer={item}
                                             keyword={[keyword]}/>)}
            {!isLast && <Button block style={{marginBottom: 30}} onClick={loadMore} type={"link"}>加载更多</Button>}
        </>

    )

}

export default AnswerSearchResult;