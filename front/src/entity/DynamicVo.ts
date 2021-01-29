import {AnswerVo} from "./AnswerVo";
import {QuestionVo} from "./QuestionVo";

export interface DynamicVo {
    eventId: number;
    userId: number;
    action: string;
    content: AnswerVo | QuestionVo;
    createTime: string;
}