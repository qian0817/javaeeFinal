export const getOverView = (content: string, length: number = 100): [string, boolean] => {
    const overView = content.replace(/<[^>]+>/g, "");
    if (overView.length < length) {
        return [overView, false]
    } else {
        return [overView.substr(0, length) + "...", true];
    }
}