import React, {Component} from 'react';
import Force from './Force.js';

class Target extends Component {

	constructor(props) {
		super(props);
		this.state = {
			targetOption: false,
			force: false,
			targetVersion: '5',
		};
		this.handleChange = this.handleChange.bind(this);
		this.handleInputChange = this.handleInputChange.bind(this);
	}

	handleChange(event) {
		this.setState({targetVersion: event.target.targetVersion});
	}

	handleInputChange(event) {
		const target = event.target;
		const value = target.type === "checkbox" ? target.checked : target.value;
		const name = target.name;

		this.setState({
			[name]:value,
		});
	}

	render() {
		const targetOption = this.state.targetOption;
		let versionList;
		let force;

		if(targetOption) {
			versionList = <label>
				Choose the target version:
				<select value={this.state.targetVersion} onChange={this.handleChange}>
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
			</label>;

			force = <Force />;
		}

		return(
            <div className="Target">
			<form>

			<label>
			Option Target:
			</label>
			<input
			name="targetOption"
				type="checkbox"
					checked={this.state.targetOption}
			onChange={this.handleInputChange} />

			<br/>

			{versionList}

			{force}

			</form>
			</div>
		);
	}
}

export default Target;