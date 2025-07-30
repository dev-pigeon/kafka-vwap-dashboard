export async function sendRequest(url : string) {
    try {
        const response = await fetch(url);
        if(!response.ok) throw new Error();
        const json_response = await response.json();
        return json_response;
    } catch(error) {
        if(error instanceof Error) {
            throw new Error(error.message);
        }
    }
}