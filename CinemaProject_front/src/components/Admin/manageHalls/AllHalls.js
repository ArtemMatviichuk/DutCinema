import React from "react";
import BackButton from "../../backButton/BackButton";
import Loading from "../../Loading/Loading";
import AccountService from "../../../services/AccountService";
import HallService from "../../../services/HallService";
import './style.css'

class AllHalls extends React.Component {

    constructor(props) {
        super(props);
        this.state ={
            isLoaded: false,
            activeHalls:{
                id:Number,
                name: String,
                rowsAmount: Number,
                places: Number
            },
            blockedHalls:{
                id:Number,
                name: String,
                rowsAmount: Number,
                places: Number
            },
        }
    }
    block = async(event)=> {
        event.preventDefault();
        await HallService.blockHall(event.target.id).then((response)=>{
            if(response.ok){
                this.componentDidMount();
            }
        });
        await HallService.getAllActive()
            .then((result) => {
                this.setState({activeHalls: result});
                console.log(result)
            });
        await HallService.getBlocked()
            .then((result) => {
                this.setState({blockedHalls: result});
                console.log(result)
            });
    };
    unblock = async(event)=> {
        event.preventDefault();
        await HallService.unblockHall(event.target.id).then((response)=>{
            if(response.ok){
                this.componentDidMount();
            }
        });
        await HallService.getAllActive()
            .then((result) => {
                this.setState({activeHalls: result});
                console.log(result)
            });
        await HallService.getBlocked()
            .then((result) => {
                this.setState({blockedHalls: result});
                console.log(result)
            });

    };

    componentDidMount =async()=>{
         await AccountService.isAdmin()
            .then(() =>{
                this.setState({
                    isLoaded: true
                })
            })
            .catch(() => window.location.replace('/'));
        await HallService.getAllActive()
            .then((result) => {
                this.setState({activeHalls: result});
            });
        await HallService.getBlocked()
            .then((result) => {
                this.setState({blockedHalls: result});
            });

    }


    render() {
        if (!this.state.isLoaded)
            return <Loading />

        const activeHalls = [];
        if(this.state.activeHalls.length > 0) {
            let item = {values: []}
            for (let i = this.state.activeHalls.length-1; i >= 0; i--) {
                item.values.push(

							<div className='main hall update_block_wrap__item styling__tem'>
								<div className='update_block_button__item'>
									<div className='text'>
										<h3>Name: {this.state.activeHalls[i].name}</h3>
										<button
											onClick={() =>
												window.location.replace(
													'/admin/update-hall/' +
														this.state.activeHalls[i].id
												)
											}
											className='update-button'
										>
											Update
										</button>
										<button
											id={this.state.activeHalls[i].id}
											onClick={this.block}
											className='block-button'
										>
											Block
										</button>
									</div>
								</div>
							</div>
						)

            }
            activeHalls.push(item)
        }
        const blockedHalls = [];
        if(this.state.blockedHalls.length > 0) {
            let item = {values: []}
            for (let i = this.state.blockedHalls.length-1; i >= 0; i--) {
                item.values.push(
							<div className='main hall update_block_wrap__item styling__tem'>
								<div className='update_block_button__item'>
									<div className='text'>
										<h3>Name: {this.state.blockedHalls[i].name}</h3>
										<button
											onClick={() =>
												window.location.replace(
													'/admin/update-hall/' +
														this.state.activeHalls[i].id
												)
											}
											className='update-button'
										>
											Update
										</button>
										<button
											id={this.state.blockedHalls[i].id}
											onClick={this.unblock}
											className='block-button'
										>
											Unblock
										</button>
									</div>
								</div>
							</div>
						)
            }
            blockedHalls.push(item)
        }

        return (
            <div className="user_list__item">
                <div className="lists_wrapper__item">
                    <div className="button-box row">
                        <div className="col-md-6"><BackButton backPath={() => this.props.history.goBack()} /></div>
                        <div className="col-md-6"><button onClick={()=>window.location.replace("/admin/create-hall/")} className="create-button">Create</button></div>
                    </div>
                    <fieldset className="movies_manage_wrap__item">
                        <input type="radio" name="sizeBy" value="weight" id="sizeWeight" defaultChecked/>
                        <label id="bookedlist_controller__item" className="radio_button__item" htmlFor="sizeWeight">
                            Active halls
                        </label>

                        <input type="radio" name="sizeBy" value="dimensions" id="sizeDimensions"/>
                        <label id="historylist_controller__item" className="radio_button__item"
                               htmlFor="sizeDimensions">Blocked halls</label>

                        <div className="movies_block_wrap__item div__item" id="booked_list__item">
                            {
                                activeHalls.map(el =>{
                                    return el.values
                                })}
                        </div>
                        <div className="movies_block_wrap__item div__item" id="history_list__item">
                            {
                                blockedHalls.map(el =>{
                                    return el.values
                                })}
                        </div>
                    </fieldset>
                </div>
            </div>
        )
    }
}
export default AllHalls;