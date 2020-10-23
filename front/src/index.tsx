import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';
import './index.css';
import * as serviceWorker from './serviceWorker';
import {BrowserRouter, Route} from "react-router-dom";
import {QueryParamProvider} from "use-query-params";
import {Provider} from "react-redux";
import store from "./store";

ReactDOM.render(
    <Provider store={store}>
        <BrowserRouter>
            <QueryParamProvider ReactRouterRoute={Route}>
                <App/>
            </QueryParamProvider>
        </BrowserRouter>
    </Provider>,
    document.getElementById('root')
);

serviceWorker.unregister();
