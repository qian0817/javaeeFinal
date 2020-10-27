import {Question} from "./Question";
import {AnswerVo} from "./AnswerVo";
import {Page} from "./Page";

export interface QuestionDetailVo {
    question:Question,
    answers:Page<AnswerVo>
}