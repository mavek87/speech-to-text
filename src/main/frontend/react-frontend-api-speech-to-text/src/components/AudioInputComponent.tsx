import React, {useState} from 'react';
import AudioButton from "./AudioButton/AudioButton";

export type props = {
    children: React.ReactNode

}

const AudioInputComponent = ({children} : props) => {
    const [transcription, setTranscription] = useState("");

    const onClickAudioButtonHandler = () => {

    };

    return (
        <div>
            {children}
            {/*<AudioButton onClick={onClickAudioButtonHandler} />*/}
        </div>
    );
};

export default AudioInputComponent;