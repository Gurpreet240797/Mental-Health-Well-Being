import React from "react"
import { useState } from 'react';
import { Table, Button, Modal } from 'react-bootstrap';
import { useEffect } from "react";


export default function ManagerList() {

    const getUsers = async () => await fetch('http://localhost:8080/manager/fetch_all_users', {
        method: "GET",
        mode: "cors",
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            'Access-Control-Allow-Origin': 'http://localhost'
        }
    }).then(response => response.json());

    const [users, setUsers] = useState([]);
    const [showConfirm, setShowConfirm] = useState(false);
    const [selectedUser, setSelectedUser] = useState("");

    const handleCloseConfirm = () => setShowConfirm(false);

    function handleOpenConfirm(email) {
        setSelectedUser(email);
        setShowConfirm(true);

        //TODO: reset selected user
    }

    // Update the table on the first render
    useEffect(() => {
        async function updateList() {
            let newList = await getUsers();
            setUsers(newList);
        }
        updateList();
    }, []);


    async function handleRemove() {
        if (selectedUser === "") {
            return;
        }
        await fetch("http://localhost:8080/manager/delete?email_address=" + selectedUser, {
            method: "DELETE",
            mode: "cors",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost'
            }
        });
        let updatedList = await getUsers();
        setUsers(updatedList);
        setShowConfirm(false);
    }

    return (

        <>
            <Modal show={showConfirm} onHide={handleCloseConfirm}>
                <Modal.Header closeButton>
                    <Modal.Title>Please Confirm removal</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Button variant="secondary" onClick={handleCloseConfirm}>
                        Cancel
                    </Button>
                    <Button variant="danger" onClick={handleRemove}>
                        Remove
                    </Button>
                </Modal.Body>
            </Modal>
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Email</th>
                        <th>Role</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    {Object.values(users)
                        .filter(user => user.role !== "MANAGER")
                        .map(user =>
                            <tr key={user.email}>
                                <td> {user.firstName} </td>
                                <td> {user.lastName} </td>
                                <td> {user.email} </td>
                                <td> {user.role} </td>
                                <td>
                                    <Button variant="danger" type="button" onClick={() => handleOpenConfirm(user.email)}>Remove</Button>
                                </td>
                            </tr>

                        )}
                </tbody>
            </Table>
        </>
    );
}
