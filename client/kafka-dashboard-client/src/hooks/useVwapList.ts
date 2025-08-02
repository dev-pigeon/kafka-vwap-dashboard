import { useEffect, useState } from "react";
import { sendRequest } from "./Api";


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

    const getTopFive = async() => {
        try {
            const topFiveRequest = await sendRequest<VwapRequestItem[]>(url);
            const updatedTopFive = processTopFiveResponse(topFiveRequest);
            console.log(updatedTopFive);
            setVwapList(updatedTopFive);
        } catch(error) {
            if(error instanceof Error) {
                throw new Error(error.message);
            }
        }
    }

    const processTopFiveResponse = (topFiveResponse : VwapListItem[]) : VwapListItem[] => {
        let updatedTopFive : VwapListItem[] = [];
        for(const requestItem of topFiveResponse) {
            const listItem : VwapListItem = {
                vwap : parseInt(requestItem.vwap.toFixed(2)),
                ticker : requestItem.ticker,
            }
            updatedTopFive.push(listItem);
        }
        return updatedTopFive;
    }

    useEffect(() => {
        getTopFive();
        const interval = setInterval(() => {
            getTopFive();
        }, REQUEST_INTERVAL);
        return () => clearInterval(interval);
    },[]);

    return {
        vwapList
    }
};

export default useVwapList;