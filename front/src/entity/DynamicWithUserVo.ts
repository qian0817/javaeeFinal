import {UserVo} from "./UserVo";
import {Question} from "./Question";
import {AnswerWithQuestionVo} from "./AnswerWithQuestionVo";

export interface DynamicWithUserVo {
    eventId: number;
    user: UserVo;
    action: string;
    content: AnswerWithQuestionVo | Question;
    createTime: string;
}