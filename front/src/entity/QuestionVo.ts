export interface QuestionVo {
    id: number
    title: string,
    tags?: string,
    content: string,
    canCreateAnswer: boolean,
    myAnswerId?: number
}