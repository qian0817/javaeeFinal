import React from 'react';
import Header from "./component/header/header";
import {Route, Switch} from "react-router";
import CreateQuestion from "./page/createQuestion";
import QuestionDetail from "./page/questionDetail";
import AnswerDetail from "./page/answerDetail";
import Home from "./page/home/Home";

function App() {

    return (
        <div>
            <Header/>
            <Switch>
                <Route path="/" exact component={Home}/>
                <Route path="/question/action/create" exact component={CreateQuestion}/>
                <Route path="/question/:id" exact component={QuestionDetail}/>
                <Route path="/question/:questionId/answer/:answerId" exact component={AnswerDetail}/>
            </Switch>
        </div>
    );
}

export default App;
