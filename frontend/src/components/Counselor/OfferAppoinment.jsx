
import React, { useState } from "react";
import Calendar from 'react-calendar';
import Timer from "./Timer";
import {Button, Modal, Alert } from 'react-bootstrap';

export default function OfferAppoinment ({ patientId, refreshData, patinetFirstName, patinetLastName, userType}){
    const current = JSON.parse(sessionStorage.getItem("session"))

    const [offerData, setOfferData] = useState({
        currentId: current.info.id,
    })
    const [alert, setAlert] = useState({ 
        show: false,
        message: "" 
    });
    const [showModal, setShowModal] = useState(false);
    const [date, setDate] = useState(new Date());
    const [showTime, setShowTime] = useState(false) 
    const [timeData, setTimeData] = useState([])

    const clearModalState = () => {
        setShowModal(false);
        setAlert({ show: false, message: "" });
    }

    const clickOfferHanlder = () => {
        setShowModal(true)
    }

    async function getAllAvailabelDataOnDate(senddata) {
        // to do -> change the URL
        const response = await fetch('http://localhost:8080/'+userType+'/getAvailableTime', {
            method: "POST",
            mode: "cors",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost'
            },
            body: senddata
        });
        
        const data = await response.json();
        setTimeData(data)
    }

    const onClickDayHandlder = (e) => {
        const someDate = new Date(e)
        const foo = someDate.getFullYear()+"/" + (someDate.getMonth() + 1) +"/"+ someDate.getDate()
        const data = JSON.stringify(
            {
                currentId:offerData.currentId,
                date:foo
            })
        setShowTime(true)
        getAllAvailabelDataOnDate(data)
    }

    const tileDisabled = ({ activeStartDate, date, view }) => {
        return date < new Date()
     }

    return (
        <>
            <Button variant="primary" size="sm" onClick={clickOfferHanlder}>
                Schedule
            </Button>
            
            <Modal show={showModal} onHide={clearModalState}>
                <Modal.Header closeButton>
                    <Modal.Title> Offer an appointment to  
                        {" "+patinetFirstName} 
                        {" "+patinetLastName}
                    </Modal.Title>
                </Modal.Header>

                <Modal.Body>

                    {alert.show &&
                        <Alert variant={
                            alert.message === "Successfully offer an Appointment" 
                            ? "success" 
                            : "danger"}
                        > 
                            {alert.message} 
                        </Alert>
                    }
                    {!alert.show && 
                        <Calendar
                            onChange={setDate}
                            value={date}
                            tileDisabled={tileDisabled}
                            onClickDay={onClickDayHandlder}
                        />
                    }
                    {
                        date === ""
                        ? (
                            <p className='text-center'>
                                Please select a day
                            </p>
                        ) : (
                            ""
                        )
                    }
                    <Timer 
                        showTime={showTime} 
                        date={date}
                        patientId={patientId}
                        refreshData={refreshData}
                        userType={userType}
                        timeData={timeData}
                    />
                </Modal.Body>
            </Modal>
        </>
    )
}