import React, { useState, useEffect } from 'react'
import { Form, Button, Container, Row, Col, Card, Alert } from 'react-bootstrap';
import { Link } from 'react-router-dom'
import useSession from './Authentication/useSession';


export default function Profile() {

    const { session, setSession } = useSession();

    const initialState = {
        address: "",
        phoneNumber: "",
        password: "",
    }

    const [state, setState] = useState(initialState);
    const [submissionDisabled, setSubmissionDisabled] = useState(true);
    const [alertState, setAlertState] = useState({ show: false, message: "", variant: "success" });


    // Form can be submitted if the address or phone number is different from the current one
    // Or if a new password has been entered
    useEffect(() => {
        if (state.address !== ""
            || state.phoneNumber !== ""
            || state.password !== "") {
            setSubmissionDisabled(false);
            return;
        }
        setSubmissionDisabled(true);
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

        const request = {
            method: "POST",
            mode: "cors",
            credentials: 'include',
            headers: { 'Content-Type': 'application/json', 'Access-Control-Allow-Origin': 'http://localhost' },
            body: JSON.stringify(state)
        }

        let response = await fetch("http://localhost:8080/" + session.info.role.toLowerCase() + "/edit", request);
        let responseBody = await response.json();

        if (response.status === 200) {
            let newSession = { status: session.status, id: session.id, info: responseBody };
            setSession(newSession);
            setAlertState({ show: true, message: "Successful modification", variant: "success" })
        } else {
            setAlertState({ show: true, message: "An error occured: " + responseBody.message, variant: "danger" })
        }
        setState(initialState);
    }


    return (
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

                                            <Alert show={alertState.show} variant={alertState.variant} onClose={() => setAlertState({ show: false, message: "", variant: "success" })} dismissible>
                                                <Alert.Heading>{alertState.message}</Alert.Heading>
                                            </Alert>

                                            <Form.Group className="mb-3" controlId="firstNameInput">
                                                <Form.Label>First Name</Form.Label>
                                                <Form.Control type="text" defaultValue={session.info.firstName} disabled />
                                            </Form.Group>

                                            <Form.Group className="mb-3" controlId="lastNameInput">
                                                <Form.Label>Last Name</Form.Label>
                                                <Form.Control type="text" defaultValue={session.info.lastName} disabled />
                                            </Form.Group>

                                            <Form.Group className="mb-3" controlId="dateOfBirthInput">
                                                <Form.Label>Birthdate</Form.Label>
                                                <Form.Control type="text" defaultValue={session.info.dateOfBirth.split("T")[0]} disabled />
                                            </Form.Group>

                                            <Form.Group className="mb-3" controlId="addressInput">
                                                <Form.Label>Address (Current:  {session.info.address})</Form.Label>
                                                <Form.Control type="text" value={state.address} name="address" onChange={handleChange} />
                                            </Form.Group>

                                            <Form.Group className="mb-3" controlId="phoneNumberInput" pattern="^-?[0-9]\d*\.?\d*$">
                                                <Form.Label>Phone Number (Current:  {session.info.phoneNumber})</Form.Label>
                                                <Form.Control type="tel" value={state.phoneNumber} name="phoneNumber" onChange={handlePhoneNumberInput} />
                                            </Form.Group>

                                            <Form.Group className="mb-3" controlId="emailInput">
                                                <Form.Label>Email</Form.Label>
                                                <Form.Control type="text" defaultValue={session.info.email} disabled />
                                            </Form.Group>

                                            <Form.Group className="mb-3" controlId="passwordInput">
                                                <Form.Label>Password</Form.Label>
                                                <Form.Control type="password" value={state.password} name="password" onChange={handleChange} placeholder="Please enter your new password here" />
                                            </Form.Group>

                                            {/* Only display this field if the user is not a patient */}
                                            {(session.info.role === "COUNSELOR" || session.info.role === "DOCTOR") &&
                                                <Form.Group className="mb-3" controlId="registrationNumberInput">
                                                    <Form.Label>Registration Number</Form.Label>
                                                    <Form.Control type="text" defaultValue={session.info.registrationNumber} disabled />
                                                </Form.Group>
                                            }

                                            <Button variant="primary" type="submit" disabled={submissionDisabled}>Update information</Button>

                                            <hr />
                                            <div className="text-center pt-2">Go back to <Link className="text-link" to="/">homepage</Link></div>
                                        </Form>

                                    </div>
                                </div>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}
