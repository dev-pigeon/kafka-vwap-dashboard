import { useEffect, useState } from "react";
import dayjs from "dayjs";
import {socket} from "./socket"
import log from "loglevel" 



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
    log.setLevel("WARN")

    const [vwapList, setVwapList] = useState<VwapListItem[]>([]);
    const [lastUpdated, setLastUpdated] = useState<string | null>(null);


    const processTopFiveResponse = (topFiveResponse : VwapRequestItem[]) => {
        let updatedTopFive : VwapListItem[] = [];
        log.debug("Parsing VwapListItems")
        for(const requestItem of topFiveResponse) {
            const listItem : VwapListItem = {
                vwap : parseInt(requestItem.vwap.toFixed(2)),
                ticker : requestItem.ticker,
            }
            updatedTopFive.push(listItem);
        }
        setVwapList(updatedTopFive);
        log.debug("Updated current top five list")
    }

   
    useEffect(() => {
        log.info("Connecting to websocket")
   
        socket.on("connect", () => {
            log.info("Websocket connection established")
        })

        socket.on("message", (data : {data : VwapRequestItem[]}) => {
            log.info("Receiving message from websocket")
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