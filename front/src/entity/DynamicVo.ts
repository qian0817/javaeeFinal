import {Question} from "./Question";
import {AnswerVo} from "./AnswerVo";

export interface DynamicVo {
    eventId: number;
    userId: number;
    action: string;
    content: AnswerVo | Question;
    createTime: string;
}