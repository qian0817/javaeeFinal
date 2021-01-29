import HotQuestionItem from "./HotQuestionItem";
import {Skeleton} from "antd";
import React from "react";
import {QuestionHotVo} from "../../entity/QuestionHotVo";

interface HotQuestionProps {
    hotQuestions: QuestionHotVo[],
    loading: boolean
}

const HotQuestionList: React.FC<HotQuestionProps> = ({hotQuestions, loading}) => {

    return (
        <>
            {hotQuestions.map((item, index) =>
                <HotQuestionItem item={item} index={index + 1} key={item.id}/>)
            }
            {loading &&
            <>
                <Skeleton active/>
                <Skeleton active/>
                <Skeleton active/>
                <Skeleton active/>
                <Skeleton active/>
                <Skeleton active/>
            </>}
        </>
    )
}

export default HotQuestionList;