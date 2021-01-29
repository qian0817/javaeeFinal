import React from "react";
import {QuestionHotVo} from "../../entity/QuestionHotVo";
import {HotIndexWrapper, HotQuestionWrapper, TitleWrapper} from "./style";
import {Divider} from "antd";
import {useHistory} from "react-router";
import hotsvg from './hot.svg'
import {getOverView} from "../../utils/StringUtils";

interface HotQuestionCardProps {
    item: QuestionHotVo,
    index: number
}

const HotQuestionItem: React.FC<HotQuestionCardProps> = ({item, index}) => {
    const history = useHistory()

    const getIndexColor = (index: number): string => {
        if (index <= 3) {
            return "#ff9607"
        } else {
            return "#999"
        }
    }

    const indexColor = getIndexColor(index);

    const overView = getOverView(item.content, 50)

    return (
        <>
            <HotIndexWrapper>
                <p style={{color: indexColor, fontSize: 15}}>{index}</p>
            </HotIndexWrapper>
            <HotQuestionWrapper>
                <TitleWrapper onClick={() => history.push(`/question/${item.id}`)}>
                    {item.title}
                </TitleWrapper>
                <div>{overView}</div>
                <img src={hotsvg} style={{width: 15, height: 15}} alt=""/>
                {item.hot}热度
                <Divider/>
            </HotQuestionWrapper>
        </>
    )
}

export default HotQuestionItem;