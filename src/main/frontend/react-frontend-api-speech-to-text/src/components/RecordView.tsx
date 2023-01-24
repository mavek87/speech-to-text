import {useReactMediaRecorder} from "react-media-recorder";
import axios from 'axios';
import {useState} from "react";

const RecordView = () => {
    const [transcription, setTranscription] = useState("");
    const [counter, setCounter] = useState(0);
    const [millis, setMillis] = useState(0);
    const [seconds, setSeconds] = useState(0);

    const updateMillisAndSeconds = (ms: number) => {
        setMillis(Math.floor(ms) % 100);
        setSeconds(Math.floor(ms / 1000) % 60);
    }

    const postVoiceToServer = async (blob: Blob) => {
        try {
            // https://stackoverflow.com/questions/72870388/send-blob-file-via-axios
            const {data, status} = await axios.post<void>("http://localhost:12000/api/speech-to-text", blob);
            console.log(JSON.stringify(data, null, 4));
            console.log('response status is: ', status);
            return data;
        } catch (error) {
            if (axios.isAxiosError(error)) {
                console.log('error message: ', error.message);
                return error.message;
            } else {
                console.log('unexpected error: ', error);
                return 'An unexpected error occurred';
            }
        }
    }

    const onStartRecording = () => {
        setMillis(0);
        setSeconds(0);
    }

    const onStopRecording = async (blobUrl: string, blob: Blob) => {
        setCounter(0);
        setMillis(0);
        setSeconds(0);
        const intervalID = setInterval(() => setCounter((oldCounter) => {
            const counter = oldCounter + 1;
            updateMillisAndSeconds(counter)
            return counter;
        }), 1);
        setTranscription("Elaborazione trascrizione in corso ...")
        console.log(blob);
        const data = await postVoiceToServer(blob);
        console.log(data);
        if (data) {
            setTranscription(data);
        } else {
            setTranscription("")
        }
        clearInterval(intervalID);
    }

    const {status, startRecording, stopRecording, mediaBlobUrl} = useReactMediaRecorder({
            audio: true,
            blobPropertyBag: {type: 'audio/mpeg'},
            onStop: onStopRecording,
            onStart: onStartRecording
        }
    );

    return (
        <>
            <div>
                <p>{status}</p>
                <p>{mediaBlobUrl}</p>
                {status !== 'recording' && <button onClick={startRecording}>Start Recording</button>}
                {status === 'recording' && <button onClick={stopRecording}>Stop Recording</button>}
                <video src={mediaBlobUrl} controls autoPlay/>
            </div>
            <br/>
            <br/>
            <br/>
            <div>
                <h2>Di qualcosa per vederlo trascritto sotto:</h2>
                {<h5>tempo impiegato: {seconds}s, {millis}ms</h5>}
                <div>
                    {status === 'stopped' && transcription}
                    {status === 'recording' && 'In ascolto ...'}
                </div>
                <input type="text"></input>
            </div>
        </>
    );
};

export default RecordView;