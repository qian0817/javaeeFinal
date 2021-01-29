import {UserVo} from "./UserVo";
import {QuestionVo} from "./QuestionVo";

export interface AnswerVo {
    id: number;
    user: UserVo;
    questionId: number;
    content: string;
    createTime: string;
    updateTime: string;
    canAgree: boolean;
    agreeNumber: number;
    question: QuestionVo;
}