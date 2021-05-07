import React from "react";
import { AiOutlineArrowLeft } from "react-icons/ai";

export default function BackButton(props) {
    return <button onClick={props.backPath} className="movie-back-button"><AiOutlineArrowLeft/>Go back</button>
}