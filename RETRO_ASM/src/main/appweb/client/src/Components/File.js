import React, {Component} from 'react';

class File extends Component {

	constructor(props) {
		super(props);


		this.handlePathChange = this.handlePathChange.bind(this);
	}

	handlePathChange(event){
	    this.props.onPathChange(event.target.value);
	}



	render() {

        const inputPath = this.props.path;
		return(

		        <div className="File">
				<label>Input Path:
                    <input type="text" name="Path"
                    id="Path"
                    placeholder=".class/.jar or directory"
                    value={inputPath}
                    onChange={this.handlePathChange} />
                  </label>
				</div>
		);
	}
}

export default File;