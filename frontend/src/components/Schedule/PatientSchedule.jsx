import React,{useState, useEffect} from "react";
import { Table } from 'react-bootstrap';

export default function PatientSchedule({userType}) {
    const currentID = JSON.parse(sessionStorage.getItem("session"))

    const [userID, setUserID] = useState({
        currentId: currentID.info.id,
    })

    const [appoinmentInfo, setAppoinmentInfo] = useState({});    

    useEffect(()=>{
        if(appoinmentInfo) {
            loadAppoinment(userType, userID.currentId)
        }
    },[])

    async function loadAppoinment(type, id) {
        // change the url later
        const res = await fetch(' http://localhost:8080/' + type + '/schedule/'+ id, {
            method: "GET",
            mode: "cors",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost'
            }
        });
        const data = await res.json()
        setAppoinmentInfo(data)
    }

    return(
        <div>      
            {appoinmentInfo.id === undefined 
            ? <h1>You don't have appoinment yet </h1>
            : 
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Time</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr key={appoinmentInfo.id}>
                            <td>
                                {
                                    appoinmentInfo.counselorName === "" 
                                    ? appoinmentInfo.doctorName 
                                    : appoinmentInfo.counselorName
                                }
                            </td>
                            <td>
                                {appoinmentInfo.time}
                            </td>
                        </tr>
                    </tbody>
                </Table>
            }
            
        </div>
    )
}