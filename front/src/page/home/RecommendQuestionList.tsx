import QuestionCard from "./QuestionCard";
import {Skeleton} from "antd";
import {LoadMoreButton} from "./style";
import React from "react";
import {Question} from "../../entity/Question";

interface ListProps {
    questions: Question[],
    loading: boolean,
    loadMore: React.MouseEventHandler<HTMLElement>
}

const RecommendQuestionList: React.FC<ListProps> = ({questions, loading, loadMore}) => {
    return (
        <>
            {questions.map((item, index) => <QuestionCard item={item} key={index}/>)}
            {
                loading &&
                <>
                    <Skeleton active/>
                    <Skeleton active/>
                    <Skeleton active/>
                    <Skeleton active/>
                    <Skeleton active/>
                    <Skeleton active/>
                </>
            }
            <LoadMoreButton type="link" block onClick={loadMore}>点击加载更多</LoadMoreButton>
        </>
    )
}

export default RecommendQuestionList;