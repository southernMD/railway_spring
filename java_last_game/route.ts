export function getStationSearch(baseUrl: string,accessToken:string,refreshToken:string) {
    return fetch(`${baseUrl}/trains/route?startStationId=${1}&endStationId=${5}&date=2025-05-26`, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Refresh-Token': `${refreshToken}`,
            'Content-Type': 'application/json'
        },

    }).then(res =>res.text())
}
