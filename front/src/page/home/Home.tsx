import React, {useEffect, useState} from "react";
import {LoadMoreButton, TitleWrapper, Wrapper} from "./style";
import {Question} from "../../entity/Question";
import {useHistory} from "react-router";
import instance from "../../axiosInstance";
import {Divider} from "antd";

const Home = () => {
    const history = useHistory();
    const [questions, setQuesions] = useState<Array<Question>>([])

    const loadQuestion = async () => {
        const response = await instance.get<Array<Question>>('/api/question/random',{params:{num:20}})
        setQuesions(questions.concat(response.data))
    }

    useEffect(() => {
        loadQuestion()
    }, [])

    return (
        <Wrapper>
            {
                questions.map((item,index) => {
                    return (<div key={index}>
                        <TitleWrapper onClick={() => history.push(`/question/${item.id}`)}>
                            {item.title}
                        </TitleWrapper>
                        <div dangerouslySetInnerHTML={{__html: item.content}}/>
                        <Divider/>
                    </div>)
                })
            }
            <LoadMoreButton type="link" block onClick={loadQuestion}>点击加载更多</LoadMoreButton>
        </Wrapper>
    )
}

export default Home;