import React, { useState, useEffect } from 'react'
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom'
import { Form, Button, Container, Row, Col, Card, Alert } from 'react-bootstrap';
import { submitLogin } from './submitLogin';


export default function Login({ setSession }) {

  const [state, setState] = useState({ email: "", password: "" });
  const [submissionDisabled, setSubmissionDisabled] = useState(true);
  const [showAlert, setShowAlert] = useState(false);

  function handleChange(event) {
    setState({
      ...state, // Keeping the rest of the state (that did not receive updates)
      [event.target.name]: event.target.value   // Updating the relevant state field
    });
  }

  useEffect(() => {
    // Check that the form can be submitted and disable the submit button if needed
    // TODO: alert if email invalid?
    if (state.email === "" || state.password === "" || !state.email.includes("@")) {
      setSubmissionDisabled(true);
      return
    }
    setSubmissionDisabled(false);
  }, [state]);


  async function handleSubmit(event) {
    event.preventDefault();
    await submitLogin(state).then(session => session.status ? setSession(session) : setShowAlert(true));
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

                      {showAlert &&
                        <Alert variant="danger" onClose={() => setShowAlert(false)} dismissible> Wrong credentials </Alert>
                      }

                      <Form.Group className="mb-3" controlId="emailInput">
                        <Form.Label className="text-center">Email address</Form.Label>
                        <Form.Control type="email" value={state.email} name="email" onChange={handleChange} placeholder="Enter a valid email address" />
                      </Form.Group>

                      <Form.Group className="mb-3" controlId="passwordInput">
                        <Form.Label>Password</Form.Label>
                        <Form.Control type="password" value={state.password} name="password" onChange={handleChange} placeholder="Enter your password" />
                      </Form.Group>

                      <div className="d-grid">
                        <Button variant="primary" type="submit" disabled={submissionDisabled}>Login</Button>
                      </div>

                    </Form>

                    <hr />
                    <div className="auth-option text-center pt-2">No Account? <Link className="text-link" to="/register" >Sign up</Link></div>

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

Login.propTypes = {
  setSession: PropTypes.func.isRequired
};
