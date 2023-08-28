import React from 'react';
import { useEffect, useState } from "react";
import PropTypes from 'prop-types';
import { Container, Button, Modal } from 'react-bootstrap';
import DataTable from 'react-data-table-component';
import 'whatwg-fetch'
import PatientInfo from "./PatientInfo";
import AssignToDoctor from '../Counselor/AssignToDoctor';
import OfferAppoinment from '../Counselor/OfferAppoinment';


export default function PatientList({ userType }) {

    const [originalData, setOriginalData] = useState([]);
    const [rowData, setRowData] = useState([]);
    const [pending, setPending] = useState(true);
    const [columns, setColumns] = useState([]);


    const [showConfirm, setShowConfirm] = useState(false);
    const [selectedUser, setSelectedUser] = useState("");
    const handleCloseConfirm = () => setShowConfirm(false);

    function handleOpenConfirm(patientId) {
        setSelectedUser(patientId);
        setShowConfirm(true);

        //TODO: reset selected user
    }

    async function handleReject() {
        if (selectedUser === "") {
            return;
        }
        // console.log(selectedUser);
        let sendUrl = 'http://localhost:8080/'+userType+'/rejectPatient?patientId='+selectedUser;
        console.log(sendUrl);
        const req = await fetch(sendUrl, {
            method: "POST",
            mode: "cors",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost'
            }
        });
        let response = await req.text();
        console.log(response);
        setShowConfirm(false);
        window.location.reload()

    }

    useEffect(() => {
        if (rowData.length === 0) {
            loadData()
        };
    }, [rowData])

    useEffect(() => {
        const timeout = setTimeout(() => {
            setColumns([
                {
                    name: 'Patient ID',
                    selector: row => row.patientId,
                },
                {
                    name: 'First Name',
                    selector: row => row.firstName,
                },
                {
                    name: 'Last Name',
                    selector: row => row.lastName,
                },
                {
                    name: 'Score',
                    selector: row => row.score,
                },
                {
                    name: 'Last Finish Date',
                    selector: row => row.finishDate,
                },
                {
                    name: 'Status',
                    selector: row => row.status,
                },
                {
                    name: 'Actions',
                    ignoreRowClick: true,
                    allowOverflow: true,

                    cell: (row) => {
                        return (

                            <Container>
                                {
                                    row.status === "" 
                                    ? 
                                        <OfferAppoinment
                                            userType={userType}
                                            patientId={row.patientId}
                                            patinetFirstName={row.firstName}
                                            patinetLastName={row.lastName}
                                            refreshData={loadData}
                                        />
                                    : ""
                                }

                                

                                {
                                    row.status === "" 
                                    &&  userType !== "doctor" 
                                    &&  <AssignToDoctor 
                                            patientId={row.patientId} 
                                            refreshData={loadData}
                                        />
                                }

                                {/* {
                                    row.status === ""
                                    ?
                                        <Button variant="danger" size="sm">
                                            Reject
                                        </Button>
                                    :   ""
                                } */}
                                
                                

                                {row.status === "" &&
                                    <Button variant="danger" size="sm" onClick={() => handleOpenConfirm(row.patientId)}>
                                        Reject
                                    </Button>
                                }

                            </Container>
                        );
                    },
                }


            ]);
            setPending(false);
        }, 1500);
        return () => clearTimeout(timeout);
    }, [])

    let dataBefore = [];
    async function loadData() {
        // console.log("test");
        const response = await fetch('http://localhost:8080/' + userType + '/getPatients', {
            method: "GET",
            mode: "cors",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost'
            }
        });
        const data = await response.json();
        // console.log(data);
        setOriginalData(data);


        if (originalData != null){
            originalData.forEach((item) => {
                dataBefore.push({...item});

            })
        }
        setRowData(dataBefore);

    }

    const ExpandedComponent = ({ data }) => 
        <pre>
            <PatientInfo 
                userType={userType} 
                patientId={data.patientId} 
                loadData={loadData}
                firstName={data.firstName}
                lastName={data.lastName}
                status={data.status}
            />
        </pre>;


    return (
        <>
            <Modal show={showConfirm} onHide={handleCloseConfirm}>
                <Modal.Header closeButton>
                    <Modal.Title>Please confirm rejection</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Button variant="secondary" onClick={handleCloseConfirm}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleReject}>
                        Reject
                    </Button>
                </Modal.Body>
            </Modal>
            <DataTable
                columns={columns}
                data={rowData}
                expandableRows
                expandableRowsComponent={ExpandedComponent}
                pagination
                progressPending={pending}
            />
        </>
    );


}


PatientList.propTypes = {
    userType: PropTypes.string.isRequired
};