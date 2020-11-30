import dayjs from "dayjs";

export const getTimeRange = (date: string): string => {
    const t = dayjs(date, "yyyy-MM-ddTHH:mm:ss")
    const now = dayjs()
    if (now.diff(t, "minute") < 1) {
        return `${now.diff(t, "second")}秒前`
    } else if (now.diff(t, "hour") < 1) {
        return `${now.diff(t, "minute")}分钟前`
    } else if (now.diff(t, "date") < 1) {
        return `${now.diff(t, "hour")}小时前`
    } else if (t.year() !== now.year()) {
        return `${t.year()}年${t.month() + 1}月${t.date()}日`
    } else {
        return `${t.month() + 1}月${t.date()}日`
    }
}