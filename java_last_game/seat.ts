export function batchSeat(baseUrl: string, accessToken: string, refreshToken: string) {
    return fetch(`${baseUrl}/seats/batch`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify([
            {
                "carriageId": 3,
                "seatNumber": "B1",
                "status": 1
            },
            {
                "carriageId": 3,
                "seatNumber": "A1",
                "status": 1
            },
            {
                "carriageId": 3,
                "seatNumber": "A2",
                "status": 1
            },
        ])
    }).then(res => res.text())
}

export function insertSeat(baseUrl: string, accessToken: string, refreshToken: string) {
    return fetch(`${baseUrl}/seats`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            carriageId: 1,
            seatNumber: "A3",
            status: 1
        })
    }).then(res => res.text())
}
