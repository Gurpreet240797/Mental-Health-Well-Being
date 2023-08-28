import React, { useState } from 'react'
import PropTypes from 'prop-types';
import { Modal, Button, Table, Alert } from 'react-bootstrap';
import { useEffect } from 'react';

export default function AssignToDoctor({ patientId, refreshData }) {

    const [doctors, setDoctors] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [alert, setAlert] = useState({ show: false, message: "" });

    const clearModalState = () => {
        setShowModal(false);
        setAlert({ show: false, message: "" });
    }

    async function getDoctorsList() {

        const request = {
            method: "GET",
            mode: "cors",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost'
            }
        }

        const response = await fetch('http://localhost:8080/counselor/getAllDoctors', request);
        const doctors = await response.json();
        setDoctors(doctors);
    }

    useEffect(() => {
        if (doctors.length === 0) {
            getDoctorsList();
        };
    }, []);

    async function handleAssign(doctorId) {

        const request = {
            method: "POST",
            mode: "cors",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost'
            },
            body: JSON.stringify({
                patientId: patientId,
                doctorId: doctorId
            })
        }

        await fetch('http://localhost:8080/counselor/assignPatientToDoctor', request)
            .then(response => response.status === 201 ? { message: "Successfully assigned to doctor" } : response.json())
            .then(data => setAlert({ show: true, message: data.message }))
            .then(() => setTimeout(() => refreshData(), 1500));
            // Time out just for the demo, to showcase the success message
    }


    return (

        <>
            <Button variant="secondary" size="sm" type="button" onClick={() => setShowModal(true)}>Assign</Button>

            <Modal show={showModal} onHide={clearModalState}>
                <Modal.Header closeButton>
                    <Modal.Title>Assign to doctor</Modal.Title>
                </Modal.Header>

                <Modal.Body>

                    {alert.show &&
                        <Alert variant={
                            alert.message === "Successfully assigned to doctor" 
                            ? "success" 
                            : "danger"}
                        > 
                            {alert.message} 
                        </Alert>
                    }

                    {!alert.show &&
                        <Table striped bordered hover>
                            <thead>
                                <tr>
                                    <th>First Name</th>
                                    <th>Last Name</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                {Object.values(doctors)
                                    .map(doctor =>
                                        <tr key={doctor.id}>
                                            <td> {doctor.firstName} </td>
                                            <td> {doctor.lastName} </td>
                                            <td>
                                                <Button variant="primary" type="button" onClick={() => handleAssign(doctor.id)}>Assign</Button>
                                            </td>
                                        </tr>

                                    )}
                            </tbody>
                        </Table>
                    }

                </Modal.Body>
            </Modal>
        </>

    )
}


AssignToDoctor.propTypes = {
    patientId: PropTypes.string.isRequired,
    refreshData: PropTypes.func.isRequired
};
