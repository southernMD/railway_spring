import { getTraninModel, insetrTrainModels,searchTrainModels } from "./insert_train_models";
import { addLockTask } from "./lock_seat";
import { batchSeat, insertSeat } from "./seat";
import { delTrain, getTranin, insetrTrain } from "./train";
import { insetrTrainCarigae } from "./train_cariage";
import { getAllOrder, getUserOrder, insetrOrder } from './order'
//@ts-ignore
import fs from 'fs';
import { updateBatchTrainStops, insetrTrainStops, updateTrainStops } from "./train_stop";
import { createTicket } from "./ticket";
import { createPassager, getPassagerByUserId } from "./passengers";
import { Test } from "./test";
import { getWaitingOrder } from "./waiting-orders";
import { getStationSearch } from "./route";
const baseUrl = `localhost:8080`;
(async () => {
    const { accessToken = '', refreshToken = '' } = await fetch(`${baseUrl}/auth/login/username`, {
        headers:{
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: 'zs',
            password: '123456'
        }),
        method: 'POST'
    }).then(res => res.json()).then(res => res.data || {}).catch(err => {
        console.log(err);
        return {};
    });
    console.log(accessToken,refreshToken);
    
    try {
        // const data = await insetrTrainModels(baseUrl,accessToken,refreshToken)
        // const data = await searchTrainModels(baseUrl,accessToken,refreshToken)
        // const data = await getTraninModel(baseUrl,accessToken,refreshToken)
        // const data = await insetrTrainCarigae(baseUrl,accessToken,refreshToken)
        // const data = await batchSeat(baseUrl,accessToken,refreshToken)
        // const data = await insertSeat(baseUrl,accessToken,refreshToken)
        // const data = await addLockTask(baseUrl,accessToken,refreshToken)
        // const data = await getTranin(baseUrl,accessToken,refreshToken)
        // const data = await insetrTrain(baseUrl,accessToken,refreshToken)
        // const data = await delTrain(baseUrl,accessToken,refreshToken)
        // const data = await insetrTrainStops(baseUrl,accessToken,refreshToken)
        // const data = await updateBatchTrainStops(baseUrl,accessToken,refreshToken)
        // const data = await updateTrainStops(baseUrl,accessToken,refreshToken)
        // const data = await insetrOrder(baseUrl,accessToken,refreshToken)
        // const data = await getUserOrder(baseUrl,accessToken,refreshToken);
        // const data = await createTicket(baseUrl,accessToken,refreshToken);
        // const data = await createPassager(baseUrl,accessToken,refreshToken);
        // const data = await getPassagerByUserId(baseUrl,accessToken,refreshToken);
        // const data = await Test(baseUrl,accessToken,refreshToken);
        // const data = await getAllOrder(baseUrl,accessToken,refreshToken);
        // const data = await getWaitingOrder(baseUrl,accessToken,refreshToken);
        const data = await getStationSearch(baseUrl,accessToken,refreshToken);
        
        
        console.log(data);
        fs.writeFileSync('output.json', JSON.parse(JSON.stringify(data, null, 2)), 'utf-8');
    } catch (error) {
        console.log(error);
    }

})();