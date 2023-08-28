import React,{useState, useEffect} from "react";
import { Table } from 'react-bootstrap';

export default function CDSchedule({userType}){
    const currentID = JSON.parse(sessionStorage.getItem("session"))

    const [userID, setUserID] = useState({
        currentId: currentID.info.id,
    })

    const [appoinmentInfo, setAppoinmentInfo] = useState([]);
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
        setAppoinmentInfo(await res.json())
        // console.log(appoinmentInfo.length)
        // console.log(appoinmentInfo.size())
    }

    return(
        <div>{
            appoinmentInfo.length === 0 
            ? <h1>you don't have appoinment yet</h1>
            :
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Time</th>
                        </tr>
                    </thead>
                    <tbody>
                        {userType != "patient" && appoinmentInfo.map((item, index) => (
                            <tr key={index}>
                                <td>{item.patientName}</td>
                                <td>{item.time}</td>
                            </tr>
                        ))}
                    </tbody>
                </Table> 
            }
                       
        </div>
    )
}