import React from "react";
import {QuestionHotVo} from "../../entity/QuestionHotVo";
import {HotIndexWrapper, HotQuestionWrapper, TitleWrapper} from "./style";
import {Divider} from "antd";
import {useHistory} from "react-router";
import hotsvg from './hot.svg'

interface HotQuestionCardProps {
    item: QuestionHotVo,
    index: number
}

const HotQuestionCard: React.FC<HotQuestionCardProps> = ({item, index}) => {
    const history = useHistory()

    const getIndexColor = (index: number): string => {
        if (index <= 3) {
            return "#ff9607"
        } else {
            return "#999"
        }
    }

    const indexColor = getIndexColor(index);
    return (
        <>
            <HotIndexWrapper>
                <p style={{color: indexColor, fontSize: 15}}>{index}</p>
            </HotIndexWrapper>
            <HotQuestionWrapper>
                <TitleWrapper onClick={() => history.push(`/question/${item.question.id}`)}>
                    {item.question.title}
                </TitleWrapper>
                <img src={hotsvg} style={{width: 15, height: 15}} alt=""/>
                {item.hot}热度
                <Divider/>
            </HotQuestionWrapper>
        </>
    )
}

export default HotQuestionCard;