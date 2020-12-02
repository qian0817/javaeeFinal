import React, {useEffect, useState} from "react";
import {useParams} from "react-router";
import {UserInfo} from "../../entity/UserInfo";
import instance from "../../axiosInstance";
import {Button, Descriptions, PageHeader, Skeleton} from "antd";
import {DynamicVo} from "../../entity/DynamicVo";
import {Page} from "../../entity/Page";
import {Question} from "../../entity/Question";
import {Link} from "react-router-dom";
import {getTimeRange} from "../../utils/DateUtils";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../store";
import {setVisible} from "../../reducers/loginFormVisible/actionCreate";
import {getOverView} from "../../utils/StringUtils";
import AnswerCard from "../../component/answerCard";
import {AnswerVo} from "../../entity/AnswerVo";


const UserPage = () => {
    const {userId} = useParams<{ userId: string }>()
    const [user, setUser] = useState<UserInfo>()
    const [dynamics, setDynamics] = useState<DynamicVo[]>([])
    const loginUser = useSelector((root: RootState) => root.login)
    const dispatch = useDispatch()
    const loadUser = async (userId: string) => {
        const user = await instance.get<UserInfo>(`/api/user/${userId}`)
        setUser(user.data)
        const dynamics = await instance.get<Page<DynamicVo>>(`/api/dynamic/user/${userId}`)
        setDynamics(dynamics.data.content)
    }

    useEffect(() => {
        loadUser(userId);
    }, [userId])


    const follow = async () => {
        if (loginUser == null) {
            dispatch(setVisible(true))
        } else {
            await instance.post(`/api/user/${userId}/following/`)
            await loadUser(userId)
        }
    }

    const unfollow = async () => {
        await instance.delete(`/api/user/${userId}/following/`)
        await loadUser(userId)
    }

    if (user == null) {
        return (<>
            <Skeleton active/>
            <Skeleton active/>
            <Skeleton active/>
        </>);
    }

    const showContent = (content: AnswerVo | Question) => {
        if ('tags' in content) {
            const question = content as Question;
            return <Link to={`/question/${question.id}`}>
                <h2>{question.title}</h2>
                <div dangerouslySetInnerHTML={{__html: getOverView(question.content)[0]}}/>
            </Link>
        } else {
            const answer = content as AnswerVo;
            return <AnswerCard showUser={false} answer={answer} showQuestion={true}/>
        }
    }

    return (
        <>
            <PageHeader
                ghost={false}
                title={user.username}
                extra={
                    <>{
                        loginUser?.id !== user.id && user.following &&
                        <Button type="primary" danger onClick={unfollow}>取消关注</Button>
                    }{
                        loginUser?.id !== user.id && !user.following &&
                        <Button type="primary" onClick={follow}>关注</Button>
                    }
                    </>
                }>
                <Descriptions size="small" column={2}>
                    <Descriptions.Item>共回答{user.totalAnswer}个问题</Descriptions.Item>
                    <Descriptions.Item>获得{user.totalAgree}次赞同</Descriptions.Item>
                    <Descriptions.Item>关注了{user.totalFollowing}人</Descriptions.Item>
                    <Descriptions.Item>关注者{user.totalFollower}</Descriptions.Item>
                </Descriptions>
            </PageHeader>
            {
                dynamics.map(dynamic => <div key={dynamic.eventId}>
                    {dynamic.action}
                    <div style={{float: "right"}}>{getTimeRange(dynamic.createTime)}</div>
                    {showContent(dynamic.content)}
                </div>)
            }
        </>
    )
}

export default UserPage;