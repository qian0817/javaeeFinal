import React from "react";
import QuestionSearchResult from "./QuestionSearchResult";
import {Tabs} from "antd";
import AnswerSearchResult from "./AnswerSearchResult";


const {TabPane} = Tabs;

const Search = () => {
    return (
        <Tabs defaultActiveKey="1" centered>
            <TabPane tab="回答" key="1">
                <AnswerSearchResult/>
            </TabPane>
            <TabPane tab="问题" key="2">
                <QuestionSearchResult/>
            </TabPane>
        </Tabs>
    )
}

export default Search;
