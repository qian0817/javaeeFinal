import React from "react";
import {Question} from "../../entity/Question";
import {PageHeader, Tag} from "antd";
import {ContentWrapper} from "./style";

interface QuestionProps {
    question: Question
}

const QuestionView: React.FC<QuestionProps> = ({question}) => {
    return (
        <PageHeader title={question.title}>
            <Tag color="blue">{question.tags}</Tag>
            <ContentWrapper dangerouslySetInnerHTML={{__html: question.content}}/>
        </PageHeader>
    )
}

export default QuestionView;