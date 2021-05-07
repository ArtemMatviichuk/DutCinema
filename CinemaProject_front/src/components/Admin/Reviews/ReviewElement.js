import React from 'react'
import { AiFillDelete } from 'react-icons/ai'
import { Link } from 'react-router-dom'
import ReviewService from '../../../services/ReviewService'
import './Review.css'

class ReviewElement extends React.Component {
	constructor(props) {
		super(props)
		this.state = {
			error: null,
			review: props.review,
			reqiureSureDelete: false,
			readAll: false,
		}

		this.deleteReview = this.deleteReview.bind(this)
		this.markToDelete = this.markToDelete.bind(this)
		this.readAllHandler = this.readAllHandler.bind(this)
	}

	deleteReview(event) {
		ReviewService.deleteReview(this.state.review.id)
			.then(() => {
				this.setState({
					review: null,
				})
			})
			.catch(err => {
				this.setState({
					error: err,
				})
			})
	}

	markToDelete(event) {
		this.setState({
			reqiureSureDelete: true,
		})
	}

	readAllHandler(event) {
		this.setState({
			readAll: !this.state.readAll,
		})
	}

	render() {
		const { review, error, reqiureSureDelete, readAll } = this.state

		if (review == null) return <div></div>

		return (
			<div>
				<div className='session__item row'>
					<div className='col-md-1'>{review.id}</div>
					<div className='col-md-2'>
						{review.firstName + ' ' + review.lastName}
					</div>
					<div className='col-md-2'>
						<Link to={'/movies/' + review.movieId}>
							{review.movieTitle}
						</Link>
					</div>
					<div className='col-md-2'>{review.creationDate}</div>

					<div className='col-md-4 review__manage__text'>
						{(!readAll && review.text.length > 30) && review.text.substring(0, 30) + '...'}
						{(readAll || 	 review.text.length < 30) && review.text}
						<br />
						{review.text.length > 30 && (
							<button onClick={this.readAllHandler}>
								{readAll ? 'Read less' : 'Read more'}
							</button>
						)}
					</div>

					<div className='col-md-1 session__remove'>
						<div onClick={this.markToDelete}>
							{!reqiureSureDelete && (
								<AiFillDelete className='session__remove__btn' />
							)}
						</div>
						{reqiureSureDelete && (
							<button
								className='session__remove__sure'
								onClick={this.deleteReview}
							>
								DELETE
							</button>
						)}
					</div>
				</div>
				{error && <p>{error.message}</p>}
			</div>
		)
	}
}

export default ReviewElement
