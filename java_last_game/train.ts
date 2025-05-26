export function getTranin(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/trains`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
    }).then(res => res.text())
}

export function insetrTrain(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/trains`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            trainNumber: 'GZ-2021-05-01-01',
            modelId:1,
            startStationId:1,
            endStationId:2,
            date:"2021-05-21",
            departureTime:"08:00:00",
            arrivalTime:"09:00:00",
            trainSeatInfo:{
                noSeatTickets:0,
                businessPrice:0,
                firstClassPrice:0,
                secondClassPrice:0,
                noSeatPrice:0
            }
        })
    }).then(res => res.text())
}

export function delTrain(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/trains/3`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
    }).then(res => res.text())
}