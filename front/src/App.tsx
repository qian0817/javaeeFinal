import React, {useEffect, useState} from 'react';
import Header from "./component/header/header";
import {Route, Switch} from "react-router";
import CreateQuestion from "./page/createQuestion";
import Register from "./page/register";
import Login from "./page/login";
import QuestionDetail from "./page/questionDetail";

function App() {
    const [loginStatus, setLoginStatus] = useState(false)
    useEffect(() => {

    }, []);
    return (
        <div>
            <Header loginStatus={loginStatus} setLoginStatus={setLoginStatus}/>
            <Switch>
                <Route path="/register" exact component={() => <Register setLoginStatus={setLoginStatus}/>}/>
                <Route path="/login" exact component={() => <Login setLoginStatus={setLoginStatus}/>}/>
                <Route path="/question/action/create" exact component={CreateQuestion}/>
                <Route path="/question/:id" exact component={QuestionDetail}/>
            </Switch>
        </div>
    );
}

export default App;
