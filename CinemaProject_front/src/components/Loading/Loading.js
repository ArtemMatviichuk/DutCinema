import React, { Component } from "react";
import './Loading.css'

export default class Loading extends Component {
    render (){
        return (
            <div className="loading__item">
                <div className="lds-spinner"><div>
                    </div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                    <div></div>
                </div>
                <p id="loading__item">Loading</p>
            </div>
        )
    }
}   