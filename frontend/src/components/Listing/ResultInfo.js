import React, { useEffect, useState } from 'react';
import PropTypes from 'prop-types';
import { Table, Container } from 'react-bootstrap';
import './PatientInfo.css'


export default function ResultInfo({resultId}) {
    const [result, setResult] = useState([]);
    useEffect(()=>{
        if(result.length === 0) {
            loadResultInfo(resultId)
        }
    },[result])

    async function loadResultInfo(resultId) {

        const resResult = await fetch('http://localhost:8080/patient/getQuestionAnswers/' + resultId, {
            method: "GET",
            mode: "cors",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost'
            }
        });
        //const data_user = await resUserInfo.json();
        setResult(await resResult.json());
    }

    return (
        <div>
            <Container>
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

ResultInfo.propTypes = {
    resultId: PropTypes.string.isRequired
  };