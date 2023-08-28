import React from "react";
import Times from './Times'

export default function Timer(props){

    return (
        <>
            <div>
                {
                    props.showTime 
                    ?   <Times 
                            date={props.date}
                            patientId={props.patientId}
                            refreshData={props.refreshData}
                            userType={props.userType}
                            timeData={props.timeData}
                        /> 
                    : null
                }
            </div>
        </>
    )
}