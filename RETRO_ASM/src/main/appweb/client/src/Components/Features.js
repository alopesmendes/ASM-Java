import React, {Component} from 'react';
/*import Force from './Force.js';*/

class Features extends Component {

	constructor(props) {
		super(props);
		this.state = {
				info: false,
				selectAll: false,
				lambda: false,
				tryWithResources: false,
				concatenation: false,
				record: false,
				nestMates: false,
		};
		this.handleInputChange = this.handleInputChange.bind(this);
		this.handleClick = this.handleClick.bind(this);
	}

	handleInputChange(event) {
		const target = event.target;
		const value = target.type === "checkbox" ? target.checked : target.value;
		const name = target.name;

		this.setState({
			[name]:value,
		});
	}

	handleClick(event) {
		const selectAll = this.state.selectAll;

		this.setState({
			selectAll: !selectAll,
		});

		this.checkAll();
	}

	checkAll() {
		const selectAll = this.state.selectAll;

		if (!selectAll) {
			this.setState({
				lambda: true,
				tryWithResources: true,
				concatenation: true,
				record: true,
				nestMates: true,
			});
		}
		else {
			this.setState({
				lambda: false,
				tryWithResources: false,
				concatenation: false,
				record: false,
				nestMates: false,
			});
		}
	}

	render() {
		let features;

		/*if (info) {*/
			features =
				<form>
			<label>
			Features:
				</label>

			<br/>

			<button type="button"
				onClick={this.handleClick} >
			{this.state.selectAll ? "Unselect all" : "Select all"}
			</button>

			<br/>

			<input
			name="lambda"
				type="checkbox"
					checked={this.state.lambda}
			onChange={this.handleInputChange} />
			<label>
			Lambda
			</label>

			<br/>

			<input
			name="tryWithResources"
				type="checkbox"
					checked={this.state.tryWithResources}
			onChange={this.handleInputChange} />
			<label>
			Try With Resources
			</label>

			<br/>

			<input
			name="concatenation"
				type="checkbox"
					checked={this.state.concatenation}
			onChange={this.handleInputChange} />
			<label>
			Concatenation
			</label>

			<br/>

			<input
			name="record"
				type="checkbox"
					checked={this.state.record}
			onChange={this.handleInputChange} />
			<label>
			Record
			</label>

			<br/>

			<input
			name="nestMates"
				type="checkbox"
					checked={this.state.nestMates}
			onChange={this.handleInputChange} />
			<label>
			Nest Mates
			</label>
			</form>;
		/*}*/

		/*if (this.state.any) {
			force = <Force />
		}*/

		return(
				<form>
                <div className="Infos">
				<label>
				Option Infos:
				</label>

				<input
				name="info"
					type="checkbox"
						checked={this.state.info}
				onChange={this.handleInputChange} />
				</div>
				<div className="Features">
				{features}
				</div>
                </form>


		);
	}
}


export default Features;