import React, {Fragment, useEffect, useState} from "react";
import {Button, Comment, Form, Input, message, Pagination} from "antd";
import instance from "../../axiosInstance";
import {CommentVo} from "../../entity/CommentVo";
import {Page} from "../../entity/Page";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../store";
import {setVisible} from "../../reducers/loginFormVisible/actionCreate";
import {useHistory} from "react-router";

interface CommentViewProps {
    answerId: string
}

const CommentView: React.FC<CommentViewProps> = ({answerId}) => {
    const [submitting, setSubmitting] = useState(false)
    const [comments, setComments] = useState<CommentVo[]>([])
    const [pageNum, setPageNum] = useState(1)
    const [total, setTotal] = useState(1)
    const loginUser = useSelector((state: RootState) => state.login)
    const dispatch = useDispatch();
    const history = useHistory();
    const [form] = Form.useForm();

    const loadComment = async (answerId: string, pageNum: number) => {
        const response = await instance
            .get<Page<CommentVo>>(`/api/comment/answer/${answerId}`, {
                params: {pageNum: pageNum - 1}
            })
        setComments(response.data.content)
        setTotal(response.data.totalElements)
    }

    useEffect(() => {
        loadComment(answerId, pageNum)
    }, [answerId, pageNum])

    const onSubmit = async (values: any) => {
        if (loginUser == null) {
            dispatch(setVisible(true))
            return
        }
        setSubmitting(true)
        try {
            await instance.post<CommentVo>(`/api/comment/answer/${answerId}`, {
                answerId: answerId,
                content: values.content,
            })
            message.success("评论成功")
            if (pageNum === 1) {
                await loadComment(answerId, pageNum)
            } else {
                setPageNum(1)
            }
            form.resetFields()
        } finally {
            setSubmitting(false)
        }
    }


    return (
        <Fragment>
            <Comment
                content={
                    <Form onFinish={onSubmit} form={form}>
                        <Form.Item name="content"
                                   rules={[{required: true}]}>
                            <Input.TextArea rows={4} placeholder="评论内容"/>
                        </Form.Item>
                        <Form.Item>
                            <Button htmlType="submit"
                                    loading={submitting}
                                    type="primary">
                                评论
                            </Button>
                        </Form.Item>
                    </Form>
                }
            />
            <Pagination size="small"
                        total={total}
                        hideOnSinglePage
                        current={pageNum}
                        pageSize={10}
                        onChange={page => setPageNum(page)}/>
            {comments.map((item => <Comment
                author={
                    <Button type="link" onClick={() => history.push(`/user/${item.userVo.id}`)}>
                        {item.userVo.username}
                    </Button>}
                content={<p>{item.comment.content}</p>}
                key={item.comment.id}/>))}
        </Fragment>

    )
}

export default CommentView