import {Question} from "./Question";
import {AnswerVo} from "./AnswerVo";

export interface QuestionDetailVo {
    question:Question,
    answers:Array<AnswerVo>
}