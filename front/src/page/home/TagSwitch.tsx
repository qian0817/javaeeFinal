import {SwitchHotWrapper} from "./style";
import {Button} from "antd";
import React from "react";
import {UserVo} from "../../entity/UserVo";

interface TagSwitchProps {
    tagClicked: '推荐' | '热榜' | '关注',
    setTagClicked: (tagClicked: '推荐' | '热榜' | '关注') => void,
    loginUser: UserVo | null
}

const TagSwitch: React.FC<TagSwitchProps> = ({tagClicked, setTagClicked, loginUser}) => {
    return (
        <SwitchHotWrapper>
            <Button type={tagClicked === '推荐' ? "text" : "link"}
                    style={{fontSize: 30}}
                    onClick={() => setTagClicked('推荐')}>推荐</Button>

            <Button type={tagClicked === '热榜' ? "text" : "link"}
                    style={{fontSize: 30}}
                    onClick={() => setTagClicked('热榜')}>热榜</Button>
            {loginUser && <Button type={tagClicked === '关注' ? "text" : "link"}
                                  style={{fontSize: 30}}
                                  onClick={() => setTagClicked('关注')}>关注</Button>}
        </SwitchHotWrapper>
    )
}

export default TagSwitch;