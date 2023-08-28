import React from 'react'
import PropTypes from 'prop-types';
import { Navigate } from 'react-router-dom';

export default function Homepage({ session }) {

  // When logging in, this page is rendered and immediately redirect to the correct page depending on the user role
  const getRedirectPath = () => {
    let redirectPath = "/profile";
    switch (session.info.role) {
      case "PATIENT":
        redirectPath = "/questionnaire";
        break;
      case "COUNSELOR":
        //TODO: add correct redirect for counselor
        redirectPath = "/getPatients";
        break;
      case "DOCTOR":
        //TODO: add correct redirect for doctor
        redirectPath = "/getPatients";
        break;
      case "MANAGER":
        redirectPath = "/removeUser";
        break;
      default:
        redirectPath = "/profile";
    }
    return redirectPath;
  };

  return (<Navigate replace to={getRedirectPath()} />)
}


Homepage.propTypes = {
  session: PropTypes.object.isRequired
};
