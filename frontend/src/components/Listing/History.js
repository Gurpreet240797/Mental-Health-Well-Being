import React from 'react';
import { useEffect, useState } from "react";
import DataTable from 'react-data-table-component';
import 'whatwg-fetch'
import ResultInfo from "./ResultInfo";



export default function History() {
    const [originalData, setOriginalData] = useState([]);
    const [rowData, setRowData] = useState([]);
    const [pending, setPending] = useState(true);
    const [columns, setColumns] = useState([]);
    useEffect(() => {
        if (rowData.length === 0) {
            loadData()
        };
    }, [rowData])

    useEffect(() => {
        const timeout = setTimeout(() => {
            setColumns([
                {
                    name: 'Question ID',
                    selector: row => row.resultId,
                },
                {
                    name: 'Finish Date',
                    selector: row => row.finishDate,
                }
            ]);
            setPending(false);
        }, 1500);
        return () => clearTimeout(timeout);
    }, [])

    let dataBefore = [];
    async function loadData() {
        // console.log("test");
        let sessionInfo = JSON.parse(sessionStorage.getItem("session"))
        let patientId = sessionInfo.info.id;
        const response = await fetch('http://localhost:8080/patient/getResult/' + patientId, {
            method: "GET",
            mode: "cors",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost'
            }
        });
        const data = await response.json();
        // console.log(data);
        setOriginalData(data);



        if (originalData != null){
            originalData.forEach((item) => {
                dataBefore.push({...item});

            })
        }
        setRowData(dataBefore);
    }

    const ExpandedComponent = ({ data }) =>
        <pre>
            <ResultInfo
                resultId={data.resultId}
            />
        </pre>;

    return (
        <>
            <DataTable
                columns={columns}
                data={rowData}
                expandableRows
                expandableRowsComponent={ExpandedComponent}
                pagination
                progressPending={pending}
            />
        </>
    );
}
