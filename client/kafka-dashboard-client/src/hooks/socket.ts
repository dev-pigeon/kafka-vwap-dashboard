import {io, Socket} from "socket.io-client"


export const socket : Socket = io(__API_URL__, {
    transports: ['polling','websocket']
});