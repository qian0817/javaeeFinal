import {UserVo} from "./UserVo";
import {Question} from "./Question";

export interface AnswerVo {
    id: number;
    user: UserVo;
    questionId: number;
    content: string;
    createTime: string;
    updateTime: string;
    canAgree: boolean;
    agreeNumber: number;
    question: Question;
}