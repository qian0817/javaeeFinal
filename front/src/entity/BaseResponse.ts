export interface BaseResponse<T> {
    id: number;
    data: T,
    message: string
}