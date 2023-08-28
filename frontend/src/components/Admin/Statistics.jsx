import React, { useState, useEffect} from "react";
import { Table } from 'react-bootstrap';


export default function Statistics(){
    
    const [statistics, setStatistics] = useState({})


    useEffect(()=>{
        if(statistics) {
            loadSatistics()
        }
    },[statistics])

    async function loadSatistics() {
        // change the url later
        const res = await fetch(' http://localhost:8080/manager/statistics/', {
            method: "GET",
            mode: "cors",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost'
            }
        });
        setStatistics(await res.json())
    }

    return(
        <div>
            <Table striped bordered hover>
                <thead>
                    <tr>
                        <th>Daily</th>
                        <th>Weekly</th>
                        <th>Monthly</th>
                    </tr>
                </thead>
                <tbody>
                        <tr>
                            <td>{statistics.daily}</td>
                            <td>{statistics.weekly}</td>
                            <td>{statistics.monthly}</td>
                        </tr>
                </tbody>
            </Table>
        </div>
    )
}