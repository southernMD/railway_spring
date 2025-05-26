export function getJavaLocalDateTime(
    year: number = 1970,
    month: number = 1,
    day: number = 1,
    hour: number = 0,
    minute: number = 0,
    second: number = 0
): string {
    // 格式化数字，确保两位数
    const pad = (num: number) => String(num).padStart(2, '0');

    // 拼接成 LocalDateTime 格式
    return `${year}-${pad(month)}-${pad(day)}T${pad(hour)}:${pad(minute)}:${pad(second)}`;
}