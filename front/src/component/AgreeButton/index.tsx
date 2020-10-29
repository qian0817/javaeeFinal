import React, {useState} from "react";
import {Button, message} from "antd";
import {AxiosError} from "axios";
import {ErrorResponse} from "../../entity/ErrorResponse";
import instance from "../../axiosInstance";
import {useSelector} from "react-redux";
import {RootState} from "../../store";

interface AgreeButtonProps {
    canAgree: boolean,
    setAgreeStatus: (agree: number, canAgree: boolean) => void,
    agreeNumber: number,
    answerId: number
}

const AgreeButton: React.FC<AgreeButtonProps> = ({canAgree, setAgreeStatus, agreeNumber, answerId}) => {
    const [loading, setLoading] = useState(false)
    const loginUser = useSelector((state: RootState) => state.login)

    const agree = async () => {
        if (loginUser == null) {
            message.warn("请先登录")
            return
        }
        setLoading(true)
        try {
            await instance.post(`/api/answer/id/${answerId}/agree/`)
            setAgreeStatus(agreeNumber + 1, false)
        } catch (e) {
            const ex: AxiosError<ErrorResponse> = e
            message.warn(ex.response?.data.message)
        } finally {
            setLoading(false)
        }
    }

    const deleteAgree = async () => {
        setLoading(true)
        try {
            await instance.delete(`/api/answer/id/${answerId}/agree/`)
            setAgreeStatus(agreeNumber - 1, true)
        } catch (e) {
            const ex: AxiosError<ErrorResponse> = e
            message.warn(ex.response?.data.message)
        } finally {
            setLoading(false)
        }
    }
    if (canAgree) {
        return <Button loading={loading} onClick={agree}>
            赞同{agreeNumber}
        </Button>
    } else {
        return <Button loading={loading} type="primary" onClick={deleteAgree}>
            已赞同{agreeNumber}
        </Button>
    }
}

export default AgreeButton;