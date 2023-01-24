import React, {useState, useEffect} from 'react';

interface ClientMessage {
    id: string;
    message: string;
}

interface ServerMessage {
    id: string;
    message: string;
    type: ServerMessageType;
}

type ServerMessageType = 'CLIENT_ID_ASSIGN' | 'TRANSCRIPTION';

const App: React.FC = () => {
    const [clientId, setClientId] = useState<string>("");
    const [isConnected, setIsConnected] = useState<boolean>(false);
    const [serverMessage, setServerMessage] = useState<string>("");
    const [clientMessage, setClientMessage] = useState<string>("");
    const [socket, setSocket] = useState<WebSocket | null>(null);

    useEffect(() => {
        console.log("Use effect");

        const newSocket = new WebSocket("ws://localhost:12000/websocket/transcript");
        newSocket.onopen = () => {
            console.log("newSocket.onopen");
            setIsConnected(true);
        };
        newSocket.onmessage = (event) => {
            const message = JSON.parse(event.data);
            console.log(`newSocket.onmessage => ${message}`);
            if (message.id) setClientId(message.id);
            if (message.message) setServerMessage(message.message);
        };
        newSocket.onclose = () => {
            console.log("newSocket.onclose");
            setIsConnected(false);
        };
        setSocket(newSocket);

        return () => {
            newSocket.close();
        };
    }, [isConnected]);

    const sendMessage = (message:string) => {
        if (socket && isConnected) {
            const messageToSend: ClientMessage = {
                id: clientId,
                message
            };
            socket.send(JSON.stringify(messageToSend));
        }
    };

    return (
        <div>
            <h1>Server message: {serverMessage}</h1>
            <h2>Client id: {clientId}</h2>
            <input
                type="text"
                placeholder="Type message to send to server"
                onChange={(e) => setClientMessage(e.target.value)}
            />
            <button onClick={() => sendMessage(clientMessage)}>Send</button>
        </div>
    );
};

export default App;