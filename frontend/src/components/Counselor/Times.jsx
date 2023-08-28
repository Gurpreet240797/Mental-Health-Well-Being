import React from 'react'
import {useState} from 'react';
import {Button, Alert } from 'react-bootstrap';

export default function Times(props){
    const current = JSON.parse(sessionStorage.getItem("session"))
    const someDate = new Date(props.date)
    const foo = someDate.getFullYear()+"/" + (someDate.getMonth() + 1) +"/"+ someDate.getDate()

    const [offerData, setOfferData] = useState({
        patientId: props.patientId,
        currentId: current.info.id,
        date:foo,
        time:""
    })
    const [info, setInfo] = useState(false)
    const [alert, setAlert] = useState({ 
        show: false,
        message: "" 
    });

    async function handleOffer() {
        const request = {
            method: "POST",
            mode: "cors",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost'
            },
            body: JSON.stringify(
                offerData
            )
        }
        console.log(request.body)

        await fetch('http://localhost:8080/'+props.userType+'/offerAppointment', request)
            .then(response => response.status === 201 
                ? { message: "Successfully offer an Appointment" } 
                : response.json()
            )
            .then(data => setAlert({ show: true, message: data.message }))
            .then(() => setTimeout(() => props.refreshData(), 1500));
            // Time out just for the demo, to showcase the success message
    }

    const displayInfo = (e) => {
        // console.log(e)
        setOfferData(prev => {
            return {
                ...prev,
                date:foo,
                time:e.target.innerText
            }
        })
        setInfo(true);
        console.log(props.timeData)
        // console.log(offerData)
    }

    const offerHandler = () => {
        console.log(offerData)
        handleOffer(offerData)
    }

    const renderTimeSlot = props.timeData.map(time => {
        return (
            <li key={time.id} >
                <button 
                    key={time.id}
                    onClick={(e)=> displayInfo(e)}
                > 
                    {time.time} 
                </button>
            </li>
            )
        })

    return (
        <>
            {alert.show &&
                <Alert variant={
                    alert.message === "Successfully offer an Appointment" 
                    ? "success" 
                    : "danger"}
                > 
                    {alert.message} 
                </Alert>
            }
            <ul className="times">
                {renderTimeSlot}
            </ul>
            <div>
                {
                    info ? (
                        `Patient's appointment is set to ${offerData.time} ${foo}` 
                    ) : null
                }
                {
                    info
                    ? 
                        <div>
                            <Button 
                                variant="primary" 
                                size="sm"
                                onClick={offerHandler}
                            >
                                offer this appointment
                            </Button>
                        </div>
                    :null
                }
            </div>
            
        </>
    )
}