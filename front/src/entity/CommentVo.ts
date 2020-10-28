import {UserVo} from "./UserVo";
import {Comment} from "./Comment";

export interface CommentVo {
    userVo: UserVo;
    comment: Comment;
}