import React, {Component} from 'react';

class File extends Component {

	constructor(props) {
		super(props);
		this.state = {
				fileOrJar: true,
				directory: false,
		};
		this.handleOptionChange = this.handleOptionChange.bind(this);
	}

	handleOptionChange(event) {
		const name = event.target.name;

		if (name === "File/Jar") {
			this.setState({
				fileOrJar: true,
				directory: false,
			});
		}
		else {
			this.setState({
				fileOrJar: false,
				directory: true,
			});
		}
	}

	render() {
		const fileOrJar = this.state.fileOrJar;
		let chooseFile;

		if (fileOrJar) {
			chooseFile = <label>
			Choix du fichier à rétrocompiler:
				<input type="file"
					ref={this.state.file} />
			</label>;
		}
		else {
			chooseFile = <label>
			Choix du dossier à parser:
				<input type="file"
					ref={this.state.file}
			webkitdirectory="" />
				</label>;
		}

		return(
		        <div className="File">
				<form>

				<div>
				<h3>Element en entrée:</h3>
					<label>
				<input
				id="FileJar"
					name="File/Jar"
						type="radio"
							checked={this.state.fileOrJar}
				onChange={this.handleOptionChange} />
				Fichier .class/.jar
				</label>

				<label>
				<input
				id="Directory"
					name="Directory"
						type="radio"
							checked={this.state.directory}
				onChange={this.handleOptionChange} />
				Répertoire
				</label>
				</div>

				{chooseFile}

				</form>
				</div>
		);
	}
}

export default File;