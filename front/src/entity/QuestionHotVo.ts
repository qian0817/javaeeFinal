import {QuestionVo} from "./QuestionVo";

export interface QuestionHotVo {    id: number
    title: string,
    tags?: string,
    content: string,
    canCreateAnswer: boolean,
    myAnswerId?: number
    hot: number;
}