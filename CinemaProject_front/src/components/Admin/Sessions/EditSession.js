import React from 'react'
import SessionService from '../../../services/SessionService'
import BackButton from '../../backButton/BackButton'
import Loading from '../../Loading/Loading'
import MovieService from '../../../services/MovieService'
import HallService from '../../../services/HallService'
import ErrorComponent from '../../error/ErrorComponent'
import './CreateEditeSessions.css'
import AccountService from '../../../services/AccountService'

class EditSession extends React.Component {
   constructor(props) {
      super(props)
      this.state = {
         isLoaded: false,
         errorPost: null,
         error: null,
         sessionId: props.match.params.sessionId,
         session: {},
         halls: [],
         titles: [
            {
               id: Number,
               title: String,
            },
         ],
         newSession: {
            movieId: Number,
            hallId: Number,
            date: Date,
         },
      }

      this.changeDate = this.changeDate.bind(this)
      this.changeHall = this.changeHall.bind(this)
      this.changeMovie = this.changeMovie.bind(this)

      this.confirmEdit = this.confirmEdit.bind(this)
   }

   componentDidMount() {
      AccountService.isAdmin()
         .then(() => {
            SessionService.getSession(this.state.sessionId)
					.then(res => {
						this.state.session = res
						this.state.newSession = res
						HallService.getAllActive()
							.then(result => {
								this.state.halls = result.sort((e1, e2) => {
									return e1.rowsAmount * e1.places >=
										e2.rowsAmount * e2.places
										? 1
										: -1
								})
								MovieService.getTitles()
									.then(result => {
										this.setState({
											isLoaded: true,
											titles: result,
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
					.catch(err => {
						this.setState({
							isLoaded: true,
							error: err,
						})
					})
         })
         .catch(() => { window.location.replace('/') })
   }

   confirmEdit(event) {
      event.preventDefault()
      SessionService.updateSession(this.state.sessionId, this.state.newSession)
         .then(() => {
            this.props.history.push('/admin/sessions')
         })
         .catch((err) => {
            this.setState({
               errorPost: err,
               isLoaded: true,
            })
         })
   }

   changeMovie(event) {
      this.state.newSession.movieId = event.target.value
   }

   changeHall(event) {
      this.state.newSession.hallId = event.target.value
   }

   changeDate(event) {
      this.state.newSession.date = event.target.value
   }

   render() {
      const { errorPost, isLoaded, session, titles, halls, error } = this.state
      if (!isLoaded) {
         return <Loading />
      } else {
         return (
            <div className='container editcreate_wrap__item'>
               <BackButton backPath={() => this.props.history.goBack()} />
               <br />
               {errorPost && errorPost.message}
               <form onSubmit={this.confirmEdit}>
                  <label row>
                     <span className>Hall: </span>
                     <select
                        onChange={this.changeHall}
                        defaultValue={session.hallId}
                        className="label_sessions_editcreate__item"
                     >
                        {halls.map((el) => {
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
                     <span style={{marginLeft:'-14px'}}>Movie: </span>
                     <select
                        onChange={this.changeMovie}
                        defaultValue={session.movieId}
                        className="label_sessions_editcreate__item"
                     >
                        {titles.map((el) => {
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
                     <span style={{marginLeft:'-4px'}}>Date: </span>
                     <input
                        type='datetime-local'
                        defaultValue={session.date}
                        onChange={this.changeDate}
                        className="label_sessions_editcreate__item"
                     />
                  </label>
                  <br />
                  <input  className="apply_button__item" type='submit' value='Apply' />
               </form>
            </div>
         )
      }
   }
}

export default EditSession
