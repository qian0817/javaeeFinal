import React, {useState} from "react";
import {AnswerVo} from "../../entity/AnswerVo";
import {getOverView} from "../../utils/StringUtils";
import {Link} from "react-router-dom";
import {OverViewWrapper, TimeWrapper, TitleWrapper, UsernameWrapper} from "./style";
import {Divider} from "antd";
import {getTimeRange} from "../../utils/DateUtils";
import AgreeButton from "../agreeButton";

interface AnswerCardProps {
    answer: AnswerVo,
    showUser?: boolean,
    showTime?: boolean,
    showQuestion?: boolean
}

const AnswerCard: React.FC<AnswerCardProps> = ({answer, showUser, showTime, showQuestion}) => {
    const [overView] = getOverView(answer.content);
    const [agreeNumber, setAgreeNumber] = useState(answer.agreeNumber);
    const [canAgree, setCanAgree] = useState(answer.canAgree);

    const formatTime = (updateTime: string, createTime: string): string => {
        // 判断是否更新过
        if (updateTime === createTime) {
            return "发布于" + getTimeRange(createTime);
        } else {
            return "编辑于" + getTimeRange(updateTime)
        }
    }

    const agreeChange = (agree: number, canAgree: boolean) => {
        setAgreeNumber(agree)
        setCanAgree(canAgree)
    }

    return (

        <div>
            {
                showQuestion && <Link to={`/question/${answer.question.id}`}>
                    <TitleWrapper>{answer.question.title}</TitleWrapper>
                </Link>
            }
            <div>
                {showUser && <UsernameWrapper to={`/user/${answer.user.id}`}>{answer.user.username}</UsernameWrapper>}
                {showTime && <TimeWrapper>{formatTime(answer.createTime, answer.updateTime)}</TimeWrapper>}
            </div>
            <Link to={`/question/${answer.questionId}/answer/${answer.id}`}>
                <OverViewWrapper>{overView}</OverViewWrapper>
            </Link>
            <AgreeButton
                style={{marginTop:30}}
                canAgree={canAgree}
                onChange={agreeChange}
                agreeNumber={agreeNumber}
                answerId={answer.id}/>
            <Divider dashed/>
        </div>
    )
}

export default AnswerCard;