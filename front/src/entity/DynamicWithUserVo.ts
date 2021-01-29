import {UserVo} from "./UserVo";
import {AnswerVo} from "./AnswerVo";
import {QuestionVo} from "./QuestionVo";

export interface DynamicWithUserVo {
    eventId: number;
    user: UserVo;
    action: string;
    content: AnswerVo | QuestionVo;
    createTime: string;
}