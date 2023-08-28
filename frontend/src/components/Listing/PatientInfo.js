import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { Table, Container } from 'react-bootstrap';
import './PatientInfo.css'


export default function PatientInfo({ userType, patientId, loadData, firstName, lastName, status}) {
    const [userInfo, setUserInfo] = useState([]);
    const [result, setResult] = useState([]);
    useEffect(()=>{
        if(result.length === 0) {
            loadUserInfo(userType, patientId)
        }
    },[result])

    async function loadUserInfo(type, id) {
        const resUserInfo = await fetch(' http://localhost:8080/' + type + '/getPatient/'+ id, {
            method: "GET",
            mode: "cors",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost'
            }
        });
        const resResult = await fetch('http://localhost:8080/' + type + '/getPatient/'+ id +'/questionnaire', {
            method: "GET",
            mode: "cors",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost'
            }
        });
        //const data_user = await resUserInfo.json();
        setUserInfo(await resUserInfo.json());
        setResult(await resResult.json());
    }

    return (
        <div>
        <Container>
            <h5>Patient Information</h5>            
            <Table striped bordered hover responsive="sm" className="wrap">
                <thead>
                <tr>
                    <th>Item</th>
                    <th>Details</th>
                </tr>
                </thead>
                <tbody>
                    <>
                    <tr>
                        <td>Email</td>
                        <td>{userInfo.firstName}</td>
                    </tr>
                        <tr>
                            <td>Address</td>
                            <td>{userInfo.address}</td>
                        </tr>
                        <tr>
                            <td>Phone Number</td>
                            <td>{userInfo.phoneNumber}</td>
                        </tr>
                        <tr>
                            <td>Date of Birth</td>
                            <td>{userInfo.dateOfBirth}</td>
                        </tr>
                    </>
                </tbody>
            </Table>

                    <h5>Results</h5>

            <Table striped bordered hover responsive="sm" className="wrap">
                    <thead>
                    <tr>
                        <th>Question</th>
                        <th>Answer</th>
                    </tr>
                    </thead>
                    <tbody>
                        {result.map((item,index) => (
                            <tr key={index}>
                                <td>{item.question}</td>
                                <td>{item.answer}</td>
                            </tr>

                        ))}
                    </tbody>
            </Table>
        </Container>
        </div>
    )
}

PatientInfo.propTypes = {
    userType: PropTypes.string.isRequired,
    patientId: PropTypes.string.isRequired
  };