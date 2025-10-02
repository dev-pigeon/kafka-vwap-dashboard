import { useEffect, useState } from "react";
import dayjs from "dayjs";
import {socket} from "./socket"


export type VwapListItem = {
    ticker : string,
    vwap : number,
}

type VwapRequestItem = {
    ticker : string,
    vwap : number,
    last_updated : string,
}

export const valueFormatter = (value : number | null) => {
    return `$${value}`
}


const useVwapList = () => {
     
    // @ts-ignore
    const REQUEST_INTERVAL = 1000;
    const [vwapList, setVwapList] = useState<VwapListItem[]>([]);
    const [lastUpdated, setLastUpdated] = useState<string | null>(null);


    const processTopFiveResponse = (topFiveResponse : VwapRequestItem[]) => {
        if(topFiveResponse.length > 0) setLastUpdated(dayjs(topFiveResponse[0].last_updated).format('hh:mm:ss A'));
        let updatedTopFive : VwapListItem[] = [];
        for(const requestItem of topFiveResponse) {
            const listItem : VwapListItem = {
                vwap : parseInt(requestItem.vwap.toFixed(2)),
                ticker : requestItem.ticker,
            }
            updatedTopFive.push(listItem);
        }
        setVwapList(updatedTopFive);
    }

   
    useEffect(() => {
        console.log("connecting to server");
        socket.on("connect", () => {
            console.log("CONNECTED")
        })

        socket.on("message", (data : {data : VwapRequestItem[]}) => {
            processTopFiveResponse(data['data'])
            const now = dayjs()
            const nowFormatted = now.format("hh:mm:ss A")
            setLastUpdated(nowFormatted)
        })

    }, [])


    return {
        vwapList,
        lastUpdated
    }
};

export default useVwapList;