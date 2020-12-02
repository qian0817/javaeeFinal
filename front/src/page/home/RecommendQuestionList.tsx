import {Skeleton} from "antd";
import {LoadMoreButton} from "./style";
import React from "react";
import {AnswerVo} from "../../entity/AnswerVo";
import AnswerCard from "../../component/answerCard";

interface ListProps {
    recommend: AnswerVo[],
    loading: boolean,
    loadMore: React.MouseEventHandler<HTMLElement>
}

const RecommendQuestionList: React.FC<ListProps> = ({recommend, loading, loadMore}) => {
    return (
        <>
            {recommend.map((item, index) =>
                <AnswerCard showQuestion={true}
                            showUser={true}
                            answer={item} key={index}/>)}
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