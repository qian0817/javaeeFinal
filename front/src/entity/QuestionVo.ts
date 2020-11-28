import {Question} from "./Question";

export interface QuestionVo {
    question: Question,
    canCreateAnswer: boolean,
    myAnswerId?: string
}