export function insetrOrder(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/orders`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
        body:JSON.stringify({
            orderNo:"bo0123456789",
            userId:8,
            totalAmount:1,
            status:0,

        })
    }).then(res => res.text())
}

export function getUserOrder(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/orders/user/8`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
    }).then(res => res.text())
}

export function getAllOrder(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/orders`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
    }).then(res => res.text())
}