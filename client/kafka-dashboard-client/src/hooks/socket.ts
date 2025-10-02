import {io, Socket} from "socket.io-client"

const URL = "http://localhost:5335/"

export const socket : Socket = io(URL, {
    transports: ['websocket']
});