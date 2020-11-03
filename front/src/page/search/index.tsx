import React from "react";
import {useParams} from "react-router";

const Search = () => {
    const {keyword} = useParams();
    return (
        <div>
            {keyword}
        </div>
    )
}

export default Search;
