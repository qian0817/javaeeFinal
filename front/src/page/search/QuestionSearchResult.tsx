import {useHistory, useParams} from "react-router";
import React, {useEffect, useState} from "react";
import {Question} from "../../entity/Question";
import instance from "../../axiosInstance";
import {Page} from "../../entity/Page";
import {Button, Divider, Skeleton} from "antd";
import {TitleWrapper} from "./style";
import Highlighter from "react-highlight-words";

const QuestionSearchResult = () => {
    const history = useHistory();
    const {keyword} = useParams<{ keyword: string }>();
    const [questions, setQuestions] = useState<Question[]>([])
    const [isLast, setIsLast] = useState(false);
    const [current, setCurrent] = useState(0)

    const loadQuestion = async (keyword: string, current: number) => {
        const response = await instance.get<Page<Question>>(`/api/question/keyword/${keyword}`, {
            params: {pageNum: current}
        })
        setIsLast(response.data.last)
        setQuestions(questions => questions.concat(response.data.content))
    }
    useEffect(() => {
        setQuestions([])
        setCurrent(0)
    }, [keyword])

    useEffect(() => {
        loadQuestion(keyword, current)
    }, [keyword, current])

    const loadMore = async () => {
        setCurrent(current => current + 1)
    }

    if (questions == null) {
        return <>
            <Skeleton active/>
            <Skeleton active/>
            <Skeleton active/>
        </>
    }

    return (
        <>
            {questions.map(item =>
                <TitleWrapper onClick={() => history.push(`/question/${item.id}`)} key={item.id}>
                    <Highlighter
                        highlightStyle={{
                            margin: 0,
                            padding: 0,
                            color: "red",
                            backgroundColor: "white"
                        }}
                        searchWords={[keyword]}
                        autoEscape={true}
                        textToHighlight={item.title}
                    />
                    <Divider type="horizontal"/>
                </TitleWrapper>
            )}
            {!isLast && <Button block style={{marginBottom: 30}} onClick={loadMore} type={"link"}>加载更多</Button>}
        </>
    )
}

export default QuestionSearchResult;