import React from 'react'
import { TiDelete } from 'react-icons/ti'
import { AiFillEdit } from 'react-icons/ai'
import { Link } from 'react-router-dom'
import UserService from '../../../services/UserService'

class UsersElement extends React.Component {
	constructor(props) {
		super(props)
		this.state = {
			error: null,
			user: props.user,
			reqiureSureBlock: false,
			reqiureSureAccess: false,
		}

		this.blockUser = this.blockUser.bind(this)
		this.markToBlock = this.markToBlock.bind(this)
        this.markToAccess = this.markToAccess.bind(this)
		this.grandAccess = this.grandAccess.bind(this)
	}

	markToBlock(event) {
		this.setState({
			reqiureSureBlock: !this.state.reqiureSureBlock,
		})
	}

	markToAccess(event) {
		this.setState({
			reqiureSureAccess: !this.state.reqiureSureAccess,
		})
	}

	blockUser(event) {
		UserService.blockUser(this.state.user.id)
			.then(result => {
				this.setState({
					user: result,
					reqiureSureBlock: false,
				})
			})
			.catch(err => {
				this.setState({
					error: err,
				})
			})
	}

	grandAccess() {
		UserService.changeUserRole(this.state.user.id)
			.then((result) => {
				this.setState({
					user: result,
					reqiureSureAccess: false
				})
			})
			.catch(err => {
				this.setState({
					error: err,
					reqiureSureAccess: false,
				})
			})
	}

	render() {
		const { user, error, reqiureSureBlock, reqiureSureAccess } = this.state

		if (user == null) return <div></div>

		return (
			<div className='all-session'>
				<div
					className={
						user.status == 'Blocked'
							? 'session__item row session__canceled'
							: 'session__item row'
					}
				>
					<label className='session__id col-md-1'>{user.id}</label>
					<div className='session__movie col-md-2'>{user.firstName}</div>
					<div className='session__hall col-md-2'>{user.lastName}</div>
					<div className='col-md-2 session__edit'>{user.status}</div>
					<div className='col-md-2 session__edit'>
						{user.roles.map(el => el)}
					</div>
					<div className='col-md-2 session__cancel'>
						<div onClick={this.markToAccess}>
							{!reqiureSureAccess && (
								<AiFillEdit className='session__cancel__btn' />
							)}
						</div>
						{reqiureSureAccess && (
							<button
								className='session__cancel__sure'
								onClick={this.grandAccess}
							>
								{user.roles.includes('ADMIN') ? 'Make as user' : 'Make as admin'}
							</button>
						)}
					</div>
					<div className='col-md-1 session__cancel'>
						<div onClick={this.markToBlock}>
							{!reqiureSureBlock && (
								<TiDelete className='session__cancel__btn' />
							)}
						</div>
						{reqiureSureBlock && (
							<button
								className='session__cancel__sure'
								onClick={this.blockUser}
							>
								{user.status == 'Blocked' ? 'Unblock' : 'Block'}
							</button>
						)}
					</div>
				</div>
				{error && <p className='error__message'>{error.message}</p>}
			</div>
		)
	}
}

export default UsersElement
