import React from 'react';
import PropTypes from 'prop-types';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import Cookies from 'universal-cookie';

export default function MenuBar({ session }) {

    const logout = () => {
        sessionStorage.removeItem('session');
        const cookies = new Cookies();
        cookies.remove('JSESSIONID', { path: '/' });
    }

    return (
        <Navbar bg="light" expand="lg">
            <Container>
                <Navbar.Brand href="/">Moodspace</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="container-fluid">

                        {session.info.role === "PATIENT" &&
                            <>
                            <Nav.Item>
                                <Nav.Link href="/questionnaire">Questionnaire</Nav.Link>
                            </Nav.Item>
                            <Nav.Item>
                            <Nav.Link href="/history">History</Nav.Link>
                            </Nav.Item>
                            </>
                        }

                        {session.info.role === "COUNSELOR" &&
                            <Nav.Item>
                                <Nav.Link href="/getPatients">Patient List</Nav.Link>
                            </Nav.Item>
                        }

                        {session.info.role === "DOCTOR" &&
                            <Nav.Item>
                                <Nav.Link href="/getPatients">Patient List</Nav.Link>
                            </Nav.Item>
                        }

                        {session.info.role === "MANAGER" &&
                            <>
                                <Nav.Item>
                                    <Nav.Link href="/addUser">Add User</Nav.Link>
                                </Nav.Item>

                                <Nav.Item>
                                    <Nav.Link href="/removeUser">Remove User</Nav.Link>
                                </Nav.Item>

                                <Nav.Item>
                                    <Nav.Link href="/statistics">Statistics</Nav.Link>
                                </Nav.Item>
                            </>
                        }

                        {session.info.role !== "MANAGER" &&
                            <>
                                <Nav.Item>
                                    <Nav.Link href="/schedule">My Appoinment</Nav.Link>
                                </Nav.Item>

                                <Nav.Item>
                                    <Nav.Link href="/profile">Profile</Nav.Link>
                                </Nav.Item>
                            </>
                        }

                        {/* Removes the session object from the sessionStorage and 
                        redirects to the homepage, which rerenders the login form */}
                        <Nav.Item className="ms-auto">
                            <Nav.Link href="/" onClick={logout}> Log out </Nav.Link>
                        </Nav.Item>

                    </Nav>
                </Navbar.Collapse>
            </Container>
        </Navbar>
    )
}

MenuBar.propTypes = {
    session: PropTypes.object.isRequired
};