import {TitleWrapper} from "./style";
import {Divider} from "antd";
import React from "react";
import {useHistory} from "react-router";
import {Question} from "../../entity/Question";

interface QuestionCardProps {
    item: Question
}

const QuestionCard: React.FC<QuestionCardProps> = ({item}) => {
    const history = useHistory();
    return (
        <>
            <TitleWrapper onClick={() => history.push(`/question/${item.id}`)}>
                {item.title}
            </TitleWrapper>
            <div dangerouslySetInnerHTML={{__html: item.content.substr(0,100)}}/>
            <Divider/>
        </>
    )
}

export default QuestionCard;