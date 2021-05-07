import React from 'react'
import BackButton from '../../backButton/BackButton'
import HallService from '../../../services/HallService'
import AccountService from '../../../services/AccountService'
import Loading from '../../Loading/Loading'

class UpdateHall extends React.Component {
	constructor(props) {
		super(props)
		this.state = {
			id: props.match.params.hallId,
			isLoaded: false,
			hall: {
				id: Number,
				name: String,
				rowsAmount: Number,
				places: Number,
			},
			newHall: {
				id: Number,
				name: String,
				rowsAmount: Number,
				places: Number,
			},

			nameError: '',
			rowsAmountError: '',
			placesError: '',

			nameBorderColor: '',
			rowsAmountBorderColor: '',
			placesBorderColor: '',

			borderColorRed: '2px solid red',
			borderColorGreen: '2px solid green',
			requiredError: 'This field is required',
			updateHallFailed: false,
		}
	}

	componentDidMount = async () => {
		AccountService.isAdmin()
			.then(() => {
				this.setState({
					isLoaded: true,
				})
				HallService.getById(this.state.id).then(result => {
					this.setState({ hall: result })
				})
			})
			.catch(() => {
				window.location.replace('/')
			})
	}
	closeButton = () => {
		this.setState({ updateHallFailed: false })
	}
	updateHall = async event => {
		event.preventDefault()

		if (
			this.state.nameError === '' &&
			this.state.rowsAmountError === '' &&
			this.state.placesError === ''
		) {
			await HallService.updateHall(this.state.id, this.state.newHall)
				.then(response => {
					if (response.ok) window.location.replace('/admin/halls')
					else this.setState({ updateHallFailed: true })
				})
				.catch(error => {
					console.log(error)
					this.setState({ updateHallFailed: true })
				})
		}
	}
	correctName = async () => {
		if (this.state.newHall.name === '') {
			this.setState({ nameError: this.state.requiredError })
			this.setState({ nameBorderColor: this.state.borderColorRed })
		} else if (this.state.newHall.name == this.state.hall.name)
			this.setState({ nameError: '', nameBorderColor: '' })
		else {
			const isNameFree = await HallService.isNameFree(
				this.state.newHall.name
			)
			if (!isNameFree) {
				this.setState({ nameError: 'This name is already used' })
				this.setState({ nameBorderColor: this.state.borderColorRed })
			} else {
				this.setState({ nameError: '' })
				this.setState({ nameBorderColor: this.state.borderColorGreen })
			}
		}
	}
	correctRows = () => {
		if (!this.state.newHall.rowsAmount) {
			this.setState({ rowsAmountError: this.state.requiredError })
			this.setState({ rowsAmountBorderColor: this.state.borderColorRed })
		} else if (this.state.newHall.rowsAmount == this.state.hall.rowsAmount)
			this.setState({ rowsAmountError: '', rowsAmountBorderColor: '' })
		else if (Number(this.state.newHall.rowsAmount) <= 0) {
			this.setState({ rowsAmountError: 'The rows can`t be <=0' })
			this.setState({ rowsAmountBorderColor: this.state.borderColorRed })
		} else {
			this.setState({ rowsAmountError: '' })
			this.setState({ rowsAmountBorderColor: this.state.borderColorGreen })
		}
	}
	correctPlaces = () => {
		if (!this.state.newHall.places) {
			this.setState({ placesError: this.state.requiredError })
			this.setState({ placesBorderColor: this.state.borderColorRed })
		} else if (this.state.newHall.places == this.state.hall.places)
			this.setState({ placesError: '', placesBorderColor: '' })
		else if (Number(this.state.newHall.places) <= 0) {
			this.setState({ placesError: 'The places can`t be <=0' })
			this.setState({ placesBorderColor: this.state.borderColorRed })
		} else {
			this.setState({ placesError: '' })
			this.setState({ placesBorderColor: this.state.borderColorGreen })
		}
	}
	changeHandler = async e => {
		switch (e.target.name) {
			case 'name':
				this.state.newHall.name = e.target.value
				await this.correctName()
				break
			case 'rowsAmount':
				this.state.newHall.rowsAmount = e.target.value
				this.correctRows()
				break
			case 'places':
				this.state.newHall.places = e.target.value
				this.correctPlaces()
				break
		}
	}

	render() {
        if (!this.state.isLoaded)
            return <Loading />

		return (
			<div className='login_block'>
				<BackButton backPath={() => this.props.history.goBack()} />
				<div>
					{this.state.updateHallFailed && (
						<a style={{ color: 'red' }}>
							An unknown error occurred
							<button
								onClick={this.closeButton}
								className='btn-close'
								aria-label='Close'
							>
								x
							</button>
						</a>
					)}
				</div>
				<form onSubmit={this.updateHall}>
					<div>
						<h3>Name:</h3>
						<input
							defaultValue={this.state.hall.name}
							onChange={e => this.changeHandler(e)}
							type='text'
							name='name'
							className='form-control col-md-12'
							style={{ border: this.state.nameBorderColor }}
						/>
						<div
							className='create_movie_error__item'
							style={{ color: 'red' }}
						>
							{this.state.nameError}
						</div>
					</div>
					<div>
						<h3>Rows:</h3>
						<input
							defaultValue={this.state.hall.rowsAmount}
							onChange={e => this.changeHandler(e)}
							type='number'
							name='rowsAmount'
							className='form-control col-md-12'
							style={{ border: this.state.rowsAmountBorderColor }}
						/>
						<div
							className='create_movie_error__item'
							style={{ color: 'red' }}
						>
							{this.state.rowsAmountError}
						</div>
					</div>
					<div>
						<h3>Places:</h3>
						<input
							defaultValue={this.state.hall.places}
							onChange={e => this.changeHandler(e)}
							type='number'
							name='places'
							className='form-control col-md-12'
							style={{ border: this.state.placesBorderColor }}
						/>
						<div
							className='create_movie_error__item'
							style={{ color: 'red' }}
						>
							{this.state.placesError}
						</div>
					</div>

					<div>
						<button
							id='btn_singin__item'
							className='w-100 btn btn-lg btn-primary'
							type='submit'
						>
							Update
						</button>
					</div>
				</form>
			</div>
		)
	}
}
export default UpdateHall
