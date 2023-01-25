import {useState} from "react";

type props = {
    label: string,
    toggled: boolean,
    onClick: (isToggled: boolean) => void
}

export const Toggle = ({label, toggled, onClick}: props) => {
    const [isToggled, toggle] = useState<boolean>(toggled)

    const callback = () => {
        toggle(oldToggle => !oldToggle)
        onClick(!isToggled)
    }

    return (
        <label>
            <input type="checkbox" defaultChecked={isToggled} onClick={callback}/>
            <span/>
            <strong>{label}</strong>
        </label>
    )
}