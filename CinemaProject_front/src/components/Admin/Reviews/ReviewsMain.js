import React from 'react'
import BackButton from '../../backButton/BackButton'
import ReviewElement from './ReviewElement'
import AccountService from '../../../services/AccountService'
import ReviewService from '../../../services/ReviewService'
import Loading from '../../Loading/Loading'
import { BsArrowUpDown } from 'react-icons/bs'
import moment from 'moment'

class ReviewsMain extends React.Component {
	constructor(props) {
		super(props)
		this.state = {
			error: null,
			isLoaded: false,
			reviews: [],
			lastSort: 'id',
			page: 1,
			perPage: 100,
			pages: 0,
		}

		this.sortByAuthor = this.sortByAuthor.bind(this)
		this.sortById = this.sortById.bind(this)
		this.sortByMovie = this.sortByMovie.bind(this)
		this.sortByDate = this.sortByDate.bind(this)

		this.applyChange = this.applyChange.bind(this)
		this.changePerPage = this.changePerPage.bind(this)
		this.changePage = this.changePage.bind(this)
	}

	componentDidMount() {
		AccountService.isAdmin()
			.then(() => {
				ReviewService.getAll()
					.then(result => {
						this.setState({
							reviews: result,
							isLoaded: true,
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
				reviews: this.state.reviews.sort((e1, e2) => {
					return e1.id >= e2.id ? 1 : -1
				}),
			})
		} else {
			this.setState({
				isLoaded: true,
				reviews: this.state.reviews.reverse(),
			})
		}
	}

	sortByAuthor(event) {
		this.setState({
			isLoaded: false,
		})
		if (this.state.lastSort != 'author') {
			this.setState({
				isLoaded: true,
				lastSort: 'author',
				reviews: this.state.reviews.sort((e1, e2) => {
					return e1.firstName + ' ' + e1.lastName >=
						e2.firstName + ' ' + e2.lastName
						? 1
						: -1
				}),
			})
		} else {
			this.setState({
				isLoaded: true,
				reviews: this.state.reviews.reverse(),
			})
		}
	}

	sortByMovie(event) {
		this.setState({
			isLoaded: false,
		})
		if (this.state.lastSort != 'movie') {
			this.setState({
				isLoaded: true,
				lastSort: 'movie',
				reviews: this.state.reviews.sort((e1, e2) => {
					return e1.movieTitle >= e2.movieTitle ? 1 : -1
				}),
			})
		} else {
			this.setState({
				isLoaded: true,
				reviews: this.state.reviews.reverse(),
			})
		}
	}

	sortByDate(event) {
		this.setState({
			isLoaded: false,
		})
		if (this.state.lastSort != 'date') {
			this.setState({
				isLoaded: true,
				lastSort: 'date',
				reviews: this.state.reviews.sort((e1, e2) => {
					return moment(e1.creationDate) >= moment(e2.creationDate)
						? 1
						: -1
				}),
			})
		} else {
			this.setState({
				isLoaded: true,
				reviews: this.state.reviews.reverse(),
			})
		}
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

	changePage(event) {
		this.setState({
			page: event.target.value,
		})
	}

	render() {
		const { isLoaded, error } = this.state

		if (!isLoaded) return <Loading />

		let reviews = this.state.reviews.slice(
			(this.state.page - 1) * this.state.perPage,
			this.state.page * this.state.perPage
		)

		this.state.pages = Math.ceil(
			this.state.reviews.length / this.state.perPage
		)
		console.log(this.state.pages)
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
					<h4>Reviews Managing</h4>
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
								onClick={this.sortByAuthor}
							/>
							<span>
								Author <BsArrowUpDown />
							</span>
						</label>
						<label className='session__hall col-md-2'>
							<input
								className='sessions_input__item'
								type='radio'
								name='sort'
								onClick={this.sortByMovie}
							/>
							<span>
								Movie <BsArrowUpDown />
							</span>
						</label>
						<label className='session__hall col-md-2'>
							<input
								className='sessions_input__item'
								type='radio'
								name='sort'
								onClick={this.sortByDate}
							/>
							<span>
								Created <BsArrowUpDown />
							</span>
						</label>
						<div className='session__edit col-md-4'>
							<span>Text</span>
						</div>
						<div className='session__edit col-md-1'>
							<span>Delete</span>
						</div>
					</div>
					<div className='sessions_elements__item'>
						{reviews.map(el => {
							return <ReviewElement review={el} key={el.id} />
						})}
					</div>
				</div>
				{pageButtons}
			</div>
		)
	}
}

export default ReviewsMain
