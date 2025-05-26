

export function insetrTrainStops(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/train-stops`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            trainId:4,
            stationId:1,
            sequence:3,
            arrivalTime:"14:00:00",
            stopDuration:"3"
        })
    }).then(res => res.text())
}

export function updateBatchTrainStops(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/train-stops/2`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            trainId:4,
            stationId:4,
            sequence:1,
            arrivalTime:"11:00:00",
            stopDuration:"3"
        })
    }).then(res => res.text())
}

export function updateTrainStops(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/train-stops/batch-update`, {
        method: 'PUT',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            trainId:4,
            trainStops:[
                {
                    id:2,
                    trainId:4,
                    stationId:4,
                    sequence:1,
                    arrivalTime:"10:00:00",
                    stopDuration:"3"
                },
                {
                    id:4,
                    trainId:4,
                    stationId:3,
                    sequence:2,
                    arrivalTime:"10:01:00",
                    stopDuration:"3"
                }
            ]
        })
    }).then(res => res.text())
}