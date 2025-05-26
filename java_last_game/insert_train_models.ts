export function insetrTrainModels(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/train-models`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
        body:JSON.stringify({
            modelName:"牛马号4",
            modelCode:"GB19198103",
            status:1,
            maxCapacity:200,
        })
    }).then(res => res.text())
}

export function searchTrainModels(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/train-models/search`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
        body:JSON.stringify({ 
            modelName:"2",
        })
    }).then(res => res.text())
}

export function getTraninModel(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/train-models`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
    }).then(res => res.text())
}