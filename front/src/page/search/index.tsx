import React, {useEffect, useState} from "react";
import {useHistory, useParams} from "react-router";
import instance from "../../axiosInstance";
import {Page} from "../../entity/Page";
import {Question} from "../../entity/Question";
import {message} from "antd";
// @ts-ignore
import Highlighter from "react-highlight-words";
import {SearchWrapper, TitleWrapper} from "./style";


const Search = () => {
    const history = useHistory();
    const {keyword} = useParams();
    const [questions, setQuestions] = useState<Page<Question>>()
    const loadQuestion = async (keyword: string) => {
        try {
            const response = await instance.get<Page<Question>>(`/api/question/keyword/${keyword}`)
            setQuestions(response.data)
        } catch (e) {
            message.warn(e);
        }
    }

    useEffect(() => {
        loadQuestion(keyword)
    }, [keyword])

    if (questions == null) {
        return <div>加载中</div>
    }
    return (
        <SearchWrapper>
            {questions.content.map(item =>
                <TitleWrapper onClick={() => history.push(`/question/${item.id}`)}>
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
                </TitleWrapper>
            )}
        </SearchWrapper>
    )
}

export default Search;
