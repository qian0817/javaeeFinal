import React, {Fragment} from "react";
import {AnswerVo} from "../../entity/AnswerVo";
import {Card, Divider} from "antd";
import {useHistory} from "react-router";

export interface AnswerViewProps {
    answer: AnswerVo
}

const AnswerView: React.FC<AnswerViewProps> = ({answer}) => {
    const history = useHistory();
    return (
        <Fragment>
            <Card size="small"
                  hoverable
                  title={<h2>{answer.user.username}</h2>}
                  extra={<div>{answer.updateTime}</div>}
                  onClick={() => history.push(`/question/${answer.questionId}/answer/${answer.id}`)}>
                <div dangerouslySetInnerHTML={{__html: answer.content}}/>
            </Card>
            <Divider/>
        </Fragment>
    )
}

export default AnswerView;