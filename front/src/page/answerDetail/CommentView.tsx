import React, {Fragment, useEffect, useState} from "react";
import {Button, Comment, Form, Input, message, Pagination} from "antd";
import instance from "../../axiosInstance";
import {CommentVo} from "../../entity/CommentVo";
import {AxiosError} from "axios";
import {ErrorResponse} from "../../entity/ErrorResponse";
import {Page} from "../../entity/Page";
import {useSelector} from "react-redux";
import {RootState} from "../../store";

interface CommentViewProps {
    answerId: string
}

const CommentView: React.FC<CommentViewProps> = ({answerId}) => {
    const [submitting, setSubmitting] = useState(false)
    const [comments, setComments] = useState<CommentVo[]>([])
    const [pageNum, setPageNum] = useState(1)
    const [total, setTotal] = useState(1)
    const loginUser = useSelector((state: RootState) => state.login)
    const [form] = Form.useForm();

    const loadComment = async (answerId: string, pageNum: number) => {
        try {
            const response = await instance
                .get<Page<CommentVo>>(`/api/comment/answer/${answerId}`, {
                    params: {pageNum: pageNum - 1}
                })
            setComments(response.data.content)
            setTotal(response.data.totalElements)
        } catch (e) {
            const ex: AxiosError<ErrorResponse> = e
            message.warn(ex.response?.data.message)
        }
    }

    useEffect(() => {
        loadComment(answerId, pageNum)
    }, [answerId, pageNum])

    const onSubmit = async (values: any) => {
        if (loginUser == null) {
            message.warn("请先登录")
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

        } catch (e) {
            const ex: AxiosError<ErrorResponse> = e
            message.warn(ex.response?.data.message)
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
            {comments.map((item => <Comment content={
                <div>
                    <h2>{item.userVo.username}</h2>
                    {item.comment.content}
                </div>
            }/>))}
        </Fragment>

    )
}

export default CommentView