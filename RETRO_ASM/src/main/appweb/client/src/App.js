import React from 'react';
import './App.css';
import './style.css';
import Header from './Components/Header.js';
import Footer from './Components/Footer.js';
import Form from './Components/Form.js';

function App() {

  return (
    <div className="App">
        <Header />
        <div className="FormContainer">
         <Form />
         </div>
    </div>
  );
}

export default App;
