import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import './index.css';
import * as serviceWorker from './serviceWorker';
import {BrowserRouter, Route} from "react-router-dom";
import {QueryParamProvider} from "use-query-params";

ReactDOM.render(
    <BrowserRouter>
        <QueryParamProvider ReactRouterRoute={Route}>
            <App/>
        </QueryParamProvider>
    </BrowserRouter>,
    document.getElementById('root')
);

serviceWorker.unregister();
