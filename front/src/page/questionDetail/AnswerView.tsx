import React from "react";
import {AnswerVo} from "../../entity/AnswerVo";
import {Button, Card} from "antd";
import {useHistory} from "react-router";
import {getTimeRange} from "../../utils/DateUtils";

interface AnswerViewProps {
    answer: AnswerVo
}

const AnswerView: React.FC<AnswerViewProps> = ({answer}) => {
    const history = useHistory();

    const formatUpdateTime = (updateTime: string, createTime: string): string => {
        // 判断是否更新过
        if (updateTime === createTime) {
            return "发布于" + getTimeRange(createTime);
        } else {
            return "编辑于" + getTimeRange(updateTime)
        }
    }

    const time = formatUpdateTime(answer.updateTime, answer.createTime);

    const getContent = (content: string): [string, boolean] => {
        if (content.length < 40) {
            return [content, false]
        } else {
            return [content.substr(0, 40), true];
        }
    }

    const [content, hasMore] = getContent(answer.content);
    return (
        <div style={{marginBottom:30}}>
            <Card hoverable
                  title={<h2>{answer.user.username}</h2>}
                  extra={<div>{time}</div>}
                  onClick={() => history.push(`/question/${answer.questionId}/answer/${answer.id}`)}>
                <div dangerouslySetInnerHTML={{__html: content}}/>
                <Button size="large" type="link" hidden={!hasMore}>查看更多</Button>
            </Card>
        </div>
    )
}

export default AnswerView;