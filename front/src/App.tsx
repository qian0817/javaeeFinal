import React from 'react';
import Header from "./component/header/header";
import {Route, Switch} from "react-router";
import CreateQuestion from "./page/createQuestion";
import QuestionDetail from "./page/questionDetail";
import AnswerDetail from "./page/answerDetail";
import Home from "./page/home/Home";
import Search from "./page/search";
import NotFound from "./page/notFound";
import LoginModal from "./component/LoginModal";
import UserPage from "./page/user";
import {Col, Row} from "antd";

function App() {

    return (
        <>
            <Header/>
            <LoginModal/>
            <Row style={{marginTop:10}}>
                <Col xs={{span: 22, offset: 1}}
                     sm={{span: 20, offset: 2}}
                     md={{span: 18, offset: 3}}
                     lg={{span: 16, offset: 4}}
                     xl={{span: 14, offset: 5}}
                     xxl={{span: 12, offset: 6}}>
                    <Switch>
                        <Route path="/" exact component={Home}/>
                        <Route path="/question/search/:keyword" exact component={Search}/>
                        <Route path="/question/action/create" exact component={CreateQuestion}/>
                        <Route path="/question/:id" exact component={QuestionDetail}/>
                        <Route path="/question/:questionId/answer/:answerId" exact component={AnswerDetail}/>
                        <Route path="/user/:userId" exact component={UserPage}/>
                        <Route path="/" component={NotFound}/>
                    </Switch>
                </Col>
            </Row>
        </>
    );
}

export default App;
