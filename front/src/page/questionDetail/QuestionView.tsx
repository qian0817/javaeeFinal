import React, {Fragment} from "react";
import {Question} from "../../entity/Question";
import {Tag} from "antd";
import {ContentWrapper} from "./style";

interface QuestionProps {
    question: Question
}

const QuestionView: React.FC<QuestionProps> = ({question}) => {
    const tags = question.tags.split(',')
    return (
        <Fragment>
            {tags.map(item => <Tag color="blue" key={item}>{item}</Tag>)}
            <h1 style={{marginTop:20}}>{question.title}</h1>
            <ContentWrapper dangerouslySetInnerHTML={{__html: question.content}}/>
            {/*<PageHeader title=>*/}
            {/*</PageHeader>*/}
        </Fragment>
    )
}

export default QuestionView;