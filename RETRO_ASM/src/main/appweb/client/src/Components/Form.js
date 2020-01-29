import React, {Component} from 'react';
import axios from 'axios';
import Features from './Features.js';
import Target from './Target.js';
import File from './File.js';


class Form extends Component {

	constructor(props) {
		super(props);
		this.state = {
				submit: false,
				info: "",
                allFeatures: true,
                feature: "",
                features: "",
                force: "",
                target:"",
                version: "",
                path:""

		};
	    this.handleInfoChange = this.handleInfoChange.bind(this);
        this.handleAnyFeatureChange = this.handleAnyFeatureChange.bind(this);
        this.handleAllFeaturesChange = this.handleAllFeaturesChange.bind(this);

        this.handleForceOptionChange = this.handleForceOptionChange.bind(this);
        this.handleTargetOptionChange = this.handleTargetOptionChange.bind(this);
        this.handleTargetVersionChange = this.handleTargetVersionChange.bind(this);

        this.handlePathChange = this.handlePathChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleSubmit(event) {
        event.preventDefault();

        const data = {
          path: this.state.path,
          options: this.state.target +" | "+this.state.version+" | ".state.info+" | "+this.state.features+ " "+ this.state.feature
        };

        axios.post('http://0.0.0.0:8080/retro', { data })
          .then(res => {
            console.log(res);
            console.log(res.data);
          })
      }

	handleInfoChange(info) {
    		if (info) {
    			this.setState({
    				info: "-Infos ",
    			});
    		}
    		else {
    			this.setState({
    				info: " ",
    			});
    		}
    }

    handleAllFeaturesChange(all) {
    		if (!all) {
    			this.setState({
    				feature: " ",
    				features: " ",
    			});
    		}
    		else {
    			this.setState({
    				feature: "-Features ",
    				features: " ",
    			});
    		}
    	}

    handleAnyFeatureChange(lambda, twr, concat, rec, nestMates) {
    		const lambdaTxt = lambda ? "Lambda" : null;
    		const twrTxt = twr ? "TryWithRessources" : null;
    		const concatTxt = concat ? "Concatenation" : null;
    		const recTxt = rec ? "Record" : null;
    		const nestMatesTxt = nestMates ? "Nestmates" : null;
    		if (lambda || twr || concat || rec || nestMates) {
            	this.setState({
            	    feature: "-Features ",
            		features: [lambdaTxt, twrTxt, concatTxt, recTxt, nestMatesTxt]
            			.filter(f => f != null)
            			.join(", ") + " ",
            	});
             }else {
                this.setState({
            	    feature: " ",
            		features: " ",
            	});
             }
    }

    handleForceOptionChange(forceOption) {
    		if (forceOption) {
    			this.setState({
    				force: "--Force ",
    			});
    		}
    		else {
    			this.setState({
    				force: " ",
    			});
    		}
    	}

    	handleTargetVersionChange(targetVersion) {
        		this.setState({
        			version: targetVersion,
        		});
        	}

    handleTargetOptionChange(targetOption) {
    		if (targetOption) {
    			this.setState({
    				target: "-Target ",
    				version: "5 ",
    			});
    		}
    		else {
    			this.setState({
    				target: " ",
    				version: " ",
    				force: " ",
    			});
    		}
    	}

    handlePathChange(value){
        this.setState({
            path:value,
        });
    }

	render() {
        const info = this.state.info;
        const feature = this.state.feature;
        const features = this.state.features;
        const target = this.state.target;
        const force = this.state.force;
        const version = this.state.version;
        const path = this.state.path;
		return(
		        <div className="Form">
		        <h2> Options choice</h2>
				<form>
				    <Target
				        onForceOptionChange={this.handleForceOptionChange}
				        onTargetOptionChange={this.handleTargetOptionChange}
				        onTargetVersionChange={this.handleTargetVersionChange}
				    />
				    <Features
				        onInfoChange={this.handleInfoChange}
                        onAllFeaturesChange={this.handleAllFeaturesChange}
                        onAnyFeatureChange={this.handleAnyFeatureChange}/>
				    <File
				        onPathChange={this.handlePathChange}/>
				    <input type="submit" value="EXECUTE"  onSubmit={this.handleSubmit}/>
				    <br/>
				    {info}
                    {feature}
                    {features}
                    {target}
                    {force}
                    {version}
                    {path}
				</form>
				</div>
		);
	}
}

export default Form;