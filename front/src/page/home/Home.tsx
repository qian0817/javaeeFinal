import React, {useEffect, useState} from "react";
import {Wrapper} from "./style";
import axios from 'axios';
import {Question} from "../../entity/Question";
import {useHistory} from "react-router";
import {Button} from "antd";

const Home = () => {
    const history = useHistory();
    const [questions, setQuesions] = useState<Array<Question>>([])
    useEffect(() => {
        const loadQuestion = async () => {
            const response = await axios.get<Array<Question>>('/api/question/random')
            setQuesions(response.data)
        }
        loadQuestion()
    }, [])
    
    return (
        <Wrapper>
            {
                questions.map((item) => {
                    return (<div key={item.id}>
                        <Button type="link"
                                style={{fontSize:30}}
                                onClick={() => history.push(`/question/${item.id}`)}>
                                {item.title}
                        </Button>
                        <div dangerouslySetInnerHTML={{__html: item.content}}/>
                    </div>)
                })
            }
        </Wrapper>
    )
}

export default Home;