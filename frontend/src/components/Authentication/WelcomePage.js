import React, { useState } from 'react'
import { Navigate } from "react-router-dom";
import { Button, Container, Row, Col, Card } from 'react-bootstrap';

function WelcomePage() {

    const [navigate, setNavigate]= useState({login: false, register: false});

    if (navigate.login) {
        return <Navigate to="/login"/>
    }
    if (navigate.register) {
        return <Navigate to="/register"/>
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
                                    <div className="d-grid gap-2">

                                        <h1 style={{ textAlign: 'center' }}> Moodspace </h1>

                                        <Button variant="primary" size="lg" onClick={() => setNavigate({login: true, register: false})}> Login </Button>
                                        <Button variant="secondary" size="lg" onClick={() => setNavigate({login: false, register: true})}> Register </Button>

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

export default WelcomePage
