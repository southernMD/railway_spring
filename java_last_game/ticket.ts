export function createTicket(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/tickets`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
        body:JSON.stringify({
            ticketNo: "TICKET123_1",
            orderId: 1,
            passengerId: 1,
            trainId: 4,
            date: "2025-05-17",
            departureStationId: 4,
            arrivalStationId: 1,
            seatType: 1,
            price: 100.00,
            status: 5,
            seatId: 1,
            carriageId:1
        })
    }).then(res =>res.text())
}
