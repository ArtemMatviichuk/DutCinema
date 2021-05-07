import React from 'react'
import AccountService from '../../services/AccountService'
import ReviewService from '../../services/ReviewService'
import Loading from '../Loading/Loading'

class Review extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            isLoaded: true,
            error: null,
            errorUpdate: null,
            review: this.props.review,
            edit: false,
            newText: this.props.review.text,
            onDelete: this.props.onDelete
        }

        this.editReview = this.editReview.bind(this)
        this.submitEdit = this.submitEdit.bind(this)
        this.deleteReview = this.deleteReview.bind(this)
        this.handleTextChange = this.handleTextChange.bind(this)
    }

    editReview(event){
        this.setState({
            edit: true
        })
    }

    handleTextChange(event){
        this.setState({
            newText: event.target.value
        });
    }

    submitEdit(event){
        event.preventDefault()
        
        if (this.state.newText.length < 5){
            this.setState({
                errorUpdate: {
                    message: "Text is too short"
                }
            })
            return
        }

        this.setState({
            isLoaded: false
        })
        ReviewService.updateReview(this.state.review.id, this.state.newText)
            .then(result => {
                this.setState({
                    edit: false,
                    isLoaded: true,
                    review: result
                })
            })
            .catch(err => {
                this.setState({
                    isLoaded: true,
                    errorUpdate: err
                })
            })
    }

    deleteReview(event){
        this.setState({
            isLoaded: false
        })
        ReviewService.deleteReview(this.state.review.id)
            .then(() => {
                this.setState({
                    isLoaded: true
                })
                this.state.onDelete(this.state.review.id)
            })
            .catch(err => {
                this.setState({
                    isLoaded: true,
                    errorUpdate: err
                })
            })
    }

    render(){
        const { review, error, isLoaded } = this.state
        if (!isLoaded)
            return <Loading />
        else if (error)
            return <div className="col-md-12 review">{error.message}</div>
        else if (review==null)
            return <div></div>

        return (
				<div className='col-md-12 review'>
					<div className='d-flex justify-content-between col-md-12 review__header'>
						<h6 className='col-md-5 text-left'>
							{review.firstName + ' ' + review.lastName}
						</h6>
						<span className='col-md-5 text-right'>
							{review.creationDate}
						</span>
						{review.authorId == AccountService.getId() && (
							<div>
								<button
									className='review__manage__buttons'
									onClick={this.editReview}
								>
									Edit
								</button>
								<button
									className='review__manage__buttons'
									onClick={this.deleteReview}
								>
									Delete
								</button>
							</div>
						)}
					</div>
					<div>
						{!this.state.edit && (
							<p className='col-md-12 review__text text-left'>
								{review.text}
							</p>
						)}
						{this.state.edit && (
							<form onSubmit={this.submitEdit}>
								<textarea
									className='col-md-12 newReview__text form-control'
									placeholder='Write your review (minimum 5 characters)'
									rows='5'
									onChange={this.handleTextChange}
									defaultValue={this.state.newText}
								/>
								{this.state.errorUpdate != null && (
									<p>{this.state.errorUpdate.message}</p>
								)}
								<input
									type='submit'
									value='Submit'
									className='buttonEdit'
								/>
							</form>
						)}
					</div>
				</div>
			)
    }
}

export default Review
