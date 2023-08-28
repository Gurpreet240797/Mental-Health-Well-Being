import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import AddUser from './components/Admin/AddUser';
import Login from "./components/Authentication/Login";
import RegistrationForm from "./components/Authentication/RegistrationForm";
import useSession from './components/Authentication/useSession';
import MenuBar from './components/MenuBar';
import Question from "./components/Patient/Question";
import WelcomePage from './components/Authentication/WelcomePage';
import Homepage from './components/Homepage';
import ManagerList from './components/Admin/ManagerList';
import PatientList from './components/Listing/PatientList';
import Profile from './components/Profile';
import CDSchedule from './components/Schedule/CDSchedule';
import PatientSchedule from './components/Schedule/PatientSchedule';
import Statistics from './components/Admin/Statistics';
import './App.css';
import History from "./components/Listing/History";


function App() {

  const { session, setSession } = useSession();

  if (!session || !session.status) {
    return (
      <Routes>
        <Route exact path='/' element={<WelcomePage></WelcomePage>}></Route>
        <Route exact path='/register' element={<RegistrationForm setSession={setSession} />}></Route>
        <Route exact path='/login' element={<Login setSession={setSession}></Login>}></Route>
        <Route path="*" element={<Navigate replace to="/" />}></Route>
      </Routes>
    );
  }

  return (
    <div className="App">
      <div className="HeaderContent">
        <MenuBar session={session}></MenuBar>
      </div>
      <div className="PageContent">
        <Routes>
          {/* Routes that are allowed depend on the user type */}
          <Route index element={<Homepage session={session} />}></Route>

          {session.info.role !== "MANAGER" &&
            <Route exact path='/profile' element={<Profile />}></Route>
          }

          {session.info.role === "PATIENT" &&
            <>
              <Route exact path='/questionnaire' element={<Question />}></Route>
              <Route exact path='/schedule' element={<PatientSchedule userType='patient'/>}></Route>
              <Route exact path='/history' element={<History />}></Route>
            </>
          }

          {session.info.role === "COUNSELOR" &&
            <>
              <Route exact path='/getPatients' element={<PatientList userType='counselor' />}></Route>
              <Route exact path='/schedule' element={<CDSchedule userType='counselor'/>}></Route>
            </>
          }

          {session.info.role === "DOCTOR" &&
            <>
              <Route exact path='/getPatients' element={<PatientList userType='doctor' />}></Route>
              <Route exact path='/schedule' element={<CDSchedule userType='doctor'/>}></Route>
            </>
          }

          {session.info.role === "MANAGER" &&
            <>
              <Route exact path='/addUser' element={<AddUser />}></Route>
              <Route exact path='/removeUser' element={<ManagerList />}></Route>
              <Route exact path='/statistics' element={<Statistics />}></Route>
            </>
          }

          {/* This route  is here to prevent errors when login redirects */}
          <Route path="*" element={<Navigate replace to="/" />}></Route>

        </Routes>
      </div>
    </div>
  );
}

export default App;
