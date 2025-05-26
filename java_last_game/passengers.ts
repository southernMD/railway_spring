export function createPassager(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/passengers`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            userId:8,
            realName:"我不相信傻鸟的道理",
            idCard:"341181199010095810",
            passengerType:1,
            isDefault:0,
            phone:12344221122
        })
    }).then(response => response.text())
}

export function getPassagerByUserId(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/passengers/user/8`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
    }).then(response => response.text())
}