import React, {useEffect, useState} from "react";
import {useParams} from "react-router";
import instance from "../../axiosInstance";
import {Button, Descriptions, PageHeader, Skeleton} from "antd";
import {DynamicVo} from "../../entity/DynamicVo";
import {Page} from "../../entity/Page";
import {Link} from "react-router-dom";
import {getTimeRange} from "../../utils/DateUtils";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../store";
import {setVisible} from "../../reducers/loginFormVisible/actionCreate";
import {getOverView} from "../../utils/StringUtils";
import AnswerCard from "../../component/answerCard";
import {AnswerVo} from "../../entity/AnswerVo";
import {QuestionVo} from "../../entity/QuestionVo";
import {UserVo} from "../../entity/UserVo";

const UserPage = () => {
    const {userId} = useParams<{ userId: string }>()
    const [user, setUser] = useState<UserVo>()
    const [dynamics, setDynamics] = useState<DynamicVo[]>([])
    const [following, setFollowing] = useState<boolean>()
    const [totalAnswer, setTotalAnswer] = useState<number>()
    const [totalAgree, setTotalAgree] = useState<number>()
    const [totalFollowing, setTotalFollowing] = useState<number>()
    const [totalFollower, setTotalFollower] = useState<number>()
    const loginUser = useSelector((root: RootState) => root.login)
    const dispatch = useDispatch()

    const loadUser = async (userId: string) => {
        const user = await instance.get<UserVo>(`/api/user/${userId}`)
        setUser(user.data)
        const dynamics = await instance.get<Page<DynamicVo>>(`/api/dynamic/user/${userId}`)
        setDynamics(dynamics.data.content)
        const totalAnswer = await instance.get<number>(`/api/answer/user/${userId}/count`)
        setTotalAnswer(totalAnswer.data)
        const totalAgree = await instance.get<number>(`/api/answer/agrees/user/${userId}/count`)
        setTotalAgree(totalAgree.data)
        const totalFollower = await instance.get<number>(`/api/followers/user/${userId}/follower/count`)
        setTotalFollower(totalFollower.data)
        const totalFollowing = await instance.get<number>(`/api/followers/user/${userId}/following/count`)
        setTotalFollowing(totalFollowing.data)
    }

    const loadFollowing = async (userId: string) => {
        if (loginUser == null) {
            setFollowing(false)
        } else if (loginUser.id.toString() !== userId) {
            const isFollowing = await instance.get <boolean>(`/api/followers/${userId}/following/${loginUser.id}`)
            console.log(isFollowing.data, loginUser.id, userId)
            setFollowing(isFollowing.data)
        }
    }
    useEffect(() => {
        loadFollowing(userId)
    }, [loginUser, userId])

    useEffect(() => {
        loadUser(userId);
    }, [userId])


    const follow = async () => {
        if (loginUser == null) {
            dispatch(setVisible(true))
        } else {
            await instance.post(`/api/followers/${userId}/following/`)
            setFollowing(true)
            setTotalFollower(n => n!! + 1)
        }
    }

    const unfollow = async () => {
        if (loginUser == null) {
            dispatch(setVisible(true))
        } else {
            await instance.delete(`/api/followers/${userId}/following/`)
            setFollowing(false)
            setTotalFollower(n => n!! - 1)
        }
    }

    if (user == null) {
        return (<>
            <Skeleton active/>
            <Skeleton active/>
            <Skeleton active/>
        </>);
    }

    const showContent = (content: AnswerVo | QuestionVo) => {
        if ('tags' in content) {
            const question = content as QuestionVo;
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
                        loginUser?.id !== user.id && following &&
                        <Button type="primary" danger onClick={unfollow}>取消关注</Button>
                    }{
                        loginUser?.id !== user.id && !following &&
                        <Button type="primary" onClick={follow}>关注</Button>
                    }
                    </>
                }
            >
                <Descriptions size="small" column={2}>
                    <Descriptions.Item>共回答{totalAnswer}个问题</Descriptions.Item>
                    <Descriptions.Item>获得{totalAgree}次赞同</Descriptions.Item>
                    <Descriptions.Item>关注了{totalFollowing}人</Descriptions.Item>
                    <Descriptions.Item>关注者{totalFollower}</Descriptions.Item>
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