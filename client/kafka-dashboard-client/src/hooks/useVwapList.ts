import { useEffect } from "react";
import { sendRequest } from "./Api";

const useVwapList = () => {
    
    const REQUEST_INTERVAL = 20_000;
    const url = "http://localhost:5335/top-five";

    const getTopFive = async() => {
        try {
            console.log("sending top five request")
            const topFive = await sendRequest(url);
            console.log(topFive);
        } catch(error) {
            if(error instanceof Error) {
                throw new Error(error.message);
            }
        }
    }

    useEffect(() => {
        getTopFive();
        const interval = setInterval(() => {
            getTopFive();
        }, REQUEST_INTERVAL);
        return () => clearInterval(interval);
    })
};

export default useVwapList;