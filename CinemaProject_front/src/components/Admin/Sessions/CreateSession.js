import React from 'react'
import SessionService from '../../../services/SessionService'
import BackButton from '../../backButton/BackButton'
import Loading from '../../Loading/Loading'
import MovieService from '../../../services/MovieService'
import HallService from '../../../services/HallService'
import ErrorComponent from '../../error/ErrorComponent'
import moment from 'moment'
import './CreateEditeSessions.css'
import AccountService from '../../../services/AccountService'

class CreateSession extends React.Component {
   constructor(props) {
      super(props)
      this.state = {
         isLoaded: false,
         error: null,
         halls: [
            {
               id: Number,
               name: String,
               rowsAmount: Number,
               places: Number,
            },
         ],
         titles: [
            {
               id: Number,
               title: String,
            },
         ],
         session: {
            movieId: -1,
            hallId: -1,
            date: moment(),
         },
         requireSure: false,
      }

      this.changeDate = this.changeDate.bind(this)
      this.changeHall = this.changeHall.bind(this)
      this.changeMovie = this.changeMovie.bind(this)

      this.confirmCreation = this.confirmCreation.bind(this)
   }

   componentDidMount() {
      AccountService.isAdmin()
         .then(() => {
            HallService.getAllActive()
					.then(result => {
						this.state.halls = result.sort((e1, e2) => {
							return e1.rowsAmount * e1.places >=
								e2.rowsAmount * e2.places
								? 1
								: -1
						})
						MovieService.getTitles()
							.then(res => {
								this.setState({
									isLoaded: true,
									titles: res,
								})
							})
							.catch(err => {
								this.setState({
									isLoaded: true,
									error: err,
								})
							})
					})
					.catch(err => {
						this.setState({
							isLoaded: true,
							error: err,
						})
					})
         })
         .catch(() => { window.location.replace('/') })
   }

   changeMovie(event) {
      this.state.session.movieId = event.target.value
   }

   changeHall(event) {
      this.state.session.hallId = event.target.value
   }

   changeDate(event) {
      this.state.session.date = event.target.value
   }

    confirmCreation(event){
        event.preventDefault();
        console.log(this.state.session)
        if (this.state.session.movieId==-1)
            this.state.session.movieId = this.state.titles[0].id
        
        if (this.state.session.hallId==-1)
            this.state.session.hallId = this.state.halls[0].id

      SessionService.createSession(this.state.session)
         .then(() => {
            this.props.history.push('/admin/sessions')
         })
         .catch((err) => {
            this.setState({
               error: err,
               isLoaded: true,
            })
         })
   }

   render() {
      const { error, isLoaded, titles, halls } = this.state
      if (!isLoaded) {
         return <Loading />
      } else {
         return (
				<div className='container editcreate_wrap__item'>
					<BackButton backPath={() => this.props.history.goBack()} />
					<br />
					{error && error.message}
					<form onSubmit={this.confirmCreation}>
						<label>
							<span>Hall: </span>
							<select
								className='label_sessions_editcreate__item'
								onChange={this.changeHall}
								defaultValue={halls[0].id}
							>
								{halls.map(el => {
									return (
										<option key={el.id} value={el.id}>
											{el.name} ({el.rowsAmount * el.places} places)
										</option>
									)
								})}
							</select>
						</label>
						<br />
						<label>
							<span style={{ marginLeft: '-14px' }}>Movie: </span>
							<select
								className='label_sessions_editcreate__item'
								onChange={this.changeMovie}
								defaultValue={titles[0].id}
							>
								{titles.map(el => {
									return (
										<option key={el.id} value={el.id}>
											{el.title}
										</option>
									)
								})}
							</select>
						</label>
						<br />
						<label>
							<span style={{ marginLeft: '-4px' }}>Date: </span>
							<input
								type='datetime-local'
								defaultValue={moment()}
								onChange={this.changeDate}
								className='label_sessions_editcreate__item'
							/>
						</label>
						<br />
						<input
							className='apply_button__item'
							type='submit'
							value='Apply'
						/>
					</form>
				</div>
			)
      }
   }
}

export default CreateSession
