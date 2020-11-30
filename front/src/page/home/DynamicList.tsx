import React from "react";
import {DynamicWithUserVo} from "../../entity/DynamicWithUserVo";
import {Divider, Skeleton} from "antd";
import {LoadMoreButton} from "./style";
import {Link} from "react-router-dom";
import {AnswerWithQuestionVo} from "../../entity/AnswerWithQuestionVo";
import {Question} from "../../entity/Question";
import {getTimeRange} from "../../utils/DateUtils";

interface DynamicListProps {
    dynamics: DynamicWithUserVo[],
    loading: boolean,
    loadMore: React.MouseEventHandler<HTMLElement>,
    isEnd: boolean
}

const DynamicList: React.FC<DynamicListProps> = ({dynamics, loading, loadMore, isEnd}) => {


    const cardContent = (content: AnswerWithQuestionVo | Question) => {
        if ('tags' in content) {
            const question = content as Question;
            return (
                <div>
                    <h2>
                        <Link to={`/question/${question.id}`}>
                            {question.title}
                        </Link>
                    </h2>
                    <div dangerouslySetInnerHTML={{__html: question.content}}/>
                </div>
            )
        } else {
            const answer = content as AnswerWithQuestionVo;
            return (
                <div>
                    <Link to={`/question/${answer.question.id}`}>
                        <h2>{answer.question.title}</h2>
                    </Link>
                    <Link
                        to={`/question/${answer.question.id}/answer/${answer.answer.id}`}>
                        <div dangerouslySetInnerHTML={{__html: answer.answer.content}}/>
                    </Link>
                </div>
            )
        }
    }

    const card = (dynamic: DynamicWithUserVo) => {
        return <>
            <Link to={`/user/${dynamic.user.id}`}>{dynamic.user.username} </Link>
            {dynamic.action}
            <div style={{float: "right"}}>{getTimeRange(dynamic.createTime)}</div>
            {cardContent(dynamic.content)}
            <Divider/>
        </>
    }

    return (
        <>
            {dynamics.map(dynamic => card(dynamic))}
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
            {
                isEnd && <LoadMoreButton type="text" block disabled>没有动态了</LoadMoreButton>
            }
            {
                !isEnd && <LoadMoreButton type="link" block onClick={loadMore}>点击加载更多</LoadMoreButton>
            }
        </>
    )
}

export default DynamicList;