import React from "react";
import BackButton from "../../backButton/BackButton";
import HallService from "../../../services/HallService";
import AccountService from "../../../services/AccountService";
import Loading from "../../Loading/Loading";

class CreateHall extends React.Component {

    constructor(props) {
        super(props);
        this.state={
            isLoaded: false,
            hall:{
                name: String,
                rowsAmount: Number,
                places: Number
            },

            nameError: "",
            rowsAmountError: "",
            placesError: "",

            nameBorderColor:"",
            rowsAmountBorderColor:"",
            placesBorderColor:"",

            borderColorRed: "2px solid red",
            borderColorGreen: "2px solid green",
            requiredError: 'This field is required',
            createHallFailed:false
        }
    }

    componentDidMount(){
        AccountService.isAdmin()
            .then(() => {
                this.setState({
                    isLoaded: true
                })
            })
            .catch(() => { window.location.replace('/') })
    }

    createHall= async(event)=>{
        event.preventDefault();

        if(this.state.nameBorderColor===this.state.borderColorGreen && this.state.rowsAmountBorderColor===this.state.borderColorGreen && this.state.placesBorderColor===this.state.borderColorGreen){
            await HallService.addHall(this.state.hall)
                .then((response) => {
                    console.log(response)
                    if(response.ok){
                        window.location.replace("/admin/halls");
                    }
                    else
                        this.setState({createHallFailed:true});
                })
                .catch((error)=>{
                    console.log(error);
                    this.setState({createHallFailed:true});
                });
        }
        else{
            if(this.state.nameBorderColor===''){
                this.setState({nameError: this.state.requiredError})
                this.setState({nameBorderColor:this.state.borderColorRed})
            }
            if(this.state.placesBorderColor===''){
                this.setState({placesError: this.state.requiredError})
                this.setState({placesBorderColor:this.state.borderColorRed})
            }
            if(this.state.rowsAmountBorderColor===''){
                this.setState({rowsAmountError: this.state.requiredError})
                this.setState({rowsAmountBorderColor:this.state.borderColorRed})
            }
        }
    }
    correctName = async () => {
        if ((this.state.hall.name) === '') {
            this.setState({nameError: this.state.requiredError})
            this.setState({nameBorderColor:this.state.borderColorRed})
        }
        else{
            const isNameFree = await HallService.isNameFree(this.state.hall.name);
            if(!isNameFree) {
                this.setState({nameError: 'This name is already used'})
                this.setState({nameBorderColor:this.state.borderColorRed})
            }
            else {
                this.setState({nameError: ''})
                this.setState({nameBorderColor:this.state.borderColorGreen})
            }
        }

    };
    correctRows = () => {
        if(!this.state.hall.rowsAmount){
            this.setState({rowsAmountError: this.state.requiredError})
            this.setState({rowsAmountBorderColor:this.state.borderColorRed})
        }
        else if (Number(this.state.hall.rowsAmount) <= 0) {
            this.setState({rowsAmountError: "The rows can`t be <=0"})
            this.setState({rowsAmountBorderColor:this.state.borderColorRed})
        }
        else {
            this.setState({rowsAmountError: ''})
            this.setState({rowsAmountBorderColor:this.state.borderColorGreen})
        }
    };
    correctPlaces = () => {
        if(!this.state.hall.places){
            this.setState({placesError: this.state.requiredError})
            this.setState({placesBorderColor:this.state.borderColorRed})
        }
        else if (Number(this.state.hall.places) <= 0) {
            this.setState({placesError: "The places can`t be <=0"})
            this.setState({placesBorderColor:this.state.borderColorRed})
        }
        else {
            this.setState({placesError: ''})
            this.setState({placesBorderColor:this.state.borderColorGreen})
        }
    };
    changeHandler = async (e) =>{
        switch (e.target.name){
            case 'name':
                this.state.hall.name=e.target.value;
                await this.correctName();
                break
            case 'rowsAmount':
                this.state.hall.rowsAmount=e.target.value;
                this.correctRows();
                break
            case 'places':
                this.state.hall.places=e.target.value;
                this.correctPlaces();
                break
        }
    };
    blurHandler = async (e) =>{
        switch (e.target.name){
            case 'name':
                await this.correctName();
                break
            case 'rowsAmount':
                this.correctRows();
                break
            case 'places':
                this.correctPlaces();
                break
        }
    };

    closeButton = () =>{
        this.setState({createHallFailed: false});
    }

    render() {
        if (!this.state.isLoaded)
            return <Loading />
            
        return(
            <div className="login_block row">
                <BackButton backPath={() => this.props.history.goBack()} />
                <div>
                    {(this.state.createHallFailed) && <a style={{color: 'red'}}>An unknown error occurred
                        <button onClick={this.closeButton} className="btn-close" aria-label="Close">x</button>
                    </a>}
                </div>
                <form onSubmit={this.createHall}>
                    <div>
                        <h3>Name:</h3>
                        <input value={this.name} onChange={e => this.changeHandler(e) }
                               type="text" name="name" className="form-control col-md-12"
                               style={{border: this.state.nameBorderColor}} onBlur={e => this.changeHandler(e)} />
                        <div className="create_movie_error__item" style={{color: 'red'}}>{this.state.nameError}</div>
                    </div>
                    <div>
                        <h3>Rows:</h3>
                        <input value={this.rowsAmount} onChange={e => this.changeHandler(e) }
                               type="number" name="rowsAmount" className="form-control col-md-12"
                               style={{border: this.state.rowsAmountBorderColor}} onBlur={e => this.changeHandler(e)} />
                        <div className="create_movie_error__item" style={{color: 'red'}}>{this.state.rowsAmountError}</div>
                    </div>
                    <div>
                        <h3>Places:</h3>
                        <input value={this.places} onChange={e => this.changeHandler(e) }
                               type="number" name="places" className="form-control col-md-12"
                               style={{border: this.state.placesBorderColor}} onBlur={e => this.changeHandler(e)} />
                        <div className="create_movie_error__item" style={{color: 'red'}}>{this.state.placesError}</div>
                    </div>
                    <div>
                        <button id="btn_create__item" className="w-60 btn btn-lg btn-primary" type="submit">Create</button>
                    </div>
                </form>
            </div>
        );
    }
}
export default CreateHall;