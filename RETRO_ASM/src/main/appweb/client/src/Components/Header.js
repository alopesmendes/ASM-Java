import React, {Component} from "react";

class Header extends Component{
    render(){
        return(
            <div className="Header">
            <header>
                                <nav>
                                     <ul>
                                        <li><a href="/index.html">HOME</a></li>
                                        <li><a href="/index.html">JAVA DOC</a></li>
                                        <li><a href="/Help.html">HELP</a></li>
                                    </ul>
                                </nav>
                     </header>
            </div>
        );
    }
}

export default Header;