export function Test(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/test`, {
        method: 'POST',
        headers: {
            // 'Authorization': `Bearer ${accessToken}`,
            // 'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
        body:JSON.stringify({
            ticketId: 1,
            newTicketId: 1,
            orderId: 1,
            userId:1
        })
    }).then(res =>res.text())
}
