import React, {Component} from "react";

class Force extends Component{

	constructor(props) {
		super(props);
		this.state = {
				force: false,
		};
		this.handleInputChange = this.handleInputChange.bind(this);
	}

	handleInputChange(event) {
		const target = event.target;
		const value = target.type === "checkbox" ? target.checked : target.value;
		const name = target.name;

		this.setState({
			[name]:value,
		});
	}

	render(){
		return(
				<form>

				<label>
				Force:
				</label>

				<input
				name="force"
					type="checkbox"
						checked={this.state.force}
				onChange={this.handleInputChange} />

				</form>
		);
	}

}

export default Force;