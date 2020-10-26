import {UserVo} from "./UserVo";

export interface AnswerVo {
    id: number;
    user: UserVo;
    questionId: number;
    content: string;
    createTime: string;
    updateTime: string;
}