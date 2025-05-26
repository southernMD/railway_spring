export function getWaitingOrder(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/waiting-orders`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
    }).then(res => res.text())
}
