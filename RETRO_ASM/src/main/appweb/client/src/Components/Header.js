import React, {Component} from "react";

class Header extends Component{
    render(){
        return(
            <div className="Header">
            <header>
                                <nav>
                                     <ul>
                                        <li><a href="#">HOME</a></li>
                                        <li><a href="#">JAVA DOC</a></li>
                                        <li><a href="#">HELP</a></li>
                                    </ul>
                                </nav>
                     </header>
            </div>
        );
    }
}

export default Header;