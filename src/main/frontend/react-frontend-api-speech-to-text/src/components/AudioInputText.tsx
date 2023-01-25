import React, {useState} from 'react';
import {useReactMediaRecorder} from "react-media-recorder";
import AudioButton from "./AudioButton/AudioButton";

export type props = {
    postVoiceToServer: (blob: Blob) => Promise<string | void>
}

const AudioInputText = ({postVoiceToServer}: props) => {
    const [text, setText] = useState("");

    const onManualChangeHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
        setText(e.target.value);
    }

    const onStopRecording = async (blobUrl: string, blob: Blob) => {
        const oldText = text;
        setText("Elaborazione trascrizione in corso ...")
        console.log(blob);
        const data = await postVoiceToServer(blob);
        console.log(data);
        if (data) {
            setText(data);
        } else {
            setText(oldText)
        }
    }

    const {status, startRecording, stopRecording, mediaBlobUrl} = useReactMediaRecorder({
            audio: true,
            blobPropertyBag: {type: 'audio/mpeg'},
            onStop: onStopRecording,
        }
    );

    return (
        <>
            <input type="text" onChange={onManualChangeHandler} value={text}/>
            <AudioButton onClickEnable={startRecording} onClickDisable={stopRecording} />
        </>
    );
}
export default AudioInputText;