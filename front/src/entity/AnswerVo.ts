import {UserVo} from "./UserVo";
import {Question} from "./Question";

export interface AnswerVo {
    id: string;
    user: UserVo;
    questionId: string;
    content: string;
    createTime: string;
    updateTime: string;
    canAgree: boolean;
    agreeNumber: number;
    question: Question;
}