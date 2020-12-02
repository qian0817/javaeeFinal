import {UserVo} from "./UserVo";
import {Question} from "./Question";
import {AnswerVo} from "./AnswerVo";

export interface DynamicWithUserVo {
    eventId: number;
    user: UserVo;
    action: string;
    content: AnswerVo | Question;
    createTime: string;
}