import {TitleWrapper} from "../../page/home/style";
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
        <div>
            <TitleWrapper onClick={() => history.push(`/question/${item.id}`)}>
                {item.title}
            </TitleWrapper>
            <div dangerouslySetInnerHTML={{__html: item.content}}/>
            <Divider/>
        </div>
    )
}

export default QuestionCard;