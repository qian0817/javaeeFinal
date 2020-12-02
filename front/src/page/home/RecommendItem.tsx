import {TitleWrapper} from "./style";
import {Divider} from "antd";
import React from "react";
import {useHistory} from "react-router";
import {getOverView} from "../../utils/StringUtils";
import {AnswerVo} from "../../entity/AnswerVo";
import {Link} from "react-router-dom";

interface RecommendItemProps {
    item: AnswerVo
}

const RecommendItem: React.FC<RecommendItemProps> = ({item}) => {
    const history = useHistory();

    return (
        <>
            <TitleWrapper onClick={() => history.push(`/question/${item.question.id}`)}>
                {item.question.title}
            </TitleWrapper>
            <Link to={`/user/${item.user.id}`}>{item.user.username}</Link>
            <Link to={`/question/${item.question.id}/answer/${item.id}`}>
                <div dangerouslySetInnerHTML={{__html: getOverView(item.content)[0]}}/>
            </Link>
            <Divider/>
        </>
    )
}

export default RecommendItem;