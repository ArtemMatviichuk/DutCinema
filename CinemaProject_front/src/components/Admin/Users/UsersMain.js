import React from 'react'
import UserService from '../../../services/UserService'
import { BsArrowUpDown } from 'react-icons/bs'
import ErrorComponent from '../../error/ErrorComponent'
import Loading from '../../Loading/Loading'
import BackButton from '../../backButton/BackButton'
import UsersElement from './UserElement'
import AccountService from '../../../services/AccountService'

class UsersMain extends React.Component {
	constructor(props) {
		super(props)
		this.state = {
			error: null,
			isLoaded: false,
			users: [],
			lastSort: 'id',
			page: 1,
			perPage: 100,
			pages: 0,
		}
		this.sortById = this.sortById.bind(this)
		this.sortByName = this.sortByName.bind(this)
		this.sortByLastName = this.sortByLastName.bind(this)

		this.changePage = this.changePage.bind(this)
		this.changePerPage = this.changePerPage.bind(this)
		this.applyChange = this.applyChange.bind(this)
	}

	componentDidMount() {
		AccountService.isAdmin()
			.then(() => {
				UserService.getAll()
					.then(result => {
						this.setState({
							isLoaded: true,
							users: result,
							pages: Math.ceil(result.length / this.state.perPage),
						})
					})
					.catch(err => {
						this.setState({
							isLoaded: true,
							error: err,
						})
					})
			})
			.catch(() => {
				window.location.replace('/')
			})
	}

	sortById(event) {
		this.setState({
			isLoaded: false,
		})
		if (this.state.lastSort != 'id') {
			this.setState({
				isLoaded: true,
				lastSort: 'id',
				users: this.state.users.sort((e1, e2) => {
					return e1.id >= e2.id ? 1 : -1
				}),
			})
		} else {
			this.setState({
				isLoaded: true,
				users: this.state.users.reverse(),
			})
		}
	}

	sortByName(event) {
		this.setState({
			isLoaded: false,
		})
		if (this.state.lastSort != 'name') {
			this.setState({
				isLoaded: true,
				lastSort: 'name',
				users: this.state.users.sort((e1, e2) => {
					return e1.firstName >= e2.firstName ? 1 : -1
				}),
			})
		} else {
			this.setState({
				isLoaded: true,
				users: this.state.users.reverse(),
			})
		}
	}

	sortByLastName(event) {
		this.setState({
			isLoaded: false,
		})
		if (this.state.lastSort != 'lastName') {
			this.setState({
				isLoaded: true,
				lastSort: 'lastName',
				users: this.state.users.sort((e1, e2) => {
					return e1.lastName >= e2.lastName ? 1 : -1
				}),
			})
		} else {
			this.setState({
				isLoaded: true,
				users: this.state.users.reverse(),
			})
		}
	}

	changePage(event) {
		this.setState({
			page: event.target.value,
		})
	}

	changePerPage(event) {
		if (event.target.value <= 100 && event.target.value >= 1)
			this.state.perPage = event.target.value
	}

	applyChange(event) {
		this.setState({
			page: 1,
		})
	}

	render() {
		const { error, isLoaded } = this.state
		if (!isLoaded) {
			return <Loading />
		} else if (error) {
			return <ErrorComponent error={error} />
		} else {
			let users = this.state.users.slice(
				(this.state.page - 1) * this.state.perPage,
				this.state.page * this.state.perPage
			)

			this.state.pages = Math.ceil(
				this.state.users.length / this.state.perPage
			)
			let pageButtons = []
			for (let i = 1; i <= this.state.pages; i++) {
				pageButtons.push(
					<label className='page_button__item' key={i}>
						<input
							className='page_input__item'
							type='radio'
							defaultChecked={i == this.state.page}
							name='pages'
							onChange={this.changePage}
							value={i}
						/>
						<div className='page_label'>{i}</div>
					</label>
				)
			}

			return (
				<div className='container'>
					<div className='d-flex justify-content-between sessions__label'>
						<BackButton backPath={() => this.props.history.goBack()} />
						<h4>Users Managing</h4>
						<label>
							<span>Items per page: </span>
							<input
								id='num_items_input__item'
								type='number'
								min='1'
								max='100'
								defaultValue={this.state.perPage}
								onChange={this.changePerPage}
							/>
							<button id='button_apply__item' onClick={this.applyChange}>
								Apply
							</button>
						</label>
					</div>
					<div className='sessions__container'>
						<div className='sessions__header row'>
							<label className='session__id col-md-1' id='sesId'>
								<input
									className='sessions_input__item'
									type='radio'
									name='sort'
									onClick={this.sortById}
									defaultChecked
								/>
								<span>
									ID <BsArrowUpDown />
								</span>
							</label>
							<label className='session__title col-md-2'>
								<input
									className='sessions_input__item'
									type='radio'
									name='sort'
									onClick={this.sortByName}
								/>
								<span>
									First Name <BsArrowUpDown />
								</span>
							</label>
							<label className='session__hall col-md-2'>
								<input
									className='sessions_input__item'
									type='radio'
									name='sort'
									onClick={this.sortByLastName}
								/>
								<span>
									Last Name <BsArrowUpDown />
								</span>
							</label>
							<div className='session__edit col-md-2'>
								<span>Status</span>
							</div>
							<div className='session__edit col-md-2'>
								<span>Role</span>
							</div>
							<div className='session__delete col-md-2'>
								<span>Update Role</span>
							</div>
							<div className='session__delete col-md-1'>
								<span>Block</span>
							</div>
						</div>
						<div className='sessions_elements__item'>
							{users.map(el => {
								return <UsersElement user={el} key={el.id} />
							})}
						</div>
					</div>
					<div className='session__pageButtons'>{pageButtons}</div>
				</div>
			)
		}
	}
}

export default UsersMain
