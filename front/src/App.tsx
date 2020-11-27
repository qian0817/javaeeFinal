import React from 'react';
import Header from "./component/header/header";
import {Route, Switch} from "react-router";
import CreateQuestion from "./page/createQuestion";
import QuestionDetail from "./page/questionDetail";
import AnswerDetail from "./page/answerDetail";
import Home from "./page/home/Home";
import Search from "./page/search";
import NotFound from "./page/NotFound";
import LoginModal from "./component/LoginModal";
import UserPage from "./page/user";

function App() {

    return (
        <div>
            <Header/>
            <LoginModal/>
            <Switch>
                <Route path="/" exact component={Home}/>
                <Route path="/question/search/:keyword" exact component={Search}/>
                <Route path="/question/action/create" exact component={CreateQuestion}/>
                <Route path="/question/:id" exact component={QuestionDetail}/>
                <Route path="/question/:questionId/answer/:answerId" exact component={AnswerDetail}/>
                <Route path="/user/:userId" exact component={UserPage}/>
                <Route path="/" component={NotFound}/>
            </Switch>
        </div>
    );
}

export default App;
