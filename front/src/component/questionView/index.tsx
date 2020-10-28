import React from "react";
import {Question} from "../../entity/Question";
import {PageHeader, Tag} from "antd";

interface QuestionProps {
    question: Question,
    onBack?: (e: React.MouseEvent<HTMLDivElement>) => void
}

const QuestionView: React.FC<QuestionProps> = ({question, onBack}) => {
    return (
        <PageHeader title={question.title} onBack={onBack}>
            <Tag color="blue">{question.tags}</Tag>
            <div dangerouslySetInnerHTML={{__html: question.content}}/>
        </PageHeader>
    )
}

export default QuestionView;