import React, {Fragment} from "react";
import {AnswerVo} from "../../entity/AnswerVo";
import {Card, Divider} from "antd";

export interface AnswerViewProps {
    answer: AnswerVo
}

const AnswerView: React.FC<AnswerViewProps> = ({answer}) => {
    return (
        <Fragment>
            <Card size="small" title={<h2>{answer.user.username}</h2>}>
                <div dangerouslySetInnerHTML={{__html: answer.content}}/>
                上次修改时间：{answer.updateTime}
            </Card>
            <Divider/>
        </Fragment>
    )
}

export default AnswerView;