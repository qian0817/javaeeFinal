import {Question} from "./Question";
import {AnswerWithQuestionVo} from "./AnswerWithQuestionVo";

export interface DynamicVo {
    eventId: number;
    userId: number;
    action: string;
    content: AnswerWithQuestionVo | Question;
    createTime: string;
}