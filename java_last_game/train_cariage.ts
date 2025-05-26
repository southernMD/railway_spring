export function insetrTrainCarigae(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/carriages`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },
        body:JSON.stringify({
            modelId:1,
            carriageNumber:1,
            carriageType:0,
            seatCount:20,
        })
    }).then(res => res.text())
}
