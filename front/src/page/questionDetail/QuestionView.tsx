import React, {Fragment} from "react";
import {Question} from "../../entity/Question";
import {Tag} from "antd";

interface QuestionProps {
    question: Question
}

const QuestionView: React.FC<QuestionProps> = ({question}) => {

    return (
        <Fragment>
            <Tag color="blue">{question.tags}</Tag>
            <h1 style={{fontSize: 40}}>
                {question.title}
            </h1>
            <div dangerouslySetInnerHTML={{__html: question.content}}/>
        </Fragment>
    )
}

export default QuestionView;