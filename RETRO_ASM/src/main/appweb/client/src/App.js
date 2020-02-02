import React from 'react';
import './App.css';
import './style.css';
import Header from './Components/Header.js';
import Form from './Components/Form.js';
import {BrowserRouter as Router} from 'react-router-dom';

function App() {

  return (
    <div className="App">
        <Header />
        <Router>
        <div className="FormContainer">
         <Form />

         </div>
         </Router>
    </div>
  );
}

export default App;
