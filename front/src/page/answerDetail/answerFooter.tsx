import React from "react";
import {CommentButton, FooterWrapper} from "./style";
import {Button} from "antd";

const AnswerFooter = () => {
    return (
        <FooterWrapper>
            <Button type="primary">赞同</Button>
            <CommentButton type="link">评论</CommentButton>
        </FooterWrapper>
    )
}

export default AnswerFooter;