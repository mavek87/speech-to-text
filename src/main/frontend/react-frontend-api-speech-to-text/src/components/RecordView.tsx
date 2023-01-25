import {useReactMediaRecorder} from "react-media-recorder";
import axios from 'axios';
import {useState} from "react";
import {microphone, trashCanIcon} from "../icons/icons";
import AudioInputText from "./AudioInputText";
import AudioButton from "./AudioButton/AudioButton";

const RecordView = () => {
    const [transcription, setTranscription] = useState("");
    const [counter, setCounter] = useState(0);

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
        setCounter(0);
    }

    const onStopRecording = async (blobUrl: string, blob: Blob) => {
        setCounter(0);
        const intervalID = setInterval(() => setCounter((oldCounter) => oldCounter + 1), 1000);
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
                {status !== 'recording' && <button style={{height: "30px"}} onClick={startRecording}>Speak {microphone}</button>}
                {status === 'recording' && <button onClick={stopRecording}>Stop Recording</button>}
                <video src={mediaBlobUrl} controls autoPlay/>
            </div>
            <br/>
            <br/>
            <br/>
            <div>
                <h2>Di qualcosa per vederlo trascritto sotto:</h2>
                {<h5>tempo impiegato: {counter}</h5>}
                <div>
                    {status === 'stopped' && transcription}
                    {status === 'recording' && 'In ascolto ...'}
                </div>
                <AudioInputText postVoiceToServer={postVoiceToServer} />
                {/*<br/>*/}
                {/*<AudioButton onClick={() => { console.log("ciao")}}/>*/}
            </div>
        </>
    );
};

export default RecordView;