import { useEffect, useState } from "react";
import { sendRequest } from "./Api";
import dayjs from "dayjs";


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
    
    const REQUEST_INTERVAL = 20_000;
    const url = "http://localhost:5335/top-five";
    const [vwapList, setVwapList] = useState<VwapListItem[]>([]);
    const [lastUpdated, setLastUpdated] = useState<string | null>(null);

    const getTopFive = async() => {
        try {
            const topFiveRequest = await sendRequest<VwapRequestItem[]>(url);
            processTopFiveResponse(topFiveRequest);
        } catch(error) {
            if(error instanceof Error) {
                throw new Error(error.message);
            }
        }
    }


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
        getTopFive();
        const interval = setInterval(() => {
            getTopFive();
        }, REQUEST_INTERVAL);
        return () => clearInterval(interval);
    },[]);


    return {
        vwapList,
        lastUpdated
    }
};

export default useVwapList;