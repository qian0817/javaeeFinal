import React, {useState} from "react";
import {Button} from "antd";
import instance from "../../axiosInstance";
import {useDispatch, useSelector} from "react-redux";
import {RootState} from "../../store";
import {setVisible} from "../../reducers/loginFormVisible/actionCreate";

interface AgreeButtonProps {
    canAgree: boolean,
    setAgreeStatus: (agree: number, canAgree: boolean) => void,
    agreeNumber: number,
    answerId: string
}

const AgreeButton: React.FC<AgreeButtonProps> = ({canAgree, setAgreeStatus, agreeNumber, answerId}) => {
    const [loading, setLoading] = useState(false)
    const dispatch = useDispatch()
    const loginUser = useSelector((state: RootState) => state.login)

    const agree = async () => {
        if (loginUser == null) {
            dispatch(setVisible(true))
            return
        }
        setLoading(true)
        try {
            await instance.post(`/api/answer/id/${answerId}/agree/`)
            setAgreeStatus(agreeNumber + 1, false)
        } finally {
            setLoading(false)
        }
    }

    const deleteAgree = async () => {
        setLoading(true)
        try {
            await instance.delete(`/api/answer/id/${answerId}/agree/`)
            setAgreeStatus(agreeNumber - 1, true)
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