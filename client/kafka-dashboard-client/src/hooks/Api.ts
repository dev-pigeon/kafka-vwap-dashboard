export async function sendRequest<T=unknown>(url : string) : Promise<T> {
    const response = await fetch(url);
    if(!response.ok) throw new Error("Networking response failed");
    return await response.json();
}