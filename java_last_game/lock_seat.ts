import { getJavaLocalDateTime } from './utils/getJavaLocalDateTime'
export function addLockTask(baseUrl: string,accessToken:string,refreshToken:string){
    return fetch(`${baseUrl}/seat-locks`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            seatId:3,
            lockStart:getJavaLocalDateTime(),
            expireTime:getJavaLocalDateTime(2025,5,17,16,54)
        })
    }).then(res => res.text())
}