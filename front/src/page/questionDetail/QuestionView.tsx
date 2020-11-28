import React from "react";
import {Question} from "../../entity/Question";
import {Tag} from "antd";
import {ContentWrapper} from "./style";

interface QuestionProps {
    question: Question
}

const QuestionView: React.FC<QuestionProps> = ({question}) => {
    const tags = question.tags?.split(',')
    return (
        <>
            {tags?.map(item => <Tag color="blue" key={item}>{item}</Tag>)}
            <h1 style={{marginTop: 20}}>{question.title}</h1>
            <ContentWrapper dangerouslySetInnerHTML={{__html: question.content}}/>
        </>
    )
}

export default QuestionView;