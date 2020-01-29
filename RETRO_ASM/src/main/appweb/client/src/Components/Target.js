import React, {Component} from 'react';
import Force from './Force.js';

class Target extends Component {

	constructor(props) {
		super(props);
		this.state = {
			targetOption: false,
			force: false,
			//targetVersion: '5',
		};
		this.handleChange = this.handleChange.bind(this);
		this.handleInputChange = this.handleInputChange.bind(this);
		this.handleForceChange = this.handleForceChange.bind(this);
	}

	handleChange(event) {
		this.props.onTargetVersionChange(event.target.value);
	}

	handleInputChange(event) {
		const target = event.target;
		const value = target.type === "checkbox" ? target.checked : target.value;
		const name = target.name;

		this.setState({
			[name]:value,
		});

		if (name === "targetOption") {
        	this.props.onTargetOptionChange(value);
        }
	}

	handleForceChange(forceOption) {
    		this.props.onForceOptionChange(forceOption);
    	}

	render() {
		const targetOption = this.state.targetOption;
		let details;

        //const forceOption = this.props.forceOption;
        const version = this.props.version;

		if(targetOption) {
			details = <div className="target-details">

             <label>
             Choose the target version:
             <select value={version} onChange={this.handleChange}>
             <option value='5'>5</option>
             <option value='6'>6</option>
             <option value='7'>7</option>
             <option value='8'>8</option>
             <option value='9'>9</option>
             <option value='10'>10</option>
             <option value='11'>11</option>
             <option value='12'>12</option>
             <option value='13'>13</option>
             </select>
             </label>
             <br /><br/>

             <Force
             onForceChange={this.handleForceChange}
             />
             </div>
		}

		return(
            <div className="Target">

			<label>Option Target:</label>
			<input
			name="targetOption"
				type="checkbox"
				id="targetOption"
                checked={this.state.targetOption}
                onChange={this.handleInputChange} />

			<br/>

			{details}



			</div>
		);
	}
}

export default Target;