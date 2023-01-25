import {useState} from "react";
import styles from './AudioButton.module.css';
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faMicrophone, faMicrophoneSlash} from "@fortawesome/free-solid-svg-icons";

export type props = {
    buttonText?: string,
    onClickEnable: () => void
    onClickDisable: () => void
}

const startAudioIcon = <FontAwesomeIcon icon={faMicrophone}/>;
const stopAudioIcon = <FontAwesomeIcon icon={faMicrophoneSlash}/>;

const AudioButton = ({buttonText, onClickEnable, onClickDisable}: props) => {
    const [isEnabled, setEnable] = useState<boolean>(true);

    const callback = () => {
        setEnable(oldIsEnabled => !oldIsEnabled);
        if (isEnabled) onClickEnable();
        else onClickDisable();
    }

    const audioButtonClass = isEnabled ? styles.disabled_audio_button : styles.enabled_audio_button;

    return (
        <button className={audioButtonClass}
                onClick={callback}>{buttonText ? buttonText + " " : ""}{isEnabled ? startAudioIcon : stopAudioIcon}</button>
    )
}

export default AudioButton;