import React, { useEffect } from 'react';
import { useState } from 'react';
import { Form, Button, Container, Row, Col, Card, Alert } from 'react-bootstrap';
import '../../App.css';


export default function AddUser() {

    const initialState = {
        role: "2",
        firstName: "",
        lastName: "",
        dateOfBirth: "",
        address: "",
        phoneNumber: "",
        email: "",
        password: "",
        registrationNumber: ""
    }

    const [state, setState] = useState(initialState);
    const [submissionDisabled, setSubmissionDisabled] = useState(true);
    const [submissionStatus, setSubmissionStatus] = useState(0);
    const [alertState, setAlertState] = useState({ show: false, message: "" });

    useEffect(() => {
        // Check that the form can be submitted and disable the submit button if needed
        for (let key in state) {
            // Only doctors and counselors need a registration ID
            if ((key === "registrationNumber" && state.role !== "2") || (key !== "registrationNumber")) {
                if (state[key] === "") {
                    setSubmissionDisabled(true);
                    return
                }
            }
        }
        setSubmissionDisabled(false);
    }, [state]);

    function handleChange(event) {
        setState({
            ...state, // Keeping the rest of the state (that did not receive updates)
            [event.target.name]: event.target.value   // Updating the relevant state field
        });
    }

    function handlePhoneNumberInput(event) {
        const re = /^[0-9\b]+$/;    // Only update the state if the input is actual numbers
        if (event.target.value === '' || re.test(event.target.value)) {
            setState({
                ...state,
                [event.target.name]: event.target.value
            })
        };
    }

    async function handleSubmit(event) {
        event.preventDefault();

        // The HTML form will not allow int in this context ; trick to convert the sent data role value to int
        let sendableState = state;
        sendableState["role"] = parseInt(sendableState["role"]);

        const request = {
            method: "POST",
            mode: "cors",
            credentials: 'include',
            headers: { 
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost' 
            },
            body: JSON.stringify(sendableState)
        };

        await fetch("http://localhost:8080/manager/adduser", request)
            .then(response => {
                if (response.status === 201) {
                    setSubmissionStatus(1);
                    return { message: "New user successfully added." };
                } else if (response.status === 403) {
                    setSubmissionStatus(2);
                    return response.json();
                } else {
                    setSubmissionStatus(3)
                    return { message: "Something went wrong." };
                }
            })
            .then(data => setAlertState({ show: true, message: data.message }));   // Need to wait for the promise to be fulfilled to have access to the error message
        setState(initialState);
    }
    return (

        <div>
            <h1>Add User</h1>
            <Alert variant="info">
                Use this tool to add a user to the system.
            </Alert>

            <div>
                <Container>
                    <Row className="vh-100 d-flex justify-content-center align-items-center">
                        <Col md={8} lg={6} xs={12}>
                            <div className="border border-3 border-primary"></div>
                            <Card className="shadow">
                                <Card.Body>
                                    <div className="mb-3 mt-md-4">
                                        <div className="mb-3">

                                            <Form onSubmit={handleSubmit}>

                                                {submissionStatus !== 0 && alertState.show &&
                                                    <Alert variant={submissionStatus === 1 ? "success" : (submissionStatus === 2 ? "warning" : "danger")} onClose={() => setAlertState({ show: false, message: "" })} dismissible>
                                                        <Alert.Heading>{submissionStatus === 1 ? "Successful registration" : (submissionStatus === 2 ? "Registration failed" : "An error occured")}</Alert.Heading>
                                                        <p id="alertTextContent">{alertState.message}</p>
                                                    </Alert>
                                                }

                                                <Form.Group className="mb-3">
                                                    <Form.Text>Please note that all fields are mandatory.</Form.Text>
                                                </Form.Group>

                                                <Form.Group className="mb-3" controlId="roleSelect">
                                                    <Form.Label>User Type*</Form.Label>
                                                    <Form.Select aria-label="User type selection" value={state.role} name="role" onChange={handleChange}>
                                                        <option value={"2"}>Patient</option>
                                                        <option value={"4"}>Counselor</option>
                                                        <option value={"3"}>Doctor</option>
                                                    </Form.Select>
                                                </Form.Group>

                                                <Form.Group className="mb-3" controlId="firstNameInput">
                                                    <Form.Label>First Name*</Form.Label>
                                                    <Form.Control type="text" value={state.firstName} name="firstName" onChange={handleChange} placeholder="Enter first name here" />
                                                </Form.Group>

                                                <Form.Group className="mb-3" controlId="lastNameInput">
                                                    <Form.Label>Last Name*</Form.Label>
                                                    <Form.Control type="text" value={state.lastName} name="lastName" onChange={handleChange} placeholder="Enter last name here" />
                                                </Form.Group>

                                                <Form.Group className="mb-3" controlId="dateOfBirthInput">
                                                    <Form.Label>Birthdate*</Form.Label>
                                                    <Form.Control type="date" value={state.dateOfBirth} name="dateOfBirth" onChange={handleChange} placeholder="Please select birthdate" />
                                                </Form.Group>

                                                <Form.Group className="mb-3" controlId="addressInput">
                                                    <Form.Label>Address*</Form.Label>
                                                    <Form.Control type="text" value={state.address} name="address" onChange={handleChange} placeholder="Please enter address here" />
                                                </Form.Group>

                                                <Form.Group className="mb-3" controlId="phoneNumberInput" pattern="^-?[0-9]\d*\.?\d*$">
                                                    <Form.Label>Phone Number*</Form.Label>
                                                    <Form.Control type="tel" value={state.phoneNumber} name="phoneNumber" onChange={handlePhoneNumberInput} placeholder="Please enter phone number here" />
                                                </Form.Group>

                                                <Form.Group className="mb-3" controlId="emailInput">
                                                    <Form.Label>Email*</Form.Label>
                                                    <Form.Control type="email" value={state.email} name="email" onChange={handleChange} placeholder="Please enter phone number here" />
                                                </Form.Group>

                                                <Form.Group className="mb-3" controlId="passwordInput">
                                                    <Form.Label>Password*</Form.Label>
                                                    <Form.Control type="password" value={state.password} name="password" onChange={handleChange} />
                                                </Form.Group>

                                                {/* Only display this field if the user is not a new patient */}
                                                {state.role !== "2" &&
                                                    <Form.Group className="mb-3" controlId="registrationNumberInput">
                                                        <Form.Label>Registration Number*</Form.Label>
                                                        <Form.Control type="number" value={state.registrationNumber} name="registrationNumber" onChange={handleChange} placeholder="Please enter registration number here" />
                                                    </Form.Group>
                                                }

                                                <Button variant="primary" type="submit" disabled={submissionDisabled}>Add user</Button>
                                            </Form>

                                        </div>
                                    </div>
                                </Card.Body>
                            </Card>
                        </Col>
                    </Row>
                </Container>
            </div>
        </div>
    )
}
