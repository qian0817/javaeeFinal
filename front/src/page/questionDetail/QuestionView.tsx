import React from "react";
import {Tag} from "antd";
import {ContentWrapper} from "./style";
import {QuestionVo} from "../../entity/QuestionVo";

interface QuestionProps {
    question: QuestionVo
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