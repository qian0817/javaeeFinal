import React, {useEffect, useState} from "react";
import {useParams} from "react-router";
import {UserInfo} from "../../entity/UserInfo";
import instance from "../../axiosInstance";
import {Button, Descriptions, PageHeader, Skeleton} from "antd";


const UserPage = () => {
    const {userId} = useParams<{ userId: string }>()

    const [user, setUser] = useState<UserInfo>()

    const loadUser = async (userId: string) => {
        const user = await instance.get<UserInfo>(`/api/user/${userId}`)
        setUser(user.data)
    }

    useEffect(() => {
        loadUser(userId);
    }, [userId])


    const follow = async () => {
        await instance.post(`/api/user/${userId}/following/`)
        await loadUser(userId)
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

    return (

        <>
            <PageHeader
                ghost={false}
                title={user.username}
                extra={[
                    <>{
                        !user.isMe && user.following &&
                        <Button type="primary" danger onClick={unfollow}>取消关注</Button>
                    }{
                        !user.isMe && !user.following &&
                        <Button type="primary" onClick={follow}>关注</Button>
                    }
                    </>
                ]}>
                <Descriptions size="small" column={2}>
                    <Descriptions.Item>共回答{user.totalAnswer}个问题</Descriptions.Item>
                    <Descriptions.Item>获得{user.totalAgree}次赞同</Descriptions.Item>
                    <Descriptions.Item>关注了{user.totalFollowing}</Descriptions.Item>
                    <Descriptions.Item>关注者{user.totalFollower}</Descriptions.Item>
                </Descriptions>
            </PageHeader>
        </>
    )
}

export default UserPage;