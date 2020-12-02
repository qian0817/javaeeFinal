import React, {useEffect, useState} from "react";
import instance from "../../axiosInstance";
import {Helmet} from "react-helmet";
import {QuestionHotVo} from "../../entity/QuestionHotVo";
import HotQuestionList from "./HotQuestionList";
import RecommendQuestionList from "./RecommendQuestionList";
import {useSelector} from "react-redux";
import {RootState} from "../../store";
import {DynamicWithUserVo} from "../../entity/DynamicWithUserVo";
import {Page} from "../../entity/Page";
import DynamicList from "./DynamicList";
import TagSwitch from "./TagSwitch";
import {AnswerVo} from "../../entity/AnswerVo";

const Home = () => {
    const [recommend, setRecommend] = useState<Array<AnswerVo>>([])
    const [hotQuestions, setHotQuestions] = useState<Array<QuestionHotVo>>([])
    const [tagClicked, setTagClicked] = useState<'推荐' | '热榜' | '关注'>('推荐');
    const [loading, setLoading] = useState(false);
    const [dynamics, setDynamics] = useState<DynamicWithUserVo[]>([])
    const [dynamicPageNum, setDynamicPageNum] = useState(0)
    const [isDynamicEnd, setDynamicEnd] = useState(true)
    const loginUser = useSelector((root: RootState) => root.login)

    const loadRecommend = async () => {
        setLoading(true)
        try {
            const response = await instance.get<Array<AnswerVo>>('/api/answer/recommend')
            setRecommend(recommend => [...recommend, ...response.data])
        } finally {
            setLoading(false)
        }
    }

    const loadHotQuestion = async () => {
        setLoading(true)
        try {
            const response = await instance.get<Array<QuestionHotVo>>('/api/question/hot/')
            setHotQuestions(response.data)
        } finally {
            setLoading(false)
        }
    }

    const loadDynamics = async (pageNum: number) => {
        setLoading(true);
        setDynamicPageNum(num => num + 1)
        try {
            const response = await instance.get<Page<DynamicWithUserVo>>('/api/dynamic/',
                {params: {pageNum: pageNum}})
            setDynamics(dynamics => [...dynamics, ...response.data.content])
            setDynamicEnd(response.data.last)
        } finally {
            setLoading(false)
        }
    }


    useEffect(() => {
        switch (tagClicked) {
            case "关注":
                setDynamics([])
                setDynamicPageNum(-1)
                loadDynamics(dynamicPageNum)
                break;
            case "推荐":
                setRecommend([])
                loadRecommend()
                break;
            case "热榜":
                loadHotQuestion();
                break;
        }
    }, [dynamicPageNum, tagClicked])


    const content = () => {
        switch (tagClicked) {
            case "热榜":
                return <HotQuestionList hotQuestions={hotQuestions} loading={loading}/>;
            case "推荐":
                return <RecommendQuestionList recommend={recommend} loading={loading} loadMore={loadRecommend}/>
            case "关注":
                return <DynamicList dynamics={dynamics}
                                    loading={loading}
                                    loadMore={() => loadDynamics(dynamicPageNum)}
                                    isEnd={isDynamicEnd}/>
        }
    }

    return (
        <>
            <Helmet title="首页"/>
            <TagSwitch tagClicked={tagClicked} setTagClicked={setTagClicked} loginUser={loginUser}/>
            {content()}
        </>
    )
}

export default Home;