import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faCircleLeft, faPenToSquare, faTrashCan} from "@fortawesome/free-regular-svg-icons";
import {faMicrophone} from "@fortawesome/free-solid-svg-icons";
// import {
//     IconLookup,
//     IconDefinition,
//     findIconDefinition, icon
// } from '@fortawesome/fontawesome-svg-core'

// export type IconPrefix = "fas" | "far" | "fal" | "fat" | "fad" | "fab" | "fak" | "fass" ;
//
// const coffeeLookup: IconLookup = { prefix: 'fas', iconName: 'coffee-pot' };
// const coffeeIconDefinition: IconDefinition = findIconDefinition(coffeeLookup)

export const trashCanIcon = <FontAwesomeIcon icon={faTrashCan}/>
export const microphone = <FontAwesomeIcon icon={faMicrophone}/>
export const penToSquareIcon = <FontAwesomeIcon icon={faPenToSquare}/>
export const circleLeftIcon = <FontAwesomeIcon icon={faCircleLeft}/>
export const circleLeftIconSizeXS = <FontAwesomeIcon icon={faCircleLeft} size={"xs"}/>
export const circleLeftIconSizeLG = <FontAwesomeIcon icon={faCircleLeft} size={"lg"}/>