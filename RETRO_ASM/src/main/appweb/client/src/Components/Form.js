import React, {Component} from 'react';
import Features from './Features.js';
import Target from './Target.js';
import File from './File.js';

class Form extends Component {

	constructor(props) {
		super(props);
		this.state = {
				submit: false,
		};
	}

	render() {

		return(
		        <div className="Form">
		        <h2> Choix des différentes options</h2>
				<form>
				    <Target />
				    <Features />
				    <File />
				    <input type="submit" value="EXECUTER" />
				</form>
				</div>
		);
	}
}

export default Form;