import { useEffect, useState } from "react";
import { sendRequest } from "./Api";
import dayjs, { Dayjs } from "dayjs";

export interface VwapRequestItem {
    ticker : string,
    vwap : number,
    last_updated: string
}

export interface VwapListItem {
    ticker : string,
    vwap : string,
    last_updated: Dayjs,
}

const useVwapList = () => {
    
    const REQUEST_INTERVAL = 20_000;
    const url = "http://localhost:5335/top-five";
    const [vwapList, setVwapList] = useState<VwapListItem[]>([]);

    const getTopFive = async() => {
        try {
            const topFiveRequest = await sendRequest<VwapRequestItem[]>(url);
            const updatedTopFive = processTopFiveResponse(topFiveRequest);
            setVwapList(updatedTopFive);
        } catch(error) {
            if(error instanceof Error) {
                throw new Error(error.message);
            }
        }
    }

    const processTopFiveResponse = (topFiveResponse : VwapRequestItem[]) : VwapListItem[] => {
        let updatedTopFive : VwapListItem[] = [];
        for(const requestItem of topFiveResponse) {
            const listItem : VwapListItem = {
                ticker : requestItem.ticker,
                vwap : requestItem.vwap.toFixed(2),
                last_updated : dayjs(requestItem.last_updated),
            }
            updatedTopFive.push(listItem);
        }
        return updatedTopFive;
    }

    const valueFormatter = (value : number) => {
        return `$${value}`
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