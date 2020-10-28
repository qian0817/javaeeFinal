import React from "react";
import {FooterWrapper} from "./style";
import AgreeButton from "../../component/AgreeButton";


interface AnswerFooterProps {
    canAgree: boolean,
    agreeNumber: number,
    answerId: number,
    setAgreeStatus: (agree: number,canAgree: boolean) => void,
}

const AnswerFooter: React.FC<AnswerFooterProps> = ({
                                                       canAgree,
                                                       setAgreeStatus,
                                                       agreeNumber,
                                                       answerId
                                                   }) => {
    return (
        <FooterWrapper>
            <AgreeButton canAgree={canAgree}
                         agreeNumber={agreeNumber}
                         answerId={answerId}
                         setAgreeStatus={setAgreeStatus}/>
        </FooterWrapper>
    )
}

export default AnswerFooter;