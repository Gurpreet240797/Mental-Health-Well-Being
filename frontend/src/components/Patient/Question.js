import React from 'react';
import 'whatwg-fetch'
import Form from 'react-bootstrap/Form';
import Button from "react-bootstrap/Button";
import './Question.css'



let answerList = [];
let questionAmount;


class Question extends React.Component{
    constructor(props) {
        super(props);
        this.state = {
            qsList: [],
            responseData: [],
            answerList: []
        }

    }

    async componentDidMount(){
        const response = await fetch('http://localhost:8080/patient/questionnaire', {
            method: "GET",
            mode: "cors",
            credentials: 'include',
            headers: {
                'Content-Type': 'application/json',
                'Access-Control-Allow-Origin': 'http://localhost'
            }
        });
        const data = await response.json();
        this.setState({qsList: data})
        // console.log(this.state)
        questionAmount = data.length;
        // console.log(questionAmount);
    }

     handleChange(event){
         let objAnswer = {qid: event.target.name, score: event.target.value};
         let flag = false;

         for (let key in answerList) {
             if (answerList[key].qid === event.target.name){
                 answerList[key].score = event.target.value
                 flag = true;
             }
         }
         if (flag === false){
             answerList.push(objAnswer)
         }
     }

    handleSubmit(event) {
        event.preventDefault();

        // console.log(answerList);
        // console.log(questionAmount);

        if (answerList.length < questionAmount){
            alert("please answer all questions")
        }else{
            fetch('http://localhost:8080/patient/questionnaire/submission', {
                credentials: 'include',
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin': 'http://localhost'
                },
                body: JSON.stringify(answerList)
            }).then(res => res.json())
                .then((data)=>{
                    document.getElementById("top_div").innerHTML = "Your score: <strong>" + data.totalScore + "</strong>";
                    document.getElementById("qs_div").style.display='none';});
        }
    }


    render() {

        return (
            <>
                <center id="top_div">

                </center>

                <div id="qs_div">
                    <Form onSubmit={this.handleSubmit} id='qs'>
                        {this.state.qsList.map((item,index) => (
                            <div key={Math.random()}>
                                <Form.Group className="mb-3">
                                    <Form.Label>{index + 1}. {item.des.split(":")[0]}: <b>{item.des.split(":")[1]}</b></Form.Label>
                                {item.options.map((opt) => (
                                    <div key={opt.optionStr} className="form-check">
                                        <Form.Check
                                        type="radio"
                                        id={opt.optionStr}
                                        label={opt.optionStr}
                                        name={item.qid}
                                        value={opt.score}
                                        onChange={this.handleChange}
                                        />
                                    </div>
                                    ))}
                                    <hr className="split-line"/>
                                </Form.Group>
                            </div>
                            ))}
                        <center>
                            <Button type="submit" >Submit</Button>
                        </center>
                    </Form>
                </div>
        </>
        )
    }
}

export default Question;
