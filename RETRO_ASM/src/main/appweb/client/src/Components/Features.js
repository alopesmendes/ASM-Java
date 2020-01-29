import React, {Component} from 'react';
/*import Force from './Force.js';*/

class Features extends Component {

	constructor(props) {
		super(props);
		this.state = {
				isInfo: false,
				selectAll: true,
				lambda: true,
				tryWithResources: true,
				concatenation: true,
				record: true,
				nestMates: true,
		};
		this.handleInputChange = this.handleInputChange.bind(this);
		this.handleClick = this.handleClick.bind(this);

		this.handleInfoChange = this.handleInfoChange.bind(this);
		this.refreshFeatures = this.refreshFeatures.bind(this);
	}

	handleInfoChange(event) {
    		const target = event.target;
    		const value = target.type === "checkbox" ? target.checked : target.value;
    		const name = target.name;

    		this.setState({
    			[name]:value,
    		});

    		this.props.onInfoChange(event.target.checked);
    	}

    refreshFeatures(name){
        const features = this.state;

        this.props.onAnyFeatureChange(
            features.lambda,
        	features.tryWithResources,
        	features.concatenation,
        	features.record,
        	features.nestMates);
    }

	handleInputChange(event) {
    		const target = event.target;
    		const value = target.type === "checkbox" ? target.checked : target.value;
    		const name = target.name;


    		this.setState((state, props)=>({
    			[name]:value,
    		}));

    		this.refreshFeatures(name);



    	}

	handleClick(event) {
    		const selectAll = this.state.selectAll;

    		this.setState({
    			selectAll: !selectAll,
    		});

    		this.checkAll();
    		this.props.onAllFeaturesChange(selectAll);
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
            <div>
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
			<br/>
			<button type="button"
            	    onClick={this.handleInputChange} >
            	    Valid Features
            </button>
			</div>;
		/*}*/

		/*if (this.state.any) {
			force = <Force />
		}*/

		return(
                <div>
                <div className="Infos">
				<label>Option Infos:</label>

				<input name="isInfo" type="checkbox" id="info"
                value="info"
                checked={this.state.isInfo}
                onChange={this.handleInfoChange} />
				</div>
				<div className="Features">
				{features}
				</div>
				</div>



		);
	}
}


export default Features;