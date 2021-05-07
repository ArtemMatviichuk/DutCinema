import React from 'react'
import { Link } from 'react-router-dom'
import AccountService from '../../services/AccountService'
import ErrorComponent from '../error/ErrorComponent'
import Loading from '../Loading/Loading'
import './AdminMain.css'

class AdminMain extends React.Component{
    constructor(props){
        super(props)
        this.state = {
            isLoaded: false,
            error: null
        }
        
    }

    componentDidMount(){
        AccountService.isAdmin()
            .then(() => {
                this.setState({
                    isLoaded: true
                })
            })
            .catch(()=>{ window.location.replace("/") })
    }

    render(){
        const { isLoaded, error } = this.state
        if (!isLoaded)
            return <Loading />
        else if (error)
            return <ErrorComponent error={error} />

        return <div className="container admin_wrap__item ">
                    <div className="row d-flex justify-content-center row_admin__item">
                        <Link to={'/admin/sessions'} className="col-md-3 session_managing__item"    >Sesseions managing</Link> 
                        <Link to={'/admin/all-movies'} className="col-md-3 session_managing__item" >Movies managing</Link> 
                        <Link to={'/admin/halls'} className="col-md-3 session_managing__item" >Halls managing</Link>
                    </div>
                    <div  className="row d-flex justify-content-center row_admin__item">
                        <Link to={'/admin/genres'} className="col-md-3 session_managing__item" >Genres managing</Link> 
                        <Link to={'/admin/reviews'} className="col-md-3 session_managing__item" >Reviews managing</Link> 
                        <Link to={'/admin/users'} className="col-md-3 session_managing__item" >Users managing</Link>    
                    </div>    
                </div>
    }
}

export default AdminMain